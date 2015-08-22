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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

/**
 * The subtitle loader unit tests.
 *
 * @author david
 */
@RunWith(Parameterized.class)
public final class ApplicationTest {

  // the default stderr
  private static final PrintStream DEFAULT_STDERR = System.err;
  // the default stdout
  private static final PrintStream DEFAULT_STDOUT = System.out;

  // the output stream of our overridden stderr
  private static final ByteArrayOutputStream ERR = new ByteArrayOutputStream();

  // the output stream of our overridden stdout
  private static final ByteArrayOutputStream OUT = new ByteArrayOutputStream();

  // ----------------
  // test cases
  // ----------------

  /**
   * Builds and return the test cases
   *
   * @return the test cases
   */
  @Parameterized.Parameters
  public static Collection<Object[]> buildTestCases() {
    return Arrays.asList(new Object[][]{
        {
            new String[0],
            Optional.of(Application.USAGE.replaceAll("%n", lineSeparator())),
            Optional.empty()
        },
        {
            new String[1],
            Optional.of(Application.USAGE.replaceAll("%n", lineSeparator())),
            Optional.empty()
        },
        {
            new String[]{"PT1m", ApplicationTest.class.getResource("/1entry.srt").getPath()},
            Optional.empty(),
            Optional.of("1\n00:05:05,704 --> 00:05:07,039\nSe me dás licença, sobrinho,\n\n")
        }
    });
  }

  // ----------------
  // test parameters
  // ----------------

  @Parameterized.Parameter(0)
  public String[] input;

  @Parameterized.Parameter(1)
  public Optional<String> expectedError;

  @Parameterized.Parameter(2)
  public Optional<String> expectedOutput;

  @BeforeClass
  public static void setupStreams() {
    System.setOut(new PrintStream(OUT));
    System.setErr(new PrintStream(ERR));
  }

  @AfterClass
  public static void cleanStreams() throws IOException {
    try {
      OUT.close();
      ERR.close();
    } finally {
      System.setOut(DEFAULT_STDOUT);
      System.setErr(DEFAULT_STDERR);
    }
  }

  @After
  public void cleanFailureTests() throws IOException {
    OUT.reset();
    ERR.reset();
  }

  @Test
  public void testApplication() {
    Application.main(input);
    expectedError.ifPresent(expected -> assertEquals(expected, ERR.toString()));
    expectedOutput.ifPresent(expected -> assertEquals(expected, OUT.toString()));
  }
}
