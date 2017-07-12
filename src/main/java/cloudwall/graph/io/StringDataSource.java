package cloudwall.graph.io;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.io.output.WriterOutputStream;

import javax.activation.DataSource;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Helper DataSource that reads and writes streams with text in a particular character set.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class StringDataSource implements DataSource {
    private final StringWriter stringWriter = new StringWriter();
    private final String name;
    private final String contentType;
    private final Charset charset;

    public StringDataSource() {
        this("<anonymous>", "text/plain", Charset.defaultCharset());
    }

    @SuppressWarnings("WeakerAccess")
    public StringDataSource(String name, String contentType, Charset charset) {
        this.name = name;
        this.contentType = contentType;
        this.charset = charset;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ReaderInputStream(new StringReader(stringWriter.toString()), charset);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new WriterOutputStream(stringWriter, charset);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public String toString() {
        return stringWriter.toString();
    }
}
