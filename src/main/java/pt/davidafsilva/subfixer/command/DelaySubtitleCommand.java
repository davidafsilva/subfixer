package pt.davidafsilva.subfixer.command;

/*
* #%L
 * * subtitle-fixer
 * *
 * %%
 * Copyright (C) 2015 David Silva
 * *
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

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import pt.davidafsilva.subfixer.load.SubtitleEntry;
import pt.davidafsilva.subfixer.load.SubtitleLoader;

import static pt.davidafsilva.subfixer.config.Configuration.LOGGER_NAME;

/**
 * This command applies a specified delay to the subtitle entries
 *
 * @author david
 */
public final class DelaySubtitleCommand implements Function<List<SubtitleEntry>, List<SubtitleEntry>> {

  // the logger
  private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

  // the entry transformation function
  private static final BiFunction<SubtitleEntry, Duration, SubtitleEntry> ENTRY_TRANSFORMATION =
    (e, d) -> e.setTimeFrame(e.getStartTime().plus(d), e.getEndTime().plus(d));

  // "raw" properties
  private final String delay;

  /**
   * Constructs the delay command with the specified delay pattern
   *
   * @param delay the delay pattern to be applied.
   */
  public DelaySubtitleCommand(final String delay) {
    this.delay = delay;
  }

  @Override
  public List<SubtitleEntry> apply(final List<SubtitleEntry> entries) {
    // create the duration with the delay
    final Duration duration = convertDelay(delay);

    // apply the delay
    return Collections.unmodifiableList(entries.stream()
      .map(entry -> ENTRY_TRANSFORMATION.apply(entry, duration))
      .collect(Collectors.toList()));
  }

  /**
   * Converts the specified delay pattern into a valid duration object.
   *
   * @param delay the raw user specified delay pattern
   * @return the duration
   * @throws CommandExecutionException if the specified delay is invalid
   */
  private Duration convertDelay(final String delay) {
    try {
      return Duration.parse(delay);
    } catch (final DateTimeParseException e) {
      final RuntimeException ce = new CommandExecutionException(delay +
                                " is an invalid delay pattern");
      LOGGER.log(Level.SEVERE, "invalid delay pattern", ce);
      throw ce;
    }
  }
 }
