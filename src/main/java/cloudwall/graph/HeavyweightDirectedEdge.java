package cloudwall.graph;

import javax.annotation.Nonnull;

/**
 * Full-featured edge implementation (supporting weights and signs) for directed edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class HeavyweightDirectedEdge<V extends Vertex> extends HeavyweightEdge<V> implements Edge.DirectedEdge<V> {
    public HeavyweightDirectedEdge(@Nonnull V vertex0, @Nonnull V vertex1) {
        this(vertex0, vertex1, 1.0);
    }

    public HeavyweightDirectedEdge(@Nonnull V vertex0, @Nonnull V vertex1, double weight) {
        this(vertex0, vertex1, weight, (short)0);
    }

    public HeavyweightDirectedEdge(@Nonnull V vertex0, @Nonnull V vertex1, double weight, short sign) {
        super(vertex0, vertex1);
    }

    public String toString() {
        return getVertex0() + " -> " + getVertex1();
    }
}
