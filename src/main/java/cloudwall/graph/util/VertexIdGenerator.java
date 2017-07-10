package cloudwall.graph.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple ID generator
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class VertexIdGenerator {
    private AtomicLong nextValue = new AtomicLong(0L);

    public long nextId() {
        return nextValue.incrementAndGet();
    }

}
