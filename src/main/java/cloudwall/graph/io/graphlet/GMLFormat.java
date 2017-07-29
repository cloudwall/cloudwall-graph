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
package cloudwall.graph.io.graphlet;

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;
import cloudwall.graph.io.ReaderInput;
import org.javafp.parsecj.Reply;

import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.*;
import java.util.function.Consumer;

/**
 * Graph Modeling Language (GML) was developed as part of the now-defunct Graphlet editor project. It is still used for
 * distribution of graph data in some academic settings.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="http://www.fim.uni-passau.de/en/faculty/former-professors/theoretische-informatik/projects/">Graphlet GML homepage</a>
 */
public class GMLFormat implements GraphFormat<GMLModel> {
    private static final MimeType[] CONTENT_TYPES;
    static {
        try {
            CONTENT_TYPES = new MimeType[] { new MimeType("application/x-graphlet") };
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MimeType[] getSupportedContentTypes() {
        return CONTENT_TYPES;
    }

    @Override
    public void read(DataSource dataIn, Consumer<GMLModel> modelConsumer) throws GraphFormatException, IOException {
        try (
                InputStream in = dataIn.getInputStream();
                Reader r = new InputStreamReader(in)
            )
        {
            ReaderInput parserInput = new ReaderInput(r);
            Reply<Character, GMLModel> result = GMLParser.newInstance().parse(parserInput.toInput());
            try {
                GMLModel model = result.getResult();
                modelConsumer.accept(model);
            } catch (Exception e) {
                throw new IOException(e);
            }

        }
    }

    @Override
    public void write(DataSource dataOut, GMLModel model) throws IOException {
        try (
                OutputStream out = dataOut.getOutputStream();
                PrintWriter pw = new PrintWriter(out)
            )
        {
            GMLModel.List root = model.getRoot();
            writeList(pw, root);
        }

    }

    private void writeList(PrintWriter pw, GMLModel.List list) {
        list.forEach(entry -> {
            pw.write(" ");
            pw.write(entry.getKey());
            pw.write(" ");

            GMLModel.Value value = entry.getValue();
            Class<?> type = value.getType();

            if (value instanceof GMLModel.Scalar) {
                Object scalar = value.getValue();
                if (String.class == type) {
                    pw.write('"');
                    pw.write(scalar == null ? "" : scalar.toString());
                    pw.write('"');
                } else {
                    pw.write(scalar == null ? "0" : String.valueOf(scalar));
                }
            } else if (value instanceof GMLModel.List) {
                pw.write("[");
                writeList(pw, (GMLModel.List) value);
                pw.write("]");
            }
        });

    }

}
