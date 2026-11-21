# AGENTS.md

Guidance for AI coding agents working in `tenio-common`.

## Project Overview

- Artifact: `io.github.congcoi123:tenio-common` (Maven, Java 21+)
- Purpose: Common module for the [TenIO](https://github.com/congcoi123/tenio) multiplayer game server framework. Provides configuration loading, Log4j2-backed pooled logging, object pooling, scheduled task management, worker thread pools, utility helpers, and binary serialization (`Zero` custom format and `MessagePack`).

## Build, Test & Verification Commands

- Compile + Checkstyle + Source/Javadoc JARs:
  ```sh
  mvn compile
  ```
- Run Tests, JaCoCo Coverage Check & Checkstyle:
  ```sh
  mvn verify
  ```
- Full Local Install:
  ```sh
  mvn clean install
  ```

### Environment Notes

- The project targets Java 21 (`maven.compiler.source=21`, `maven.compiler.target=21`).
- Running `mvn verify` on much newer JDKs (e.g., JDK 27) with Mockito `5.14.1` can fail when inline-mocking JDK classes such as `java.util.UUID` or `org.w3c.dom.Node`. Use JDK 21 when running the full test suite.
- `maven-checkstyle-plugin` is bound to the `validate` phase with `failsOnError=true`, so formatting or style violations fail every build early.
- `jacoco-maven-plugin` enforces a minimum 80% line coverage (`COVEREDRATIO >= 0.80`) during `verify`.

## Architecture & Package Map

All production code lives under `src/main/java/com/tenio/common/`:

- `configuration/` — `Configuration`, `ConfigurationType`, and `CommonConfiguration` (XML-backed key/value configuration with Jackson pretty-printed `toString()`).
- `constant/` — `CommonConstant` (object pool sizing constants: `DEFAULT_NUMBER_ELEMENTS_POOL`, `ADDITIONAL_NUMBER_ELEMENTS_POOL`).
- `data/` — Serialization abstraction (`DataCollection`, `DataType` [`ZERO`, `MSG_PACK`], `DataUtility`, `ImmutableSnapshot`):
  - `data/common/` — `CommonArray`, `CommonMap`.
  - `data/msgpack/` — `MsgPackUtility` and `element/MsgPackMap`.
  - `data/zero/` — Custom compact binary protocol (`ZeroMap`, `ZeroArray`, `ZeroElement`, `ReadonlyZeroMap`, `ReadonlyZeroArray`, `ZeroType`, `utility/ZeroUtility`, and `implement/*`).
- `exception/` — `MsgPackOperationException`, `NullElementPoolException`, `RunningScheduledTaskException`, `UnsupportedMsgPackDataTypeException`.
- `logger/` — `AbstractLogger`, `SystemLogger`, `LoggerBootstrap`, and `pool/StringBuilderPool` (singleton `ElementPool<StringBuilder>`).
- `pool/` — `ElementPool<T>` generic object pool contract.
- `task/` — `Task`, `TaskManager`, and `implement/TaskManagerImpl` (thread-safe `ConcurrentHashMap` + lifecycle-versioned `ScheduledFuture` management).
- `utility/` — `ByteUtility`, `ClassLoaderUtility`, `MathUtility`, `OsUtility`, `StringUtility`, `TimeUtility`, `XmlUtility`.
- `worker/` — `WorkerPool` (bounded `ArrayBlockingQueue` `ThreadPoolExecutor` with daemon threads) and `WorkerPoolRunnable`.

Tests live under `src/test/java/com/tenio/common/` and are aggregated by `TenIOCommonTest` via `@SelectPackages`.

## Coding Conventions & Rules

1. Checkstyle (`checkstyle.xml`, Google Java Style):
   - Maximum line length: 100 characters (excluding `package`, `import`, and URLs).
   - No star imports (`AvoidStarImport`); one top-level class per file (`OneTopLevelClass`); no tabs (`FileTabCharacter`).
   - Always use braces on `if`, `else`, `for`, `while`, `do`.
2. License Header: Keep the MIT license header (`Copyright (c) 2016-2026 kong <congcoi123@gmail.com>`) at the top of all Java source and test files.
3. Javadoc: Document all public classes, interfaces, methods, enum constants, and public fields. Annotate newly added public APIs with `@since <version>`.
4. Logging:
   - Extend `SystemLogger` (which extends `AbstractLogger`) for classes that log.
   - Use `buildgen(...)` to obtain pooled `StringBuilder` instances when building multi-part log messages, and guard log calls with `isTraceEnabled()` / `isDebugEnabled()` / `isInfoEnabled()` / `isErrorEnabled()`.
5. Utility Classes: Declare as `public final class` with a `private` constructor throwing `UnsupportedOperationException`.
6. Defensive Bounds & Security:
   - Preserve input size, collection size, and nesting depth guards in `ZeroUtility` (`MAX_MESSAGE_BYTES`, `MAX_BINARY_BYTES`, `MAX_NESTING_DEPTH`) and `MsgPackUtility` (`MAX_INPUT_BYTES`, `MAX_COLLECTION_SIZE`, `MAX_NESTING_DEPTH`).
   - Preserve XXE protections (disallowed doctype, disabled external entities/DTDs) in `XmlUtility`.
7. Thread Safety:
   - `StringBuilderPool` relies on explicit `borrowed` tracking; any `StringBuilder` obtained from the pool must be returned via `repay(...)` exactly once.
   - `TaskManagerImpl` uses `lifecycleLock` + `lifecycleVersion` to prevent race conditions between `create`, `kill`, and `clear`; preserve these invariants when editing task lifecycle code.

## Git & Contribution Workflow

- Base feature/fix branches off `develop` (do not merge PRs directly into `master`).
- Follow [Conventional Commits](https://www.conventionalcommits.org): `type(scope): message` where `type` is one of `feat`, `fix`, `docs`, `meta`, `refactor`, `test`, `chore`, `ci`, `perf`.
- Before opening a PR, verify `mvn clean install` passes (Checkstyle, unit tests, and JaCoCo coverage).
