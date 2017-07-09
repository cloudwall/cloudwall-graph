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

import java.util.NoSuchElementException;

/**
 * Extensions to the basic graph interface allowing mutation.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface MutableGraph<V extends Vertex, E extends Edge> extends Graph<V,E> {
    /**
     * Adds a new vertex to the given graph.
     */
    void addVertex(V vertex);

    /**
     * Removes the given vertex from the graph, including any edges including it. For the latter note that you will
     * receive events for the edge removals before the vertex removal.
     *
     * @throws NoSuchElementException if the vertex is not a member of the graph
     */
    void removeVertex(V vertex) throws NoSuchElementException;

    /**
     * Creates an edge for the given pair of vertices in the graph.
     *
     * @throws NoSuchElementException if the edge vertices are not members of the graph
     */
    void addEdge(E edge) throws NoSuchElementException;

    /**
     * Removes the given edge from the graph.
     *
     * @throws NoSuchElementException if the edge is not in the graph.
     */
    void removeEdge(E edge) throws NoSuchElementException;

    /**
     * Adds the given listener to receive notification of add/remove operations on vertices & edges.
     */
    void addGraphChangeListener(GraphChangeListener listener);

    /**
     * Removes the given listener, if present, else no-op.
     */
    void removeGraphChangeListener(GraphChangeListener listener);
}
