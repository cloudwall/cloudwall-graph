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
public interface MutableGraph<N, E extends Edge> extends Graph<N,E> {
    /**
     * Adds a new node to the given graph.
     */
    void addNode(N node);

    /**
     * Removes the given node from the graph, including any edges including it.
     *
     * @throws NoSuchElementException if the node is not a member of the graph
     */
    void removeNode(N node) throws NoSuchElementException;

    /**
     * Creates an edge for the given pair of nodes in the graph.
     *
     * @throws NoSuchElementException if the edge nodes are not members of the graph
     */
    void addEdge(E edge) throws NoSuchElementException;

    /**
     * Removes the given edge from the graph.
     *
     * @throws NoSuchElementException if the edge is not in the graph.
     */
    void removeEdge(E edge) throws NoSuchElementException;
}
