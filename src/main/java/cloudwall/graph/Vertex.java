package cloudwall.graph;

/**
 * A node or vertex in a graph data structure. The only required attribute is a unique ID which must comply with
 * the standard equals/hashCode contract so it can be used to key into a map.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public interface Vertex {
    Object getVertexId();
}
