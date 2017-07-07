/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cloudwall.graph;

/**
 * Conversion between a given graph model (basic node/edge data) and an optimized form
 * stripped of extraneous detail specific to the format and laid out internally to
 * facilitate graph analysis. You can also build a graph incrementally with the
 * {@link MutableGraph} interface -- compilation is intended for loaded models, and
 * to separate the concerns of representing graph data from graph analysis; tools
 * like format converters, basic viewers or pretty-printers can be built with just the model.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface GraphCompiler<G extends Graph, M extends GraphModel> {
    G compile(M model);
}
