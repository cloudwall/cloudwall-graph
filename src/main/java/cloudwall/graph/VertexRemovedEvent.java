package cloudwall.graph;

import javax.annotation.Nonnull;

/**
 * Event fired when a vertext is removed from the graph. If the vertex is a member of an existing {@link Edge}
 * it may be preceded by {@link EdgeRemovedEvent}.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class VertexRemovedEvent <V extends Vertex, E extends Edge> extends GraphChangeEvent<V,E> {
    private final V removedVertex;

    public VertexRemovedEvent(@Nonnull MutableGraph<V, E> graph, @Nonnull V removedVertex) {
        super(graph);
        this.removedVertex = removedVertex;
    }


    public @Nonnull V getRemovedVertex() {
        return removedVertex;
    }

    @Override
    public void apply() {
        getGraph().removeVertex(removedVertex);
    }
}
