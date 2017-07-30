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

/**
 * Descriptive information about the features supported by a {@link Graph}, to help guide implementors of
 * {@link GraphVisitor} in terms of allowed casts and relevant algorithms.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface GraphMetadata {
    enum Feature {
        /**
         * The graph is a digraph, with meaningful source and targets vertices on each edge.
         */
        DIRECTED,

        /**
         * Edges have a sign (positive, negative, neutral).
         */
        SIGNED,

        /**
         * Edges have a weight expressing relevance, strength of connection, distance, etc..
         */
        WEIGHTED,
    }

    /**
     * Gets the number of vertices in the graph, or -1 if unknown.
     */
    default long getVertexCount() {
        return -1;
    }


    /**
     * Gets the number of edges in the graph, or -1 if unknown.
     */
    default long getEdgeCount() {
        return -1;
    }

    /**
     * Tests whether the given {@link Feature} is supported by this graph.
     */
    default boolean supports(Feature feature) {
        return false;
    }
}
