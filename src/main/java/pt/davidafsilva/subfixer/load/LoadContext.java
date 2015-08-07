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

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import pt.davidafsilva.subfixer.load.EntryLoadContext.EntryLoadState;

/**
 * The load context that is used by the loader steps.
 *
 * @author david
 */
final class LoadContext {

  // the loaded entries
  private final List<SubtitleEntry> entries = new ArrayList<>();

  // the current entry load context
  private EntryLoadContext currentEntryLoadContext;

  /**
   * Creates a load context
   */
  LoadContext() {}

  /**
   * Adds the specified subtitle entry to the list of loaded entries.
   *
   * @param entry the newly parsed entry
   */
  void addSubtitleEntry(final SubtitleEntry entry) {
    entries.add(entry);
  }

  /**
   * Returns all of the loaded entries, so far, wrapped around an unmodifiable
   * struture.
   *
   * @return the loaded entries
   */
  List<SubtitleEntry> getLoadedEntries() {
    return Collections.unmodifiableList(entries);
  }

  /**
   * Creates a new, empty entry load context and sets it (replacing the previous
   * one, if any was set) as the current entry load context.
   *
   * @return the entry load context
   */
  EntryLoadContext createEntryLoadContext() {
    return currentEntryLoadContext = new EntryLoadContext(EntryLoadState.INITIAL);
  }

  /**
   * Returns the current active entry load context
   *
   * @return the current entry load context
   */
  EntryLoadContext getCurrentEntryLoadContext() {
    return currentEntryLoadContext;
  }
}
