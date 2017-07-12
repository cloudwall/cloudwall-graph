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
 * Format used by the Stanford Network Analysis Project (SNAP), a large collection of graph data online. All their files
 * are simple edge files with lists of edges, and comments with a # prefix at the top. It is very similar to TGF, the
 * Trivial Graph Format, which this library also supports.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="https://snap.stanford.edu/index.html">SNAP homepage</a>
 * @see TrivialGraphFormat
 */
public class EdgeListFormat implements GraphFormat<EdgeListModel> {
    private static final MimeType[] CONTENT_TYPES;

    static {
        try {
            CONTENT_TYPES = new MimeType[]{new MimeType("text/plain")};
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }

    private int skipLines = 0;

    @Override
    public MimeType[] getSupportedContentTypes() {
        return CONTENT_TYPES;
    }

    @Override
    public void read(DataSource dataIn, Consumer<EdgeListModel> modelConsumer) throws GraphFormatException, IOException {
        EdgeListModel model = new EdgeListModel();
        try (LineNumberReader r = new LineNumberReader(new InputStreamReader(dataIn.getInputStream()))) {
            for (int i = 0; i < skipLines; i++) {
                // some formats have # nodes / edges as first two lines
                r.readLine();
            }

            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                model.parseAndAddEdge(line, r.getLineNumber());
            }
        }

        modelConsumer.accept(model);
    }

    @Override
    public void write(DataSource dataOut, EdgeListModel model) throws IOException {
        model.write(dataOut);
    }

    @SuppressWarnings("SameParameterValue")
    void setSkipLines(int skipLines) {
        this.skipLines = skipLines;
    }
}
