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
import javax.annotation.concurrent.Immutable;

/**
 * Basic edge that just stores the vertices as fields, and adds the ability to hold a value.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@Immutable
public class LightweightEdge<V extends Vertex> implements Edge<V> {
    private final V vertex0;
    private final V vertex1;

    public LightweightEdge(@Nonnull V vertex0, @Nonnull V vertex1) {
        this.vertex0 = vertex0;
        this.vertex1 = vertex1;
    }

    @Nonnull
    @Override
    public V getVertex0() {
        return vertex0;
    }

    @Nonnull
    @Override
    public V getVertex1() {
        return vertex1;
    }

    public String toString() {
        return getVertex0() + " - " + getVertex1();
    }
}
