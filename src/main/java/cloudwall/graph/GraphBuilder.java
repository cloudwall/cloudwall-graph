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

package cloudwall.graph;

import javax.annotation.Nonnull;

/**
 * Helper class that builds a graph as a side effect of visiting a graph model.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class GraphBuilder implements GraphVisitor {
    private AdjacencyListGraph<Vertex, Edge<Vertex>> graph = new AdjacencyListGraph<>();

    public Graph<?,?> build(GraphModel model) {
        model.visit(this);
        return graph;
    }

    @Override
    public void visitVertex(@Nonnull Vertex vertex) {
        graph.addVertex(vertex);
    }

    @Override
    public void visitEdge(@Nonnull Edge<Vertex> edge) {
        graph.addEdge(edge);
    }
}
