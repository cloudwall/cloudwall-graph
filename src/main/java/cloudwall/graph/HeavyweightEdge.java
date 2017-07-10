package cloudwall.graph;

import javax.annotation.Nonnull;

/**
 * Full-featured edge implementation (supporting weights and signs) for undirected edges.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class HeavyweightEdge<V extends Vertex> implements Edge.WeightedEdge<V>, Edge.SignedEdge<V> {
    private final V vertex0;
    private final V vertex1;

    public HeavyweightEdge(@Nonnull V vertex0, @Nonnull V vertex1) {
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
