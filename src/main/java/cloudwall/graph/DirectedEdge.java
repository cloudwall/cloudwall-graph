package cloudwall.graph;

import javax.annotation.Nonnull;

/**
 * Specialization for directed edges that marks vertex0 and vertex1 as from and to ends respectively.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class DirectedEdge<V extends Vertex> extends LightweightEdge<V> {
    public DirectedEdge(@Nonnull V from, @Nonnull V to) {
        super(from, to);
    }

    @Nonnull
    public V getFrom() {
        return getVertex0();
    }

    @Nonnull
    public V getTo() {
        return getVertex1();
    }

    @Override
    public boolean isTraversable(V vertex) {
        return (vertex == getFrom());
    }

    public String toString() {
        return getFrom() + " -> " + getTo();
    }
}
