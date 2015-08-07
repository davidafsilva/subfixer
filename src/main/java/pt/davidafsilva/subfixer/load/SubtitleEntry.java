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
import java.util.Objects;

/**
 * The subtitle entry model, which contains the necessary information
 * about an individual (time-framed) entry.
 * Each entry is immutable.
 *
 * @author david
 */
public final class SubtitleEntry {

  // properties
  private final LocalTime startTime;
  private final LocalTime endTime;
  private final String text;

  /**
   * The default constructor for the entry.
   *
   * @param startTime the start time of the entry
   * @param endTime   the end time of the entry
   * @param text      the entry text
   */
  SubtitleEntry(final LocalTime startTime, final LocalTime endTime,
                final String text) {
    this.startTime = Objects.requireNonNull(startTime, "startTime");
    this.endTime = Objects.requireNonNull(endTime, "endTime");
    this.text = Objects.requireNonNull(text, "text");
  }

  /**
   * Returns the start time of the entry
   * @return the start time
   */
  LocalTime getStartTime() { return startTime; }

  /**
   * Returns the end time of the entry
   * @return the end time
   */
  LocalTime getEndTime() { return endTime; }

  /**
   * Returns the text of the entry
   * @return the actual text
   */
  String getText() { return text; }

  /**
   * Returns a string representation of this entry
   *
   * @return the entry's string representation
   */
   @Override
   public String toString() {
     return "SubtitleEntry{" +
        "start: " + startTime +
        ", end: " + endTime +
        ", text: " + text +
        "}";
   }
}
