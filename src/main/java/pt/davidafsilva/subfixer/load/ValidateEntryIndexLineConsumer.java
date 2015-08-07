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

import java.util.function.BiConsumer;

/**
 * This consumer validates the first line of the entry being loaded as it shall
 * contain the entry index (1..N).
 *
 * @author david
 */
final class ValidateEntryIndexLineConsumer implements BiConsumer<LoadContext, String> {

  @Override
  public void accept(final LoadContext loadContext, final String line) {
    if (line.isEmpty()) {
      // there's nothing to do here, just a blank line.. we hope
      return;
    }

    // first entry line, let's check if we have our entry index
    try {
      if (loadContext.getLoadedEntries().size()+1 != Integer.parseInt(line)) {
        throw new IllegalStateException("unable to load subtitle file: " +
                   "expected entry " + loadContext.getLoadedEntries().size()+1 +
                    ", got " + line);
      }

      // jump to the next state
      loadContext.getCurrentEntryLoadContext().nextState();
    } catch (final NumberFormatException e) {
      // houston, we've a problem?
      throw new IllegalStateException("unable to load subtitle file: " +
                 "expected a positive (non-decimal) numeric value, got " +
                 line);
    }
  }

}
