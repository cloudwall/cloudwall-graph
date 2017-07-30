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
 * Visitor that exposes the structure of a graph in terms of the library's standard interfaces. The sequence of visits
 * depends on the specific algorithm.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface GraphVisitor {
    /**
     * Signals we are about to enter a graph defined by the given metadata.
     */
    default void start(@Nonnull GraphMetadata metadata) { }

    /**
     * Visits a vertex definition. You are guaranteed to get a callback for every vertex in the graph at most once,
     * but there are no guarantees about ordering.
     */
    default void visitVertex(@Nonnull Vertex vertex) { }

    /**
     * Visits an edge definition. You are guaranteed to get a callback for every edge in the graph at most once,
     * but there are no guarantees about ordering.
     */
    default void visitEdge(@Nonnull Edge<Vertex> edge) { }

    /**
     * Signals there are no more vertices or edges.
     */
    default void complete() { }
}
