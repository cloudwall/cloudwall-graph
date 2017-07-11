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
package cloudwall.graph.io.trivial;

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;

import javax.activation.DataSource;
import javax.activation.MimeType;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Reader for the Trivial Graph Format (TGF), a simple line-oriented representation for nodes and edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="https://en.wikipedia.org/wiki/Trivial_Graph_Format">Trivial Graph Format (Wikipedia)</a>
 */
public class TrivialGraphFormat implements GraphFormat<TrivialGraphModel> {
    @Override
    public Iterable<MimeType> getSupportedContentTypes() {
        return null;
    }

    @Override
    public void read(DataSource dataIn, Consumer<TrivialGraphModel> modelConsumer) throws GraphFormatException, IOException {

    }

    @Override
    public void write(DataSource dataOut, TrivialGraphModel model) {

    }
}
