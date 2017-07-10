/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cloudwall.graph.analysis;

import cloudwall.graph.*;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ConnectedComponentCollectorTest {
    @Test
    public void analyzeSimpleDAG() {
        aSimpleDAG().apply(new ConnectedComponentCollector<>()).forEach(c -> {
            System.out.println("#" + c.getComponentNumber() + " => " + c.getVertices());
        });    }

    @Test
    public void analyzeDualDAG() {
        aPairOfDirectedSubgraphs().apply(new ConnectedComponentCollector<>()).forEach(c -> {
            System.out.println("#" + c.getComponentNumber() + " => " + c.getVertices());
        });
    }

    @SuppressWarnings("ConstantConditions")
    private Graph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> aSimpleDAG() {
        MutableGraph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> graph  = new AdjacencyListGraph<>();
        graph.addVertex(new LightweightVertex("A"));
        graph.addVertex(new LightweightVertex("B"));
        graph.addVertex(new LightweightVertex("C"));

        graph.addEdge(new HeavyweightDirectedEdge<>(graph.getVertex("A"), graph.getVertex("B")));
        graph.addEdge(new HeavyweightDirectedEdge<>(graph.getVertex("A"), graph.getVertex("C")));

        return graph;
    }

    @SuppressWarnings("ConstantConditions")
    private Graph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> aPairOfDirectedSubgraphs() {
        MutableGraph<LightweightVertex, HeavyweightDirectedEdge<LightweightVertex>> graph  = new AdjacencyListGraph<>();
        graph.addVertex(new LightweightVertex("A"));
        graph.addVertex(new LightweightVertex("B"));
        graph.addVertex(new LightweightVertex("C"));
        graph.addVertex(new LightweightVertex("D"));
        graph.addVertex(new LightweightVertex("E"));

        graph.addEdge(new HeavyweightDirectedEdge<>(graph.getVertex("A"), graph.getVertex("B")));
        graph.addEdge(new HeavyweightDirectedEdge<>(graph.getVertex("A"), graph.getVertex("C")));
        graph.addEdge(new HeavyweightDirectedEdge<>(graph.getVertex("D"), graph.getVertex("E")));

        return graph;
    }
}