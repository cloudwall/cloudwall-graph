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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.function.Consumer;

/**
 * Graph implementation based on adjacency lists internally. This is the benchmark as well: any more optimized graphs
 * need to compare favorably with this simple approach.
 *
 * <p>It supports both BFS and DFS, implemented with queue &amp; stack (iterative). Given the graph:
 *
 * <pre>
 *                  A
 *                /  \
 *               B   C
 *             /   / |  \
 *            D   E  F   G
 *               /        \
 *              H          I
 * </pre>
 *
 *
 * <p>The BFS traversal is A B C D E F G H I and the DFS traversal is A C G I F E H B D.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public class AdjacencyListGraph<V extends Vertex, E extends Edge<V>> implements MutableGraph<V, E> {
    private final Map<Object,V> vertices = new HashMap<>();
    private final Set<E> edges = new HashSet<>();

    private final Multimap<Object, E> adjacencyMap = ArrayListMultimap.create(1024, 8);

    @Override
    public void forEachVertex(Consumer<V> visitor) {
        for (V vertex : vertices.values()) {
            visitor.accept(vertex);
        }
    }

    @Override
    public void forEachEdge(Consumer<E> visitor) {
        for (E edge : edges) {
            visitor.accept(edge);
        }
    }

    @Override
    public void forEachConnectedEdge(V vertex, Consumer<E> edges) {
        adjacencyMap.get(vertex.getVertexId()).forEach(edges);
    }


    @SuppressWarnings("WeakerAccess")
    public void forEachTraversableEdge(V vertex, Consumer<E> edges) {
        adjacencyMap.get(vertex.getVertexId()).stream().filter(e -> e.isTraversable(vertex)).forEach(edges);
    }

    @Override
    public void visitBreadthFirstFrom(V start, Consumer<V> visitor) {
        Set<V> visitedVertices = new HashSet<>();
        LinkedList<V> toVisitQueue = new LinkedList<>();
        toVisitQueue.add(start);
        while (!toVisitQueue.isEmpty()) {
            V current = toVisitQueue.poll();
            if (!visitedVertices.contains(current)) {
                visitedVertices.add(current);
                visitor.accept(current);
                forEachTraversableEdge(current, e -> toVisitQueue.add(e.getOpposite(current)));
            }
        }
    }

    @Override
    public void visitDepthFirstFrom(V start, Consumer<V> visitor) {
        Set<V> visitedVertices = new HashSet<>();
        LinkedList<V> toVisitStack = new LinkedList<>();
        toVisitStack.push(start);
        while (!toVisitStack.isEmpty()) {
            V current = toVisitStack.pop();
            if (!visitedVertices.contains(current)) {
                visitedVertices.add(current);
                visitor.accept(current);
                forEachTraversableEdge(current, e -> toVisitStack.push(e.getOpposite(current)));
            }
        }
    }

    @Override
    public V getVertex(Object vertexId) {
        return vertices.get(vertexId);
    }

    @Override
    public long getVertexCount() {
        return vertices.size();
    }

    @Override
    public long getEdgeCount() {
        return edges.size();
    }

    @Override
    public void addVertex(V vertex) {
        vertices.put(vertex.getVertexId(), vertex);
    }

    @Override
    public void removeVertex(V vertex) throws NoSuchElementException {
        forEachConnectedEdge(vertex, this::removeEdge);
        vertices.remove(vertex.getVertexId());
    }

    @Override
    public void addEdge(E edge) throws NoSuchElementException {
        edges.add(edge);
        edge.forEachVertex(v -> adjacencyMap.put(v.getVertexId(), edge));
    }

    @Override
    public void removeEdge(E edge) throws NoSuchElementException {
        edges.remove(edge);
    }
}
