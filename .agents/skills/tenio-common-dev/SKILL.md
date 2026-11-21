---
name: tenio-common-dev
description: >-
  Engineering guide, performance patterns, concurrency invariants, and testing/PR
  workflows for developing and auditing `io.github.congcoi123:tenio-common`.
  Use when modifying `ZeroUtility`, `MsgPackUtility`, `StringBuilderPool`,
  `TaskManagerImpl`, `WorkerPool`, `XmlUtility`, or preparing PRs in `tenio-common`.
---

# `tenio-common` Development & Audit Skill

Captured from auditing, optimizing, testing, and submitting PRs to `io.github.congcoi123:tenio-common` (`1.0.0`, Java 21).

## 1. Build, Checkstyle, Coverage & Test Gotchas

- **Early Validation Gates**:
  - `maven-checkstyle-plugin` runs in the `validate` phase with `failsOnError=true` (Google Java Style, **max 100 chars/line**, explicit `{}` braces on all control flow, no star imports, no tabs, MIT header `Copyright (c) 2016-2026 kong <congcoi123@gmail.com>`).
  - `maven-javadoc-plugin` runs in `compile` — document all public classes, interfaces, methods, enum constants, and public fields.
  - `jacoco-maven-plugin` enforces `>= 80%` line coverage (`COVEREDRATIO >= 0.80`) during `mvn verify`.
- **Surefire Test Execution Gotcha**:
  - `TenIOCommonTest` aggregates tests via `@SelectPackages` (`junit-platform-suite-engine`). Passing unqualified `-Dtest=...` can run `0` tests. Run `mvn test` directly to execute the full suite (`127+` tests).
- **JDK 27 + Mockito `5.14.1` Gotcha**:
  - On JDK 27, Mockito `5.14.1` fails when inline-mocking JDK classes (`java.util.UUID` in `StringUtilityTest` / `WorkerPoolRunnableTest`, `org.w3c.dom.Node` in `XmlUtilityTest`). Run `mvn verify` on **JDK 21** for full green Mockito + JaCoCo verification.
- **Fast Local Install for Downstream Modules (`tenio-core`, apps)**:
  ```sh
  mvn -Dgpg.skip=true -DskipTests -Dmaven.javadoc.skip=true install
  ```

## 2. Binary Serialization (`ZeroUtility` & `MsgPackUtility`)

### `Zero` Wire Format & Performance Rules
- **Wire Layout** (big-endian `ByteBuffer`, `ZeroType` codes `0..18`):
  - `ZERO_MAP (18)`: `[1B type][2B entryCount][entries: [2B keyLen][UTF-8 key][element]]`
  - `ZERO_ARRAY (17)`: `[1B type][2B itemCount][elements...]`
  - `STRING (8)`: `[1B type][2B utf8Len][UTF-8 bytes]`
  - `BYTE_ARRAY (10)`: `[1B type][4B byteLen][bytes]`
- **Avoid O(N²) Buffer Growth & Allocation Churn**:
  - Always grow the encoding `ByteBuffer` exponentially via `ensureCapacity(buffer, additionalBytes)` (`Math.min(MAX_MESSAGE_BYTES, Math.max(capacity << 1, required))`) rather than linear fixed-chunk increments.
  - Write primitives, map keys, arrays, and nested `ZeroMap`/`ZeroArray` directly into the shared `ByteBuffer` instead of allocating temporary per-field `ByteBuffer` / `byte[]` objects.
  - In `encodeStringArray`, encode each `String` to UTF-8 once (`List<byte[]>`) rather than calling `getBytes(StandardCharsets.UTF_8)` twice.
- **Symmetric Defensive Bounds**:
  - Enforce `MAX_MESSAGE_BYTES` (`16 MiB`), `MAX_BINARY_BYTES` (`8 MiB`), `MAX_SHORT_LENGTH` (`32767`), and `MAX_NESTING_DEPTH` (`64`) on **both** encode and decode paths so cyclic or deeply nested structures throw `IllegalArgumentException` instead of `StackOverflowError`.
  - Preserve `MAX_INPUT_BYTES`, `MAX_COLLECTION_SIZE`, and `MAX_NESTING_DEPTH` in `MsgPackUtility`.
- **Wire Backward Compatibility**:
  - Non-JVM clients (e.g., LibGDX Web/TeaVM) implement lightweight pure-Java codecs speaking `Zero` directly because `tenio-common` pulls Jackson/Guava/Log4j2. Never alter `ZeroType` codes or length widths without a coordinated protocol version bump.

## 3. Concurrency & Thread-Safety Invariants

- **`StringBuilderPool`**:
  - Synchronize both pool expansion in `get()` and reset in `cleanup()` on the **same monitor (`expansionLock`)** so concurrent `get()` and `cleanup()` cannot interleave.
  - Preserve explicit `borrowed` tracking (`repay(...)` rejects null or unborrowed instances with `NullElementPoolException`) and `MAX_RETAINED_CAPACITY` (`8 KiB`) eviction to prevent oversized buffers from pinning heap.
- **`TaskManagerImpl`**:
  - Preserve `lifecycleLock` + `lifecycleVersion` checks across `create`, `kill`, and `clear` so a task started concurrently with `kill`/`clear` is immediately shut down instead of leaked.
  - Null-guard `task.getScheduler()` in read methods like `getRemainTime(String id)` because a task may shut down concurrently and clear its scheduler reference.
- **`WorkerPool`**:
  - Keep bounded `ArrayBlockingQueue`, daemon threads, `AbortPolicy`, and `UncaughtExceptionHandler` error logging intact.

## 4. Security, Logging & Utility Conventions

- **`XmlUtility`**: Preserve all XXE protections (`FEATURE_SECURE_PROCESSING`, `disallow-doctype-decl`, disabled external general/parameter entities and external DTD loading, `ACCESS_EXTERNAL_DTD = ""`, `ACCESS_EXTERNAL_SCHEMA = ""`, `XIncludeAware = false`, `ExpandEntityReferences = false`).
- **Utility Classes**: Declare as `public final class` with a `private` constructor throwing `UnsupportedOperationException` (verified via reflection in `*UtilityTest`).
- **Logging**: Extend `SystemLogger`, guard log calls with `isTraceEnabled()` / `isDebugEnabled()` / `isInfoEnabled()` / `isErrorEnabled()`, and use `buildgen(...)` for multi-part log messages.

## 5. Git & Pull Request Workflow

- Use Conventional Commits: `type(scope): message` (`feat`, `fix`, `perf`, `refactor`, `test`, `docs`, `chore`, `ci`, `meta`).
- Stage only intended tracked files — do not accidentally commit local untracked files (e.g., `AGENTS.md`).
- When opening a PR without direct push access to upstream via `gh` CLI:
  ```sh
  git checkout -b <branch-name>
  gh repo fork --remote --remote-name fork --default-branch-only=false
  git push -u fork <branch-name>
  gh pr create --repo congcoi123/tenio-common --base <target-branch> --head <user>:<branch-name>
  ```
