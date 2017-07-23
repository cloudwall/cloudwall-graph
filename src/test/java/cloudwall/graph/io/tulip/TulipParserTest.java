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

package cloudwall.graph.io.tulip;

import com.google.common.collect.ImmutableList;
import org.javafp.parsecj.State;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TulipParserTest {
    @Test
    public void parseNodesAndEdges() throws Exception {
        String tlp =
                ";(nodes <node_id> <node_id> ...)\n" +
                "(nodes 0 1 2 3 4 5 6 7)\n" +
                ";(edge <edge_id> <source_id> <target_id>)\n" +
                "(edge 0 6 2)\n" +
                "(edge 1 2 1)\n" +
                "(edge 2 1 4)";

        TulipInput in = new TulipInput(tlp);
        TulipModel model = TulipParser.tulipLight().parse(in.toInput()).getResult();
        System.out.println(model);
    }

    @Test
    public void parseGlobalProperty() throws Exception {
        String propertyTxt = "(property  0 double \"viewBorderWidth\"\n" +
                "(default \"1\" \"0\")\n" +
                ")";

        Property property = TulipParser.propertyDeclaration().parse(State.of(propertyTxt)).getResult();

        assertEquals(0, property.getClusterId());
        assertEquals(PropertyType.DOUBLE, property.getPropertyType());
        assertEquals("viewBorderWidth", property.getName());
        assertEquals("1", property.getNodeDefaultValue());
        assertEquals("0", property.getEdgeDefaultValue());
        assertTrue("no node values expected", property.getNodeValues().isEmpty());
        assertTrue("no edge values expected", property.getEdgeValues().isEmpty());
    }

    @Test
    public void parsePropertyAppliedToNodesOnly() throws Exception {
        String propertyTxt = "(property  0 layout \"viewLayout\"\n" +
                "(default \"(115,81,255)\" \"()\" )\n" +
                "(node 0 \"(5.91166,-2.81404,-6.84822e-08)\")\n" +
                "(node 1 \"(0.501728,-2.77041,-6.74205e-08)\")\n" +
                ")";

        Property property = TulipParser.propertyDeclaration().parse(State.of(propertyTxt)).getResult();
        assertEquals(0, property.getClusterId());
        assertEquals(PropertyType.LAYOUT, property.getPropertyType());
        assertEquals("viewLayout", property.getName());
        assertEquals("(115,81,255)", property.getNodeDefaultValue());
        assertEquals("()", property.getEdgeDefaultValue());
        assertEquals("(5.91166,-2.81404,-6.84822e-08)", property.getNodeValues().get(0));
        assertEquals("(0.501728,-2.77041,-6.74205e-08)", property.getNodeValues().get(1));
    }

    @Test
    public void parsePropertyAppliedToNodesAndEdges() throws Exception {
        String propertyTxt = "(property  0 layout \"viewLayout\"\n" +
                "(default \"(115,81,255)\" \"()\" )\n" +
                "(node 0 \"(5.91166,-2.81404,-6.84822e-08)\")\n" +
                "(node 1 \"(0.501728,-2.77041,-6.74205e-08)\")\n" +
                "(edge 4 \"((-2.33098,5.09903,1.2409e-07))\")\n" +
                "(edge 13 \"((3.94972,2.99467,7.2878e-08))\")\n" +
                ")";

        Property property = TulipParser.propertyDeclaration().parse(State.of(propertyTxt)).getResult();
        assertEquals(0, property.getClusterId());
        assertEquals(PropertyType.LAYOUT, property.getPropertyType());
        assertEquals("viewLayout", property.getName());
        assertEquals("(115,81,255)", property.getNodeDefaultValue());
        assertEquals("()", property.getEdgeDefaultValue());
        assertEquals("(5.91166,-2.81404,-6.84822e-08)", property.getNodeValues().get(0));
        assertEquals("(0.501728,-2.77041,-6.74205e-08)", property.getNodeValues().get(1));
        assertEquals("((-2.33098,5.09903,1.2409e-07))", property.getEdgeValues().get(4));
        assertEquals("((3.94972,2.99467,7.2878e-08))", property.getEdgeValues().get(13));
    }

    @Test
    public void parseTestCasesFromTulip() throws Exception {
        Collection<String> testCases = ImmutableList.of(
                "filesystem.tlp",
                "grid1010.tlp",
                "k5lostingrid5050.tlp",
                "k33k55.tlp",
                "k33lostInGrip.tlp",
                "openmetanode1.tlp",
                "planar30drawnFPP.tlp",
                "planar30drawnMM.tlp",
                "unbiconnected.tlp",
                "unconnected.tlp"
        );

        for (String testCase : testCases) {
            System.out.println(testCase);
            Reader r = new InputStreamReader(getClass().getResourceAsStream(testCase));
            TulipInput in = new TulipInput(r);
            TulipParser.newInstance().parse(in.toInput()).getResult();
        }
    }



}
