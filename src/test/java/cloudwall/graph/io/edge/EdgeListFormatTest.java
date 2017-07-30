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
import cloudwall.graph.analysis.ConnectedComponentCollector;
import cloudwall.graph.io.StringDataSource;
import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class EdgeListFormatTest {
    @Test
    public void loadEuEmailCoreGraph() throws Exception {
        EdgeListFormat format = new EdgeListFormat();

        URL resource = getClass().getResource("email-Eu-core.txt");
        DataSource graphIn = new URLDataSource(resource);
        format.read(graphIn, model -> {
            GraphBuilder builder = new GraphBuilder();
            Graph<?, ?> graph = builder.build(model);
            assertEquals(25571, graph.getEdgeCount());
            assertEquals(1005, graph.getVertexCount());
        });
    }

    // from http://algs4.cs.princeton.edu/42digraph/
    @Test
    public void loadTinyDirectedGraph() throws Exception {
        EdgeListFormat format = new EdgeListFormat();
        format.setSkipLines(2);

        URL resource = getClass().getResource("tinyDG.txt");
        DataSource graphIn = new URLDataSource(resource);
        format.read(graphIn, model -> {
            GraphBuilder builder = new GraphBuilder();
            Graph<?, ?> graph = builder.build(model);
            Collection<ConnectedComponentCollector.Component> components = graph.apply(new ConnectedComponentCollector<>());
            components.forEach(System.out::println);
        });
    }

    // from http://algs4.cs.princeton.edu/42digraph/
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    @Test
    public void loadTinyDAG() throws Exception {
        EdgeListFormat format = new EdgeListFormat();
        format.setSkipLines(2);

        URL resource = getClass().getResource("tinyDAG.txt");
        DataSource graphIn = new URLDataSource(resource);
        format.read(graphIn, model -> {
            GraphBuilder builder = new GraphBuilder();
            Graph<Vertex, LightweightEdge> graph = (Graph<Vertex, LightweightEdge>) builder.build(model);
            graph.visitDepthFirstFrom(graph.getVertex(0L), System.out::println);

            StringDataSource dataOut = new StringDataSource();
            try {
                format.write(dataOut, model);
                System.out.println(dataOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}