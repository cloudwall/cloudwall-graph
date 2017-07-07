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
import javax.annotation.Nonnull;
import javax.annotation.Signed;

/**
 * Basic interfaces for edges from one node to another.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface Edge<N> {
    @Nonnull
    N getNode0();

    @Nonnull
    N getNode1();

    /**
     * Gets either node; by convention should return node0.
     * @return
     */
    @Nonnull
    N getEither();

    /**
     * Gets the node opposite the given null.
     *
     * @throws IllegalArgumentException if node is not on the edge
     */
    @Nonnull
    N getOpposite(N node);

    /**
     * Specialization for directed edges that marks node0 and node1 as from and to ends respectively.
     */
    interface DirectedEdge<N> extends Edge<N> {
        @Nonnull
        N getFrom();

        @Nonnull
        N getTo();
    }

    /**
     * Specialization for weighted edges (e.g. distance, strength of affinity, etc.).
     */
    interface WeightedEdge<N> extends Edge<N> {
        @Nonnegative
        double getWeight();
    }

    /**
     * Specialization for signed edges (e.g. friendship vs. enmity).
     */
    interface SignedEdge<N> extends Edge<N> {
        /**
         * Returns zero or one, always.
         */
        @Signed
        short getSign();

        default boolean isPositive() {
            return getSign() >= 0;
        }

        default boolean isNegative() {
            return !isPositive();
        }
    }
}
