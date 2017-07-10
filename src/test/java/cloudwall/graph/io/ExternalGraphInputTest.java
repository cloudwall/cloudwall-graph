/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cloudwall.graph.io;

import org.junit.Test;

import javax.activation.MimeType;
import java.io.File;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;

import static org.junit.Assert.*;

public class ExternalGraphInputTest {
    @Test
    public void forGraphvizFileDetectType() throws Exception {
        File tmp = File.createTempFile("test", ".dot");
        GraphInput src = ExternalGraphInput.forFile(tmp);
        assertTrue("MIME type mismatch", new MimeType("text/vnd.graphviz").match(src.getContentType()));
        assertNotNull(src.getReader());
    }

    @Test
    public void loadLocalResourceFixedMimeType() throws Exception {
        URL testLoc = getClass().getResource("graphviz/helloworld.dot");
        MimeType mimeType = new MimeType("text/vnd.graphviz");
        ExternalGraphInput src = new ExternalGraphInput(testLoc, mimeType);
        Reader r = src.getReader();
        LineNumberReader lineReader = new LineNumberReader(r);
        String line = lineReader.readLine();

        assertEquals(mimeType, src.getContentType());
        assertEquals("digraph G {Hello->World}", line);
    }

}