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
}
