package cloudwall.graph.io;

import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.MimetypesFileTypeMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GraphFormatTest {
    @Test
    public void enumerateFormats() {
        Set<String> contentTypes = new HashSet<>();
        for (GraphFormat format : GraphFormat.Provider.getSupportedFormats()) {
            for (MimeType contentType : format.getSupportedContentTypes()) {
                contentTypes.add(contentType.toString());
                System.out.println(contentType);
            }
        }
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        String dotContentType = fileTypeMap.getContentType("foo.dot");

        assertEquals("text/vnd.graphviz", dotContentType);
        assertTrue(contentTypes.contains(dotContentType));
    }
}