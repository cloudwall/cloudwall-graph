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
package cloudwall.graph.transaction;

import cloudwall.graph.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Decorator that allows you to batch changes to a {@link MutableGraph} and then commit or rollback.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@ParametersAreNonnullByDefault
public class TransactionalGraph<V extends Vertex,E extends Edge<V>> implements MutableGraph<V, E> {
    private final MutableGraph<V,E> delegate;

    private final List<GraphChangeListener> listeners = new CopyOnWriteArrayList<>();
    private final List<GraphChangeEvent> queuedEvents = new ArrayList<>();

    public TransactionalGraph(MutableGraph<V, E> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void forEachVertex(Consumer<V> visitor) {
        delegate.forEachVertex(visitor);
    }

    @Override
    public void forEachEdge(Consumer<E> visitor) {
        delegate.forEachEdge(visitor);
    }

    @Override
    public void forEachConnectedEdge(V vertex, Consumer<E> edges) {
        delegate.forEachConnectedEdge(vertex, edges);
    }

    @Override
    public void visitBreadthFirstFrom(V start, Consumer<V> visitor) {
        delegate.visitBreadthFirstFrom(start, visitor);
    }

    @Override
    public void visitDepthFirstFrom(V start, Consumer<V> visitor) {
        delegate.visitDepthFirstFrom(start, visitor);
    }

    @Nullable
    @Override
    public V getVertex(@Nonnull Object vertexId) {
        return delegate.getVertex(vertexId);
    }

    @Override
    public long getVertexCount() {
        return delegate.getVertexCount();
    }

    @Override
    public long getEdgeCount() {
        return delegate.getEdgeCount();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addVertex(V vertex) {
        queueEvent(new VertexAddedEvent(delegate, vertex));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeVertex(V vertex) throws NoSuchElementException {
        queueEvent(new VertexRemovedEvent(delegate, vertex));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addEdge(E edge) throws NoSuchElementException {
        queueEvent(new EdgeAddedEvent(delegate, edge));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeEdge(E edge) throws NoSuchElementException {
        queueEvent(new EdgeRemovedEvent(delegate, edge));
    }

    @Override
    public void addGraphChangeListener(GraphChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeGraphChangeListener(GraphChangeListener listener) {
        listeners.remove(listener);
    }

    synchronized void begin() {
        assert queuedEvents.isEmpty();
    }

    @SuppressWarnings("unchecked")
    synchronized void commit() {
        for (GraphChangeEvent event: queuedEvents) {
            event.apply();
            fireEvent(event);
        }
        clearEventQueue();
    }

    synchronized void rollback() {
        clearEventQueue();
    }

    private synchronized void queueEvent(GraphChangeEvent event) {
        queuedEvents.add(event);
    }

    private void clearEventQueue() {
        queuedEvents.clear();
    }

    private void fireEvent(GraphChangeEvent event) {
        for (GraphChangeListener l : listeners) {
            l.changeApplied(event);
        }
    }
}
