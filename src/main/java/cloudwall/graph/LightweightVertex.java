package cloudwall.graph;

import cloudwall.graph.util.VertexIdGenerator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Base implementation of a vertex that just stores the ID.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@Immutable
public class LightweightVertex implements Vertex {
    private final Object id;

    /**
     * Helper factory method for creating a vertex with an auto-generated ID.
     */
    public static LightweightVertex newInstance(VertexIdGenerator generator) {
        return new LightweightVertex(generator.nextId());
    }

    public LightweightVertex(@Nonnull Object id) {
        this.id = id;
    }

    @Override
    public Object getVertexId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LightweightVertex that = (LightweightVertex) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return id.toString();
    }
}
