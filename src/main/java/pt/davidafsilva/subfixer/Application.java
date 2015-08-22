package pt.davidafsilva.subfixer;

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

import java.util.logging.Level;
import java.util.logging.Logger;

import pt.davidafsilva.subfixer.command.CommandExecutionException;
import pt.davidafsilva.subfixer.command.CommandExecutor;
import pt.davidafsilva.subfixer.command.DelaySubtitleCommand;
import pt.davidafsilva.subfixer.command.LoadSubtitleEntriesCommand;
import pt.davidafsilva.subfixer.command.PrintSubtitleEntriesCommand;

import static pt.davidafsilva.subfixer.config.Configuration.LOGGER_NAME;

/**
 * The entry point for the subtitle-fixer utility
 *
 * @author david
 */
public final class Application {

  // the logger
  private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

  // the usage help message
  static final String USAGE =
      "incorrect usage - required arguments are: <delay pattern> <input file>%n" +
          " Examples of valid delay patterns are:%n" +
          "  1. PT20.345S = 20.345 seconds%n" +
          "  2. PT15M     = 15 minutes%n" +
          "  3. PT10H     = 10 hours%n" +
          "  4. PT-6H3M   = -6 hours and +3 minutes%n" +
          "  5. -PT6H3M   = -6 hours and -3 minutes%n" +
          " Please refer to the ISO-8601 standard for more information.%n";

  // input indices
  private static final int DELAY_INDEX = 0;
  private static final int INPUT_FILE_INDEX = 1;

  /**
   * The main method, called from the command line
   *
   * @param args The command line arguments
   */
  public static void main(final String[] args) {
    // input validation
    if (args.length != 2) {
      System.err.printf(USAGE);
      return;
    }

    // extract the input from args
    final String delay = args[DELAY_INDEX];
    final String inputFile = args[INPUT_FILE_INDEX];

    try {
      // chain and execute the commands
      CommandExecutor.getInstance().execute(
          new LoadSubtitleEntriesCommand().andThen(
              new DelaySubtitleCommand(delay).andThen(
                  new PrintSubtitleEntriesCommand(System.out)
              )
          ), inputFile);
    } catch (final CommandExecutionException e) {
      System.err.printf("error while executing command: %s%n", e.getLocalizedMessage());
    } catch (final Exception e) {
      System.err.printf("an unexpected error has landed:%n\tcause: %s%n\tmessage: %s%n",
          e.getClass().getSimpleName(), e.getLocalizedMessage());
      LOGGER.log(Level.SEVERE, "an unexpected error has landed", e);
    }
  }
}
