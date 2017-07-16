package cloudwall.graph.io.tulip;

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;

import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.*;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Reader for the Tulip graph visualization tool native format (TLP files).
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="http://tulip.labri.fr/TulipDrupal/">Tulip</a>
 * @see <a href="http://tulip.labri.fr/TulipDrupal/?q=tlp-file-format">TLP file format</a>
 */
public class TulipFormat implements GraphFormat<TulipModel> {
    private static final MimeType[] CONTENT_TYPES;
    static {
        try {
            CONTENT_TYPES = new MimeType[] { new MimeType("application/x-tulip-tlp") };
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MimeType[] getSupportedContentTypes() {
        return CONTENT_TYPES;
    }

    @Override
    public void read(DataSource dataIn, Consumer<TulipModel> modelConsumer) throws GraphFormatException, IOException {
        try (Reader r = new InputStreamReader(dataIn.getInputStream())) {
            modelConsumer.accept(null);
        } catch (Exception e) {
            throw new GraphFormatException("unexpected error", e);
        }
    }

    @Override
    public void write(DataSource dataOut, TulipModel model) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(dataOut.getOutputStream()))) {
            // output header
            pw.write("(tlp ");
            pw.write('"');
            pw.write(model.getVersion());
            pw.write('"');
            pw.write("\n");

            // output metadata
            maybeWriteField(pw, "date", model.getDate());
            maybeWriteField(pw, "author", model.getAuthor());
            maybeWriteField(pw, "comments", model.getComments());

            // output graph
            writeCluster(pw, model.getRootCluster());
            writeProperties(pw, model.getProperties());

            pw.write(")");

        }
    }

    private void writeNodes(PrintWriter pw, Collection<Node> nodes) {
        if (nodes.isEmpty()) {
            return;
        }
        pw.write("(nodes");
        nodes.forEach(n -> {
            pw.write(" ");
            pw.write(n.getId());
        });
        pw.write(")\n");
    }

    private void writeEdges(PrintWriter pw, Collection<Edge> edges) {
        edges.forEach(e -> {
            pw.write("(edge ");
            pw.write(e.getId());
            pw.write(" ");
            pw.write(e.getNode0().getId());
            pw.write(" ");
            pw.write(e.getNode1().getId());
            pw.write(")\n");
        });
    }

    private void writeCluster(PrintWriter pw, Cluster c) {
        boolean rootCluster = (c.getId() > 0);
        if (rootCluster) {
            pw.write("(cluster ");
            pw.write(c.getId());
            pw.write("\n");
        }
        writeNodes(pw, c.getNodes());
        writeEdges(pw, c.getEdges());
        c.getClusters().forEach(child -> writeCluster(pw, child));
        if (rootCluster) {
            pw.write(")\n");
        }
    }

    private void writeProperties(PrintWriter pw, Collection<Property> properties) {
        properties.forEach(p -> {
            pw.write("(property ");
            pw.write(p.getClusterId());
            pw.write(" ");
            pw.write(p.getPropertyType().toString());
            pw.write(" ");
            writeQuotedValue(pw, p.getName());
            pw.write("\n");
            pw.write("(default ");
            writeQuotedValue(pw, p.getNodeDefaultValue());
            pw.write(" ");
            writeQuotedValue(pw, p.getEdgeDefaultValue());
            pw.write(")\n");

            p.getNodeValues().forEach((k, v) -> {
                pw.write("(node ");
                pw.write(k);
                pw.write(" ");
                writeQuotedValue(pw, v);
                pw.write(")\n");
            });
            p.getEdgeValues().forEach((k, v) -> {
                pw.write("(edge ");
                pw.write(k);
                pw.write(" ");
                writeQuotedValue(pw, v);
                pw.write(")\n");
            });
        });
    }

    private void maybeWriteField(PrintWriter pw, String fieldName, Object value) {
        if (value != null) {
            pw.write("(");
            pw.write(fieldName);
            pw.write(" ");
            writeQuotedValue(pw, value);
            pw.write(")\n");
        }
    }

    private void writeQuotedValue(PrintWriter pw, Object value) {
        pw.write('"');
        if (value != null) {
            pw.write(value.toString());
        }
        pw.write('"');
    }
}
