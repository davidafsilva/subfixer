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
import java.time.format.DateTimeParseException;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pt.davidafsilva.subfixer.config.Configuration.DATE_TIME_FORMAT;
import static pt.davidafsilva.subfixer.config.Configuration.LOGGER_NAME;

/**
 * This consumer validates and sets the time frame of the subtitle entry
 * being loaded at the entry load context.
 *
 * @author david
 */
final class EntryTimeFrameLineConsumer implements BiConsumer<LoadContext, String> {

  // the logger
  private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

  // the time frame separator
  private static final String TIME_FRAME_SEPARATOR = " --> ";

  @Override
  public void accept(final LoadContext loadContext, final String line) {
    final String[] times = line.split(TIME_FRAME_SEPARATOR);
    if (times.length != 2) {
      final RuntimeException e = new IllegalStateException("unable to load subtitle file: " +
          "expected a time frame with the format <start>" +
          TIME_FRAME_SEPARATOR + "<end>, got " + line);
      LOGGER.log(Level.SEVERE, "invalid subtitle entry time frame", e);
      throw e;
    }
    try {
      loadContext.getCurrentEntryLoadContext().setTimeFrame(
          LocalTime.parse(times[0].trim(), DATE_TIME_FORMAT),
          LocalTime.parse(times[1].trim(), DATE_TIME_FORMAT)
      );
    } catch (final DateTimeParseException e) {
      final RuntimeException e2 = new IllegalStateException(String.format(
          "unable to load subtitle file: invalid time format for entry %d",
          loadContext.getLoadedEntries().size() + 1), e);
      LOGGER.log(Level.SEVERE, "invalid subtitle entry time frame", e2);
      throw e2;
    }

    // jump to the next state
    loadContext.getCurrentEntryLoadContext().nextState();
  }
}
