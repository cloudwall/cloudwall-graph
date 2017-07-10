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

import java.util.function.Function;

/**
 * A graph analysis algorithm that gets applied to a graph and computes one or more values. In general these
 * objects are not threadsafe and are meant for one-time use.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@FunctionalInterface
public interface GraphFunctor<V extends Vertex, E extends Edge, G extends Graph<V,E>,R> extends Function<G,R> {
}
