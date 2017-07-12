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