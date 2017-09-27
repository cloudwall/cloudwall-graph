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

import cloudwall.graph.analysis.GraphFunctor;

import javax.annotation.*;
import java.util.function.Consumer;

/**
 * A basic graph representation that lets you visit vertices and edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@ParametersAreNonnullByDefault
public interface Graph<V extends Vertex, E extends Edge> {
    /**
     * Iterates over all vertices.
     */
    void forEachVertex(Consumer<V> visitor);

    /**
     * Iterates over all edges.
     */
    void forEachEdge(Consumer<E> visitor);

    /**
     * Iterates over all edges connected to the given vertex, i.e. the ones connecting this vertex to its adjacencies.
     */
    void forEachConnectedEdge(V vertex, Consumer<E> edges);

    /**
     * Iterates over all edges connected to the given vertex that can be traversed; this will differ from
     * {@link #forEachConnectedEdge(Vertex, Consumer)} in the case of inbound directed edges.
     */
    void forEachTraversableEdge(V vertex, Consumer<E> edges);

    /**
     * Given a starting vertex performs a BFS (Breadth First Search) from that vertex. In case of a graph with cycles
     * the implementation should guarantee that the operation terminates and visits all vertices in scope only once.
     */
    void visitBreadthFirstFrom(V start, Consumer<V> visitor);

    /**
     * Given a starting vertex performs a DFS (Depth First Search) from that vertex. In case of a graph with cycles
     * the implementation should guarantee that the operation terminates and visits all vertices in scope only once.
     * The default visiting order is <em>pre-order</em>.
     */
    void visitDepthFirstFrom(V start, Consumer<V> visitor);

    /**
     * Given a unique vertex ID look up the vertex.
     */
    @Nullable
    V getVertex(@Nonnull Object vertexId);

    /**
     * Gets the number of vertices declared in this graph.
     */
    @Nonnegative long getVertexCount();

    /**
     * Gets the number of edges defined in this graph.
     */
    @Nonnegative long getEdgeCount();

    default <R> R apply(GraphFunctor<V, E, Graph<V,E>, R> f) {
        return f.apply(this);
    }
}
