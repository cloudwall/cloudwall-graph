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

package cloudwall.graph.io.graphviz;

import org.javafp.parsecj.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphvizDotParserTest {
    @Test
    public void parseCompassPoints() throws Exception {
        for (GraphvizDotModel.CompassPoint point : GraphvizDotModel.CompassPoint.values()) {
            GraphvizDotParser.compassPoint().parse(State.of(point.getLabel()));
        }
    }

    @Test
    public void parseNodeId() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(State.of("A_BC")).getResult();
        assertEquals("A_BC", nodeId.toString());
    }

    @Test
    public void parseNodeIdInt() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(State.of("123")).getResult();
        assertEquals("123", nodeId.toString());
    }

    @Test
    public void parseNodeIdPortIdBothInt() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(State.of("123:456")).getResult();
        assertEquals("123:456", nodeId.toString());
    }

    @Test
    public void parseNodeIdWithPort() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(State.of("\"X yz\":32")).getResult();
        assertEquals("\"X yz\":32", nodeId.toString());
    }

    @Test
    public void parseNodeIdWithCompassPoint() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(State.of("x64:nw")).getResult();
        assertEquals("x64:nw", nodeId.toString());
    }
    
    @Test
    public void parseNodeIdWithPortAndCompassPoint() throws Exception {
        GraphvizDotModel.NodeId nodeId = GraphvizDotParser.nodeId().parse(State.of("ABC:123:_")).getResult();
        assertEquals("ABC:123:_", nodeId.toString());
    }
}