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

import java.util.EventListener;

/**
 * Callback for any change to a {@link MutableGraph}. Note that these changes may be batched, e.g. with
 * {@link cloudwall.graph.transaction.TransactionalGraph} and so while you are guaranteed to get the updates in
 * order, you are not guaranteed to get them immediately after the change is requested.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface GraphChangeListener extends EventListener {
    void changeApplied(GraphChangeEvent event);
}
