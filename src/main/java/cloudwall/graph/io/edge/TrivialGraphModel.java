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

import cloudwall.graph.GraphModel;
import cloudwall.graph.GraphVisitor;
import org.jooq.lambda.tuple.Tuple2;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple data model for storing node and edge information in Trivial Graph Format (TGF).
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see TrivialGraphFormat
 */
@SuppressWarnings("WeakerAccess")
public class TrivialGraphModel implements GraphModel {
    private final List<Tuple2<Long, String>> nodes = new ArrayList<>();
    private final EdgeListModel edgeListModel;

    public TrivialGraphModel() {
        edgeListModel = new EdgeListModel() {
            @Override
            protected void writeEdges(Writer w) throws IOException {
                for (Tuple2<Long,String> node : nodes) {
                    w.write(String.valueOf(node.v1()));
                    if (node.v2() != null){
                        w.write(" ");
                        w.write(node.v2());
                    }
                    w.write("\n");
                }
                w.write("#\n");
                super.writeEdges(w);
            }
        };
    }

    @Override
    public void visit(GraphVisitor visitor) {
        edgeListModel.visit(visitor);
    }

    @Override
    public long getVertexCount() {
        return edgeListModel.getVertexCount();
    }

    @Override
    public long getEdgeCount() {
        return edgeListModel.getEdgeCount();
    }

    void parseAndAddEdge(String line, int lineNumber) throws IOException {
        edgeListModel.parseAndAddEdge(line, lineNumber);
    }

    void parseAndAddVertex(String line, int lineNumber) throws IOException {
        int ndxFirstSpace = line.indexOf(" ");

        String vidTxt;
        String label = null;
        if (ndxFirstSpace == -1) {
            vidTxt = line;
        } else {
            vidTxt = line.substring(0, ndxFirstSpace);
            label = line.substring(ndxFirstSpace + 1);
        }

        try {
            addNode(Long.parseLong(vidTxt), label);
        } catch (NumberFormatException e) {
            throw new IOException("invalid node ID in node list at line # " + lineNumber + ": " + line);
        }
    }

    void addNode(long vid, String label) {
        nodes.add(new Tuple2<>(vid, label));
    }

    void write(DataSource dataOut) throws IOException {
        edgeListModel.write(dataOut);
    }
}
