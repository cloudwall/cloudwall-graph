/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cloudwall.graph.io;

import cloudwall.graph.GraphModel;

import javax.activation.DataSource;
import javax.activation.MimeType;
import java.io.IOException;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * A supported graph serialization format. If you want to know every supported content type you can use
 * {@link Provider#getSupportedFormats()} and {@link #getSupportedContentTypes()} like so:
 *
 * <pre>
 * Set<MimeType> allSupportedTypes = new HashSet<>();
 * for (GraphFormat format : GraphFormat.Provider.getSupportedFormats() {
 *     Iterables.addAll(allSupportedTypes, format.getSupportedContentTypes());
 * }
 * </pre>
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface GraphFormat<M extends GraphModel> {
    /**
     * Helper class that discovers all registered formats under META-INF/services in the JAR file.
     */
    class Provider {
        private static final ServiceLoader<GraphFormat> FORMAT_LOADER = ServiceLoader.load(GraphFormat.class);

        static Iterable<GraphFormat> getSupportedFormats() {
            return FORMAT_LOADER;
        }
    }

    /**
     * Gets the content types that this particular format supports, e.g. text/vnd.graphviz for Graphviz DOT.
     */
    MimeType[] getSupportedContentTypes();

    /**
     * Reads a graph a returns it to the given consumer when complete.
     */
    void read(DataSource dataIn, Consumer<M> modelConsumer) throws GraphFormatException, IOException;

    /**
     * Writes the given graph to the output stream.
     */
    void write(DataSource dataOut, M model) throws IOException;

}
