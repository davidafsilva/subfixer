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

/**
 * The entry point for the subtitle-fixer utility
 * @author david
 */
public final class Application {

  /**
   * The main method, called from the command line
   * @param args The command line arguments
   */
  public static void main(final String[] args) {
    // input validation
    if (args.length != 3 && args.length != 4) {
      System.err.println("missing arguments: \n" +
        "<subtitle file> - the input file\n" +
        "    <[+|-]time> - the positive or negative delta of time to apply\n" +
        "    <time unit> - the time unit (one of: MS|S|M)\n" +
        "     [out file] - optional output file, otherwie outputs to STDOUT");
      System.exit(1);
    }

    // try {
    //   SubtitleLoader.load(args[0]).stream().forEach(System.out::println);
    // } catch (final Exception e) {
    //   System.err.println("an error occurred: " + e.getLocalizedMessage());
    // }
  }
}
