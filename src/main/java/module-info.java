/**
 * Exported packages for the tenio.common module.
 */
open module tenio.common {
  requires jsr305;
  requires org.reflections;
  requires com.google.common;
  requires org.apache.logging.log4j;
  requires java.xml;
  requires java.desktop;

  exports com.tenio.common.bootstrap.annotation;
  exports com.tenio.common.bootstrap.injector to tenio.core;
  exports com.tenio.common.configuration;
  exports com.tenio.common.constant;
  exports com.tenio.common.data;
  exports com.tenio.common.data.common;
  exports com.tenio.common.data.utility;
  exports com.tenio.common.exception;
  exports com.tenio.common.logger;
  exports com.tenio.common.pool;
  exports com.tenio.common.task;
  exports com.tenio.common.task.implement to tenio.core;
  exports com.tenio.common.utility;
  exports com.tenio.common.worker;
}
