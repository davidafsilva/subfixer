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

 import java.io.BufferedReader;
 import java.nio.charset.StandardCharsets;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.nio.file.Path;
 import java.util.Collections;
 import java.util.List;
 import java.util.Map;
 import java.util.HashMap;
 import java.util.function.BiConsumer;
 import java.util.logging.Logger;
 import java.util.logging.Level;
 import pt.davidafsilva.subfixer.load.EntryLoadContext.EntryLoadState;

 import static pt.davidafsilva.subfixer.config.Configuration.LOGGER_NAME;

/**
 * The subtitle loaders, which takes an arbitrary subtitle file as input and
 * returns the associated {@link SubtitleEntry entries}.
 *
 * @author david
 */
public final class SubtitleLoader {

  // the logger
  private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

  // the entry states consumers
  private static final Map<EntryLoadState, BiConsumer<LoadContext, String>> STATE_COMSUMERS;
  static {
    final Map<EntryLoadState, BiConsumer<LoadContext, String>> consumers = new HashMap<>();
    consumers.put(EntryLoadState.INITIAL, new ValidateEntryIndexLineConsumer());
    consumers.put(EntryLoadState.TIME_FRAME, new EntryTimeFrameLineConsumer());
    consumers.put(EntryLoadState.TEXT, new EntryTextLineConsumer()
                              .andThen(new EntryFinalLineConsumer()));
    STATE_COMSUMERS = Collections.unmodifiableMap(consumers);
  }

  // the default NO-OP consumer (safe default)
  private static final BiConsumer<LoadContext, String> DEFAULT_CONSUMER = (c, s) -> {
    final RuntimeException e = new IllegalStateException("unsupported state");
    LOGGER.log(Level.SEVERE, "invalid state", e);
    throw e;
  };

  // private constructor
  private SubtitleLoader() {}

  /**
   * Loads the entries associated with the specified subtitle file
   *
   * @param subtitleFile the start time of the entry
   * @return the ordered list of subtitle entries
   * @throws IOException if an error occurs while reading the source file
   */
   public static List<SubtitleEntry> load(final String subtitleFile) throws IOException {
     // creates a new load context
     final LoadContext context = new LoadContext();

     // load the file
     final Path file = Paths.get(subtitleFile);
     try (final BufferedReader br = Files.newBufferedReader(file)) {
       // prepare for the first entry being loaded
       context.createEntryLoadContext();

       // read line by line
       String line;
       int number = 0;
       while ((line = br.readLine()) != null) {
         line = line.trim();
         number++;

         // log the line read
         LOGGER.info(String.format("line[%d]=%s", number, line));

         // apply the specific consumer
         consume(context, line);
       }

       // consume after we exit the loop in order to fill the last entry if not
       // processed, otherwise, nothing bad shall happen
       consume(context, "");
     } catch (final IOException e) {
       // log and rethrow
       LOGGER.log(Level.SEVERE, "error reading file", e);
       throw e;
     }

     // return the loaded entries
     return context.getLoadedEntries();
  }

  /**
   * Consumes the specified line of read text
   *
   * @param context the current load context
   * @param line    the line read
   */
  private static void consume(final LoadContext context, final String line) {
    STATE_COMSUMERS.getOrDefault(context.getCurrentEntryLoadContext()
        .getCurrentLoadState(), DEFAULT_CONSUMER)
        .accept(context, line);
  }
}
