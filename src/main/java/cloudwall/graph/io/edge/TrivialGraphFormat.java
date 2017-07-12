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
package cloudwall.graph.io.edge;

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;

import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.function.Consumer;

/**
 * Reader for the Trivial Graph Format (TGF), a simple line-oriented representation for nodes and edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="https://en.wikipedia.org/wiki/Trivial_Graph_Format">Trivial Graph Format (Wikipedia)</a>
 */
public class TrivialGraphFormat implements GraphFormat<TrivialGraphModel> {
    private static final MimeType[] CONTENT_TYPES;
    static {
        try {
            CONTENT_TYPES = new MimeType[] { new MimeType("chemical/x-mdl-tgf") };
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MimeType[] getSupportedContentTypes() {
        return CONTENT_TYPES;
    }

    @Override
    public void read(DataSource dataIn, Consumer<TrivialGraphModel> modelConsumer) throws GraphFormatException, IOException {
        TrivialGraphModel model = new TrivialGraphModel();

        try (LineNumberReader r = new LineNumberReader(new InputStreamReader(dataIn.getInputStream()))) {
            boolean nodesDone = false;
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                } else if (line.equals("#")) {
                    nodesDone = true;
                    continue;
                }
                int lineNumber = r.getLineNumber();
                if (nodesDone) {
                    model.parseAndAddEdge(line, lineNumber);
                } else {
                    model.parseAndAddVertex(line, lineNumber);
                }
            }
        }

        modelConsumer.accept(model);
    }

    @Override
    public void write(DataSource dataOut, TrivialGraphModel model) throws IOException {
        model.write(dataOut);
    }
}
