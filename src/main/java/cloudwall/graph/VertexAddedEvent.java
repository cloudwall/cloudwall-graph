package cloudwall.graph;

import javax.annotation.Nonnull;

/**
 * Event fired when a new vertex has been added to the graph.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class VertexAddedEvent<V extends Vertex, E extends Edge> extends GraphChangeEvent<V,E> {
    private final V addedVertex;

    public VertexAddedEvent(@Nonnull MutableGraph<V, E> graph, @Nonnull V addedVertex) {
        super(graph);
        this.addedVertex = addedVertex;
    }

    @Nonnull
    public V getAddedVertex() {
        return addedVertex;
    }

    @Override
    public void apply() {
        getGraph().addVertex(addedVertex);
    }
}
