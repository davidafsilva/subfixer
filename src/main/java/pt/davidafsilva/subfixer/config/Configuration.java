package pt.davidafsilva.subfixer.config;

/*
 * #%L
 * subtitle-fixer
 * %%
 * Copyright (C) 2015 David Silva
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This configuration class contains all the necessary runtime configurations
 * for the application.
 *
 * @author david
 */
public final class Configuration {

  // the global logger namespace
  public static final String LOGGER_NAME = "pt.davidafsilva.subfixer";
  // the log level
  public static final Level LOGGER_LEVEL = Level.parse(System.getProperty("logLevel", "OFF"));
  // the charset to be used when loading and write data
  public static final Charset CHARSET = Charset.forName(System.getProperty("encoding", "UTF-8"));
  // the logger instance
  private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

  static {
    // default logging configuration
    LOGGER.setLevel(LOGGER_LEVEL);
  }

  // log loaded configuration
  static {
    LOGGER.info(String.format("{%n\tLogger: %s%n\tLog level: %s%n\tCharset: %s%n}",
        LOGGER_NAME, LOGGER_LEVEL, CHARSET));
  }

  // private constructor
  private Configuration() {}
}
