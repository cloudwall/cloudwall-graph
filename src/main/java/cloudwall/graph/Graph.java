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

import javax.annotation.Nonnegative;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

/**
 * A basic graph representation that lets you visit nodes and edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@ParametersAreNonnullByDefault
public interface Graph<N, E extends Edge> {
    /**
     * Iterates over all nodes.
     */
    void forEachNode(Consumer<N> nodeVisitor);

    /**
     * Iterates over all edges connected to the given node, i.e. the ones connecting this node to its adjacencies.
     */
    void forEachConnectedEdge(N node, Consumer<E> edges);

    /**
     * Given a starting node performs a BFS (Breadth First Search) from that node. In case of a graph with cycles
     * the implementation should guarantee that the operation terminates and visits all nodes in scope only once.
     */
    void visitBreadthFirstFrom(N start, Consumer<N> nodeVisitor);

    /**
     * Given a starting node performs a DFS (Depth First Search) from that node. In case of a graph with cycles
     * the implementation should guarantee that the operation terminates and visits all nodes in scope only once.
     */
    void visitDepthFirstFrom(N start, Consumer<N> nodeVisitor);

    /**
     * Gets the number of nodes declared in this graph.
     */
    @Nonnegative long getNodeCount();

    /**
     * Gets the number of edges defined in this graph.
     */
    @Nonnegative long getEdgeCount();
}
