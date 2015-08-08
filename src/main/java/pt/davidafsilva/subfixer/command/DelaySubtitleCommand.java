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

import java.time.temporal.ChronoUnit;
import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import pt.davidafsilva.subfixer.load.SubtitleEntry;
import pt.davidafsilva.subfixer.load.SubtitleLoader;

/**
 * This command applies a specified delay to the subtitle entries
 *
 * @author david
 */
public final class DelaySubtitleCommand implements Function<List<SubtitleEntry>, List<SubtitleEntry>> {

  // the valid temporal units
  private static final Map<String, ChronoUnit> VALID_UNITS;
  static {
    VALID_UNITS = Collections.unmodifiableMap(Arrays.stream(ChronoUnit.values())
          .filter(ChronoUnit::isDurationEstimated)
          .collect(Collectors.toMap(ChronoUnit::name, u -> u)));
  }

  // the invalid unit exception function
  private static final Function<String, ChronoUnit> INVALID_UNIT_EXCEPTION = u -> {
    throw new CommandExecutionException(u + " is an invalid unit. Valid units are: " + VALID_UNITS);
  };

  // the entry transformation function
  final BiFunction<SubtitleEntry, Duration, SubtitleEntry> ENTRY_TRANSFORMATION =
    (e, d) -> e.setTimeFrame(e.getStartTime().plus(d), e.getEndTime().plus(d));

  // "raw" properties
  private final String delay;
  private final String unit;

  /**
   * Constructs the delay command with the specified delay and unit
   *
   * @param delay the delay to be applied. May be negative if prefixed with a minus (-) sign
   * @param unit  the delay time unit
   */
  public DelaySubtitleCommand(final String delay, final String unit) {
    this.delay = delay;
    this.unit = unit;
  }

  @Override
  public List<SubtitleEntry> apply(final List<SubtitleEntry> entries) {
    // create the duration with the delay
    final Duration duration = Duration.of(convertDelay(delay), convertUnit(unit));

    // apply the delay
    return Collections.unmodifiableList(entries.stream()
      .map(entry -> ENTRY_TRANSFORMATION.apply(entry, duration))
      .collect(Collectors.toList()));
  }

  /**
   * Converts the specified delay into a valid numeric value typed delay.
   *
   * @param delay the raw user specified delay
   * @return the numeric delay
   * @throws CommandExecutionException if the specified delay is invalid
   */
  private long convertDelay(final String delay) {
    try {
      return Long.parseLong(delay);
    } catch (final NumberFormatException e) {
      throw new CommandExecutionException(delay + " is an invalid delay");
    }
  }

  /**
   * Converts the specified unit into a valid ChronoUnit.
   *
   * @param unit the raw user specified unit
   * @return the ChronoUnit
   * @throws CommandExecutionException if the specified unit is invalid
   */
  private ChronoUnit convertUnit(final String unit) {
    return VALID_UNITS.computeIfAbsent(delay, INVALID_UNIT_EXCEPTION);
  }
 }
