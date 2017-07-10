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
package cloudwall.graph.analysis;

import cloudwall.graph.Edge;
import cloudwall.graph.Graph;
import cloudwall.graph.Vertex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * Traverses a graph and determines how many connected components comprise the graph. Each of these gets built as a
 * subgraph if the graph is not fully connected, else you are left just with the original graph as the only component.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class ConnectedComponentCollector<V extends Vertex, E extends Edge, G extends Graph<V,E>> implements GraphFunctor<V, E, G, Stream<ConnectedComponentCollector.Component>> {
    public static class Component<V> {
        private final long componentNumber;
        private final Collection<V> vertices;

        public Component(long componentNumber, Collection<V> vertices) {
            this.componentNumber = componentNumber;
            this.vertices = vertices;
        }

        public long getComponentNumber() {
            return componentNumber;
        }

        public Collection<V> getVertices() {
            return vertices;
        }
    }

    @Override
    public Stream<Component> apply(G g) {
        Collection<Component> components = new HashSet<>();
        Set<V> visited = new HashSet<>();
        AtomicLong numConnected = new AtomicLong(0L);
        g.forEachVertex(outer -> {
            if (!visited.contains(outer)) {
                Set<V> vertices = new HashSet<>();
                g.visitDepthFirstFrom(outer, inner -> {
                    if (!visited.contains(inner)) {
                        visited.add(inner);
                        vertices.add(inner);
                    }
                });
                components.add(new Component<>(numConnected.getAndIncrement(), vertices));
            }
        });
        return components.stream();
    }
}
