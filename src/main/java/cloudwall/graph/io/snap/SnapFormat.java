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
package cloudwall.graph.io.snap;

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;
import cloudwall.graph.io.GraphInput;
import cloudwall.graph.io.GraphOutput;

import javax.activation.MimeType;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Format used by the Stanford Network Analysis Project (SNAP), a large collection of graph data online. All their files
 * are simple text files with lists of edges, and comments with a # prefix at the top. It is very similar to TGF, the
 * Trivial Graph Format, which this library also supports.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="https://snap.stanford.edu/index.html">SNAP homepage</a>
 * @see cloudwall.graph.io.trivial.TrivialGraphFormat
 */
public class SnapFormat implements GraphFormat<SnapModel> {
    @Override
    public Iterable<MimeType> getSupportedContentTypes() {
        return null;
    }

    @Override
    public void read(GraphInput in, Consumer<SnapModel> modelConsumer) throws GraphFormatException, IOException {

    }

    @Override
    public void write(GraphOutput out, SnapModel model) {

    }
}
