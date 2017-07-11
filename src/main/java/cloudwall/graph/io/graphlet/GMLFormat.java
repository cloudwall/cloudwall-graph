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

import javax.activation.DataSource;
import javax.activation.MimeType;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Graph Modeling Language (GML) was developed as part of the now-defunct Graphlet editor project. It is still used for
 * distribution of graph data in some academic settings.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="http://www.fim.uni-passau.de/en/faculty/former-professors/theoretische-informatik/projects/">Graphlet GML homepage</a>
 * @see <a href="http://www.fim.uni-passau.de/fileadmin/files/lehrstuhl/brandenburg/projekte/gml/gml-technical-report.pdf">GML: A portable Graph File Format</a>
 */
public class GMLFormat implements GraphFormat<GMLModel> {
    @Override
    public Iterable<MimeType> getSupportedContentTypes() {
        return null;
    }

    @Override
    public void read(DataSource dataIn, Consumer<GMLModel> modelConsumer) throws GraphFormatException, IOException {

    }

    @Override
    public void write(DataSource dataOut, GMLModel model) {

    }

}
