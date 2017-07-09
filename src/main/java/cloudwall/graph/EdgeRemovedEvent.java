package cloudwall.graph;

/**
 * Event fired when an edge between two vertices is cut.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class EdgeRemovedEvent<V extends Vertex, E extends Edge> extends GraphChangeEvent<V,E> {
    private final E removedEdge;

    public EdgeRemovedEvent(MutableGraph<V, E> source, E removedEdge) {
        super(source);
        this.removedEdge = removedEdge;
    }

    @Override
    public void apply() {
        getGraph().removeEdge(removedEdge);
    }
}
