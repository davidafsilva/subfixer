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
import java.util.logging.Logger;
import java.util.logging.Level;

import static pt.davidafsilva.subfixer.config.Configuration.LOGGER_NAME;

/**
 * The entry load context that is used by the loader to keep entry loading
 * state across the different steps.
 *
 * @author david
 */
final class EntryLoadContext {

  // the logger
  private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

  // the current entry load state
  private EntryLoadState currentLoadState;
  // the entry start time
  private LocalTime startTime;
  // the entry end time
  private LocalTime endTime;
  // the actual subtitle text of this entry
  private StringBuilder text = new StringBuilder();

  /**
   * Creates a entry load context with the specified initial state.
   *
   * @param initialState the initial load state
   */
  EntryLoadContext(final EntryLoadState initialState) {
    this.currentLoadState = Objects.requireNonNull(initialState, "initialState");
  }

  /**
   * Returns the current load state of this context
   *
   * @return the current entry load state
   */
  EntryLoadState getCurrentLoadState() { return currentLoadState; }

  /**
   * Returns the start time, if any was set so far, otherwise {@code null} is returned
   *
   * @return entry start time
   */
  LocalTime getStartTime() { return startTime; }

  /**
   * Returns the end time, if any was set so far, otherwise {@code null} is returned
   *
   * @return entry end time
   */
  LocalTime getEndTime() { return endTime; }

  /**
   * Returns the entry text, if any was set so far, otherwise an empty string is
   * returned
   *
   * @return entry text
   */
  String getText() { return text.toString(); }

  /**
   * Transitions to the next state
   */
  void nextState() {
    final EntryLoadState[] possibleStates = EntryLoadState.values();
    if (currentLoadState.ordinal()+1 == possibleStates.length) {
      final RuntimeException e = new IllegalStateException(
              "unable to progress to the next state, already at " + currentLoadState);
      LOGGER.log(Level.SEVERE, "invalid state transition", e);
      throw e;
    }
    this.currentLoadState = EntryLoadState.values()[currentLoadState.ordinal()+1];
  }

  /**
   * Sets the time frame for this subtitle entry being loaded
   *
   * @param startTime the start time of the entry
   * @param endTime   the end time of the entry
   */
  void setTimeFrame(final LocalTime startTime, final LocalTime endTime) {
    this.startTime = Objects.requireNonNull(startTime, "startTime");
    this.endTime = Objects.requireNonNull(endTime, "endTime");
  }

  /**
   * Appends the specified text to the subtitle entry being loaded
   *
   * @param text the subtitle text to be added
   */
  void appendText(final String text) {
    if (this.text.length() > 0) {
      this.text.append(System.lineSeparator());
    }
    this.text.append(Objects.requireNonNull(text, "text"));
  }

  // the possible states for the entry loader to flow
  enum EntryLoadState {
    INITIAL, TIME_FRAME, TEXT, FINAL
  }
}
