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
package cloudwall.graph.io.graphviz;

import cloudwall.graph.GraphModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Object model for the content of a Graphviz DOT model.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class GraphvizDotModel implements GraphModel {
    private boolean strict = false;
    private boolean digraph = false;

    private Id id;
    private List<Statement> statements = new ArrayList<>();

    public enum CompassPoint {
        NORTH("n"),
        NORTHEAST("ne"),
        EAST("e"),
        SOUTHEAST("se"),
        SOUTH("s"),
        SOUTHWEST("sw"),
        WEST("w"),
        NORTHWEST("nw"),
        CENTER("c"),
        DEFAULT("_");

        private String label;

        CompassPoint(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String toString() {
            return label;
        }
        
    }

    public static class Id {
        private String value;
        private boolean quoted;

        public Id(String value) {
            this(value, false);
        }

        public Id(String value, boolean quoted) {
            this.value = value;
            this.quoted = quoted;
        }

        public String toString() {
            if (!quoted) {
                return value;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append('"');
                sb.append(value);
                sb.append('"');
                return sb.toString();
            }
        }
    }

    public interface Statement {

    }

    public interface EdgeTerminal {

    }

    public static class Subgraph implements EdgeTerminal, Statement {
        private Id id;
    }

    public static class NodeId implements EdgeTerminal {
        private Id nodeId;
        private Id portId;
        private CompassPoint compassPoint;

        public NodeId(Id nodeId) {
            this(nodeId, null);
        }

        public NodeId(Id nodeId, CompassPoint compassPoint) {
            this(nodeId, null, compassPoint);
        }

        public NodeId(Id nodeId, Id portId, CompassPoint compassPoint) {
            this.nodeId = nodeId;
            this.portId = portId;
            this.compassPoint = compassPoint;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(nodeId);
            if (portId != null) {
                sb.append(":");
                sb.append(portId);
            }
            if (compassPoint != null) {
                sb.append(":");
                sb.append(compassPoint.getLabel());
            }
            return sb.toString();
        }
    }

    public static class AttributeList {
        private Map<Id, Id> attributes = new LinkedHashMap<>();

        public void addAttribute(Id name, Id value) {
            attributes.put(name, value);
        }

        public String toString() {
            if (attributes.isEmpty()) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (Map.Entry<Id,Id> entry : attributes.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    sb.append(";");
                }
                sb.setLength(sb.length() - 1);
                sb.append("]");
                return sb.toString();
            }

        }

        public boolean isEmpty() {
            return attributes.isEmpty();
        }
    }

    public enum AttributeScope {
        GRAPH("graph"),
        NODE("node"),
        EDGE("edge");

        private String label;

        AttributeScope(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public static class SingleAttributeStatement implements Statement {
        private Id id;
        private Id value;

    }

    public static class AttributeStatement implements Statement {
        private AttributeScope scope;
        private AttributeList attributes;

        public AttributeStatement(AttributeScope scope, AttributeList attributes) {
            this.scope = scope;
            this.attributes = attributes;
        }

        public String toString() {
            return conditionalToString(attributes, attrs -> scope + " " + attrs);
        }
    }

    public static class NodeStatement implements Statement {
        private NodeId id;
        private AttributeList attributes = new AttributeList();

        public String toString() {
            return conditionalToString(attributes, attrs -> id + " " + attrs);
        }
    }

    public enum EdgeOp {
        DIRECTED("->"),
        UNDIRECTED("--");

        private String label;

        EdgeOp(String label) {
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    public static class Edge {
        private EdgeTerminal node0;
        private EdgeOp operator;
        private EdgeTerminal node1;
    }

    public static class EdgeStatement implements Statement {
        private List<Edge> edges = new ArrayList<>();
        private AttributeList attributes;

        public String toString() {
            return conditionalToString(attributes, attrs -> edges + " " + attrs);
        }
    }

    private static String conditionalToString(AttributeList attributes, Function<AttributeList, String> f) {
        if (attributes.isEmpty()) {
            return "";
        } else {
            return f.apply(attributes);
        }


    }
}
