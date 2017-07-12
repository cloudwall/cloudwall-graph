package cloudwall.graph.io.tulip;

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;

import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
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

    }

    @Override
    public void write(DataSource dataOut, TulipModel model) {

    }
}
