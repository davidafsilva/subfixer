package pt.davidafsilva.subfixer.command;

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

import java.util.function.Function;

/**
* This command applies a specified delay to the subtitle entries
*
* @author david
*/
public final class CommandExecutor {

  // private constructor
  private CommandExecutor() {}

  // the singleton holder entity
  private static final class Holder {
    private static final CommandExecutor INSTANCE = new CommandExecutor();
  }

  /**
   * Returns the singleton instance of the command executor
   *
   * @return the command executor instance
   */
  public static CommandExecutor getInstance() {
    return Holder.INSTANCE;
  }

  /**
   * Executes and returns the command result.
   *
   * @param <I> the type of the input value
   * @param <R> the type of the command result
   * @param command the command to be executed
   * @param input   the command input
   * @return the command result
   */
  public <I, R> R execute(final Function<I, R> command, final I input)
    throws CommandExecutionException {
    return command.apply(input);
  }
}
