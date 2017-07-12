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

import cloudwall.graph.*;
import org.jooq.lambda.tuple.Tuple3;

import javax.activation.DataSource;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Ultra-simple data model for various edge formats that are just lists of edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see EdgeListFormat
 */
@SuppressWarnings("WeakerAccess")
public class EdgeListModel implements GraphModel {
    private final List<Tuple3<Long, Long, String>> edges = new ArrayList<>();

    void parseAndAddEdge(String line, int lineNumber) throws IOException {
        String[] lineParts = line.split("\\s+");
        if (lineParts.length < 2) {
            throw new IOException("invalid line at line # " + lineNumber + ": " + line);
        }
        String vid1Txt = lineParts[0];
        String vid2Txt = lineParts[1];
        String label;
        if (lineParts.length == 2) {
            label = null;
        } else {
            int ndxFirstSpace = line.indexOf(" ");
            int ndxSecondSpace = line.indexOf(" ", ndxFirstSpace + 1);
            label = line.substring(ndxSecondSpace + 1);
        }

        try {
            long vertex0 = Long.parseLong(vid1Txt);
            long vertex1 = Long.parseLong(vid2Txt);
            addEdge(vertex0, vertex1, label);
        } catch (NumberFormatException e) {
            throw new IOException("invalid node ID in edge list at line # " + lineNumber + ": " + line);
        }
    }

    void addEdge(long vid1, long vid2, String label) {
        edges.add(new Tuple3<>(vid1, vid2, label));
    }

    void write(DataSource dataOut) throws IOException {
        try (Writer w = new OutputStreamWriter(dataOut.getOutputStream())) {
            writeEdges(w);
        }
    }

    void writeEdges(Writer w) throws IOException {
        for (Tuple3<Long, Long, String> edge : edges) {
            w.write(String.valueOf(edge.v1()));
            w.write(" ");
            w.write(String.valueOf(edge.v2()));
            if (edge.v3() != null) {
                w.write(" ");
                w.write(edge.v3());
            }
            w.write("\n");
        }
    }

    Graph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> compile() {
        AdjacencyListGraph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> graph = new AdjacencyListGraph<>();
        for (Tuple3<Long, Long, String> edge : edges) {
            ensureNodeExists(graph, edge.v1());
            ensureNodeExists(graph, edge.v2());

            LightweightVertex vid1 = graph.getVertex(edge.v1());
            LightweightVertex vid2 = graph.getVertex(edge.v2());

            assert vid1 != null;
            assert vid2 != null;

            graph.addEdge(new HeavyweightDirectedEdge<>(vid1, vid2));
        }

        return graph;
    }

    private void ensureNodeExists(MutableGraph<LightweightVertex, ?> graph, long vid) {
        if (graph.getVertex(vid) == null) {
            graph.addVertex(new LightweightVertex(vid));
        }
    }
}
