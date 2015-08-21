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
 * This consumer terminates the entry being loaded and initiates a new load
 * step.
 *
 * @author david
 */
final class EntryFinalLineConsumer implements BiConsumer<LoadContext, String> {

  @Override
  public void accept(final LoadContext loadContext, final String line) {
    switch (loadContext.getCurrentEntryLoadContext().getCurrentLoadState()) {
      case FINAL:
        // create the subtitle entry
        final SubtitleEntry entry = new SubtitleEntry(
            loadContext.getCurrentEntryLoadContext().getStartTime(),
            loadContext.getCurrentEntryLoadContext().getEndTime(),
            loadContext.getCurrentEntryLoadContext().getText()
        );

        // add it to the context
        loadContext.addSubtitleEntry(entry);

        // create a new entry load step
        loadContext.createEntryLoadContext();
        break;
      default:
        break;
    }
  }
}
