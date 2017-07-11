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
package cloudwall.graph.io.text;

import cloudwall.graph.Graph;
import cloudwall.graph.HeavyweightDirectedEdge;
import cloudwall.graph.LightweightVertex;
import cloudwall.graph.analysis.ConnectedComponentCollector;
import org.junit.Test;

import javax.activation.DataSource;
import javax.activation.URLDataSource;
import java.net.URL;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class TextFormatTest {
    @Test
    public void loadEuEmailCoreGraph() throws Exception {
        TextFormat format = new TextFormat();

        URL resource = getClass().getResource("email-Eu-core.txt");
        DataSource graphIn = new URLDataSource(resource);
        format.read(graphIn, model -> {
            Graph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> graph = model.compile();
            Collection<ConnectedComponentCollector.Component> components = graph.apply(new ConnectedComponentCollector<>());
            components.forEach(System.out::println);

            assertEquals(25571, graph.getEdgeCount());
            assertEquals(1005, graph.getVertexCount());
        });
    }

}