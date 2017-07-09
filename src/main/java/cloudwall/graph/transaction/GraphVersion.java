package cloudwall.graph.transaction;

import javax.annotation.Nonnegative;

/**
 * Object holding a version number for a {@link TransactionalGraph}.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class GraphVersion {
    private final long version;

    public GraphVersion(@Nonnegative long version) {
        this.version = version;
    }

    public @Nonnegative long getVersionNumber() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphVersion that = (GraphVersion) o;

        return version == that.version;
    }

    @Override
    public int hashCode() {
        return (int) (version ^ (version >>> 32));
    }
}
