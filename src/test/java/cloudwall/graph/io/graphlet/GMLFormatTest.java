/*
 * (C) Copyright 2017 Kyle F. Downey.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloudwall.graph.io.graphlet;

import cloudwall.graph.io.StringDataSource;
import org.junit.Test;

import javax.activation.MimeType;
import javax.activation.URLDataSource;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GMLFormatTest {
    @Test
    public void roundTripFormat() throws Exception {
        URL resource = getClass().getResource("example1.gml");
        URLDataSource uds = new URLDataSource(resource);

        GMLFormat format = new GMLFormat();
        MimeType[] supported = format.getSupportedContentTypes();
        String contentType = supported[0].toString();

        format.read(uds, model -> {
            try {
                StringDataSource sds = new StringDataSource("<test>", contentType, Charset.defaultCharset());
                format.write(sds, model);
                String outputTxt = sds.toString();

                assertTrue(outputTxt.contains("graph [ ") && outputTxt.contains("edge [ "));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals("application/x-graphlet", supported[0].toString());
    }
}