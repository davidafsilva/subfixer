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

import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.List;
import pt.davidafsilva.subfixer.load.SubtitleEntry;

//import static pt.davidafsilva.subfixer.config.Configuration.CHARSET;

/**
 * This command prints the subtitle entries to a designated output stream.
 *
 * @author david
 */
public final class PrintSubtitleEntriesCommand
                implements Function<List<SubtitleEntry>, List<SubtitleEntry>> {

  // the output format
  private static final String ENTRY_FORMAT = "%d%n%s --> %s%n%s%n%n";

  // the output stream
  private final OutputStream out;

  /**
   * Default print command constructor, it accepts the target output stream.
   *
   * @param out the output stream
   */
  public PrintSubtitleEntriesCommand(final OutputStream out) {
    this.out = out;
  }

  @Override
  public List<SubtitleEntry> apply(final List<SubtitleEntry> entries) {
    // print the entries
    IntStream.range(0, entries.size())
             .mapToObj(idx -> {
               final SubtitleEntry entry = entries.get(idx);
               return String.format(ENTRY_FORMAT, idx+1,
                                    entry.getStartTime().toString(),
                                    entry.getEndTime().toString(),
                                    entry.getText());
             })
             .map(str -> str.getBytes(StandardCharsets.UTF_8))
             .forEach(this::write);
    // return them as is
    return entries;
  }

  /**
   * Writes the given chunck of raw entry data to the configured output stream
   *
   * @param entry the raw byte data
   */
  private void write(final byte[] entry) {
    try {
      out.write(entry);
    } catch (final IOException e) {
      throw new CommandExecutionException("unable to write entry data to output"
                                          + " stream.", e);
    }
  }
}
