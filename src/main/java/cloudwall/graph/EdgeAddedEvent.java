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
 * Event fired when a new edge is created between two vertices. If the vertices themselves have not been added previously
 * it may be preceded by {@link VertexAddedEvent}.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class EdgeAddedEvent<V extends Vertex, E extends Edge> extends GraphChangeEvent<V,E> {
    private final E edge;

    public EdgeAddedEvent(MutableGraph<V, E> graph, E edge) {
        super(graph);
        this.edge = edge;
    }

    @Override
    public void apply() {
        getGraph().addEdge(edge);
    }
}
