/*
The MIT License

Copyright (c) 2016-2026 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.tenio.common.logger;

import org.apache.logging.log4j.core.config.Configurator;

import java.net.URL;

/**
 * Logger Bootstrapper.
 *
 * @since 0.6.10
 */
public final class LoggerBootstrap {

    public static void initialize() {
        // 1. Respect system property (highest priority)
        if (System.getProperty("log4j.configurationFile") != null) {
            return;
        }

        // 2. Check if application provides its own config
        if (hasUserLog4jConfig()) {
            return;
        }

        // 3. Fallback to framework default
        URL url = LoggerBootstrap.class
                .getClassLoader()
                .getResource("log4j2.tenio.xml");

        if (url != null) {
            Configurator.initialize(null, url.toString());
            System.out.println("[Log4j] Using framework default configuration: " + url);
        } else {
            System.err.println("[Log4j] No default configuration found!");
        }
    }

    private static boolean hasUserLog4jConfig() {
        try {
            return Thread.currentThread()
                    .getContextClassLoader()
                    .getResources("log4j2.xml")
                    .hasMoreElements();
        } catch (Exception e) {
            return false;
        }
    }
}
