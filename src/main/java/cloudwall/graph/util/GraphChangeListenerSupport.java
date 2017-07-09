package cloudwall.graph.util;

import cloudwall.graph.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Composable helper for implementing graph change event propagation.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class GraphChangeListenerSupport<V extends Vertex, E extends Edge> {
    private final MutableGraph<V, E> graph;
    private final List<GraphChangeListener> listeners = new CopyOnWriteArrayList<>();

    public GraphChangeListenerSupport(@Nonnull MutableGraph<V, E> graph) {
        this.graph = graph;
    }

    public void addGraphChangeListener(GraphChangeListener listener) {
        listeners.add(listener);
    }

    public void removeGraphChangeListener(GraphChangeListener listener) {
        listeners.remove(listener);
    }

    public void vertexAdded(V vertex) {
        fireEvent(new VertexAddedEvent<>(graph, vertex));
    }

    public void vertexRemoved(V vertex) {
        fireEvent(new VertexRemovedEvent<>(graph, vertex));
    }

    public void edgeAdded(E edge) {
        fireEvent(new EdgeAddedEvent<>(graph, edge));
    }

    public void edgeRemoved(E edge) {
        fireEvent(new EdgeRemovedEvent<>(graph, edge));
    }

    private void fireEvent(GraphChangeEvent event) {
        for (GraphChangeListener l : listeners) {
            l.changeApplied(event);
        }
    }
}
