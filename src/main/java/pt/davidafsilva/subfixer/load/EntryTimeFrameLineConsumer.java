package pt.davidafsilva.subfixer.load;

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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.function.BiConsumer;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

/**
 * This consumer validates and sets the time frame of the subtitle entry
 * being loaded at the entry load context.
 *
 * @author david
 */
final class EntryTimeFrameLineConsumer implements BiConsumer<LoadContext, String> {

  // the time frame separator
  private static final String TIME_FRAME_SEPARATOR = " --> ";

  // the date time format for the entry timestamps
  private static final DateTimeFormatter DATE_TIME_FORMAT;
  static {
    DATE_TIME_FORMAT = new DateTimeFormatterBuilder()
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendLiteral(',')
                .appendFraction(NANO_OF_SECOND, 0, 3, false)
                .toFormatter();
  }

  @Override
  public void accept(final LoadContext loadContext, final String line) {
    final String[] times = line.split(TIME_FRAME_SEPARATOR);
    if (times.length != 2) {
      throw new IllegalStateException("unable to load subtitle file: " +
                 "expected a time frame with the format <start>" +
                 TIME_FRAME_SEPARATOR + "<end>, got " + line);
    }
    loadContext.getCurrentEntryLoadContext().setTimeFrame(
      LocalTime.parse(times[0].trim(), DATE_TIME_FORMAT),
      LocalTime.parse(times[1].trim(), DATE_TIME_FORMAT)
    );

    // jump to the next state
    loadContext.getCurrentEntryLoadContext().nextState();
  }
}
