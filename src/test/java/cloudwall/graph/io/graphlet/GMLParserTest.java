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

import cloudwall.graph.io.ReaderInput;
import org.javafp.parsecj.Parser;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GMLParserTest {
    @Test
    public void parseGraphExample1() throws Exception {
        GMLModel model = parseExample("example1.gml");
        GMLModel.ListEntry graph = (GMLModel.ListEntry)model.getRoot().getValue("graph");
        GMLModel.List graphValue = (GMLModel.List) graph.getValue();
        GMLModel.ListEntry comment = (GMLModel.ListEntry) graphValue.getValue("comment");
        assertEquals("This is a sample graph", comment.getValue().getValue());
        assertNotNull(graph);
    }

    @Test
    public void parseGraphExample2() throws Exception {
        parseExample("example2.gml");
    }

    @Test
    public void parseGraphExample3() throws Exception {
        parseExample("example3.gml");
    }

    private GMLModel parseExample(String exampleResource) throws Exception {
        Parser<Character, GMLModel> parser = GMLParser.newInstance();
        Reader r = new InputStreamReader(getClass().getResourceAsStream(exampleResource));
        return parser.parse(new ReaderInput(r).toInput()).getResult();
    }
}