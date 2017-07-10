package cloudwall.graph;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Base implementation of a vertex that just stores the ID.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@Immutable
public class DefaultVertex implements Vertex {
    private final Object id;

    public DefaultVertex(@Nonnull Object id) {
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

        DefaultVertex that = (DefaultVertex) o;

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
