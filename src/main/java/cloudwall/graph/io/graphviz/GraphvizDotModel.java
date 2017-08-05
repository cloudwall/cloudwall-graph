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

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 * Object model for the content of a Graphviz DOT model.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@SuppressWarnings("WeakerAccess")
public class GraphvizDotModel implements GraphModel {
    private final Id id;
    private final List<Statement> statements = new ArrayList<>();

    private boolean strict = false;
    private boolean digraph = false;

    public GraphvizDotModel(@Nullable Id id) {
        this.id = id;
    }

    public @Nullable Id getId() {
        return id;
    }

    public boolean isStrict() {
        return strict;
    }

    public boolean isDigraph() {
        return digraph;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public void setDigraph(boolean digraph) {
        this.digraph = digraph;
    }

    public void addStatement(Statement statement) {
        statements.add(statement);
    }

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
        private final Id id;
        private final List<Statement> statements = new ArrayList<>();

        public Subgraph() {
            this(null);
        }

        public Subgraph(Id id) {
            this.id = id;
        }

        public void addStatement(Statement stmt) {
            statements.add(stmt);
        }
    }

    public static class NodeId implements EdgeTerminal {
        private Id nodeId;
        private Id portId;
        private CompassPoint compassPoint;

        public NodeId(Id nodeId) {
            this(nodeId, null, null);
        }

        public NodeId(Id nodeId, Id portId) {
            this(nodeId, portId, null);
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
        private List<Attribute> attributes = new ArrayList<>();

        public void addAttribute(Attribute attr) {
            attributes.add(attr);
        }

        public String toString() {
            if (attributes.isEmpty()) {
                return "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (Attribute attr : attributes) {
                    sb.append(attr.id);
                    sb.append("=");
                    sb.append(attr.value);
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


    public static class Attribute implements Statement {
        private final Id id;
        private final Id value;

        public Attribute(Id id, Id value) {
            this.id = id;
            this.value = value;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(id);
            sb.append("=");
            sb.append(value);
            return sb.toString();
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

    public static class AttributeStatement implements Statement {
        private final AttributeScope scope;
        private final Collection<AttributeList> attributes;

        public AttributeStatement(AttributeScope scope, Collection<AttributeList> attributes) {
            this.scope = scope;
            this.attributes = attributes;
        }

        public String toString() {
            return conditionalToString(attributes, attrs -> scope + " " + attrs);
        }
    }

    public static class NodeStatement implements Statement {
        private final NodeId id;
        private final Collection<AttributeList> attributes;

        public NodeStatement(NodeId id, Collection<AttributeList> attributes) {
            this.id = id;
            this.attributes = attributes;
        }

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

    public static class EdgeStatement implements Statement {
        private final EdgeTerminal lhsTerminal;
        private final EdgeOp operator;
        private final List<EdgeTerminal> rhsTerminals;
        private final AttributeList attributes;

        public EdgeStatement(EdgeTerminal lhsTerminal,
                             EdgeOp operator,
                             List<EdgeTerminal> rhsTerminals,
                             AttributeList attributes) {
            this.lhsTerminal = lhsTerminal;
            this.operator = operator;
            this.rhsTerminals = rhsTerminals;
            this.attributes = attributes;
        }
    }

    private static String conditionalToString(AttributeList attributes, Function<AttributeList, String> f) {
        if (attributes.isEmpty()) {
            return "";
        } else {
            return f.apply(attributes);
        }
    }

    private static String conditionalToString(Collection<AttributeList> attributeLists, Function<AttributeList, String> f) {
        if (attributeLists.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (AttributeList attributes : attributeLists) {
                sb.append(conditionalToString(attributes, f));
            }
            return sb.toString();
        }
    }
}
