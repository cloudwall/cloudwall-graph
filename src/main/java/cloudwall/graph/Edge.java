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

import org.jooq.lambda.tuple.Tuple;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Signed;
import java.util.function.Consumer;

/**
 * Basic interfaces for edges from one vertex to another.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface Edge<V extends Vertex> {
    @Nonnull
    V getVertex0();

    @Nonnull
    V getVertex1();

    /**
     * Gets either vertex; by convention should return {@link #getVertex0}.
     */
    @Nonnull
    default V getEither() {
        return getVertex0();
    }

    /**
     * Gets the vertex opposite the given vertex.
     *
     * @throws IllegalArgumentException if vertex is not on the edge
     */
    @Nonnull
    default V getOpposite(V vertex) {
        if (vertex == getVertex0()) {
            return getVertex1();
        } else if (vertex == getVertex1()) {
            return getVertex0();
        } else {
            throw new IllegalArgumentException("edge does not contain vertex: " + vertex);
        }
    }

    default void forEachVertex(@Nonnull Consumer<V> visitor) {
        visitor.accept(getVertex0());
        visitor.accept(getVertex1());
    }

    /**
     * Tests whether the given vertex is part of the edge.
     */
    default boolean containsVertex(@Nonnull V vertex) {
        return (getVertex0() == vertex) || (getVertex1() == vertex);
    }

    /**
     * Specialization for directed edges that marks vertex0 and vertex1 as from and to ends respectively.
     */
    interface DirectedEdge<V extends Vertex> extends Edge<V> {
        @Nonnull
        V getFrom();

        @Nonnull
        V getTo();
    }

    /**
     * Specialization for weighted edges (e.g. distance, strength of affinity, etc.).
     */
    interface WeightedEdge<V extends Vertex> extends Edge<V> {
        @Nonnegative
        double getWeight();
    }

    /**
     * Specialization for signed edges (e.g. friendship vs. enmity).
     */
    interface SignedEdge<V extends Vertex> extends Edge<V> {
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
