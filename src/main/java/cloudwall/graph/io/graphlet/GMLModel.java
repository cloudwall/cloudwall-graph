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
package cloudwall.graph.io.graphlet;

import cloudwall.graph.*;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the Graph Modeling Language (GML) used by Graphlet.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see GMLFormat
 */
@SuppressWarnings("WeakerAccess")
public class GMLModel implements GraphModel {
    private static final Integer IS_DIRECTED = Integer.valueOf(1);

    private List root;


    public GMLModel(List root) {
        this.root = root;
    }

    public List getRoot() {
        return root;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void visit(GraphVisitor visitor) {
        List graph = (List) root.getValue("graph");
        if (graph == null) {
            throw new IllegalStateException("mal-formed GML for graph representation: missing graph list");
        }
        visitor.start(this);

        Map<Object, LightweightVertex> vertices = new HashMap<>();
        Collection<List> nodes = graph.getValues("node");
        nodes.forEach(node -> {
            Object nodeId = node.getValue("id");
            LightweightVertex vertex = new LightweightVertex(nodeId);
            vertices.put(nodeId, vertex);
            visitor.visitVertex(vertex);
        });

        boolean isDigraph = supports(Feature.DIRECTED);
        Collection<List> edges = graph.getValues("edge");
        edges.forEach(edge -> {
            Object srcId = edge.getValue("source");
            Object tgtId = edge.getValue("target");

            LightweightVertex vertex0 = vertices.get(srcId);
            LightweightVertex vertex1 = vertices.get(tgtId);

            Edge<LightweightVertex> edgeToVisit = isDigraph
                    ? new HeavyweightDirectedEdge<>(vertex0, vertex1)
                    : new LightweightEdge<>(vertex0, vertex1);

            visitor.visitEdge((Edge)edgeToVisit);
        });


        visitor.complete();
    }

    @Override
    public boolean supports(Feature feature) {
        List graph = (List) root.getValue("graph");
        boolean isDirected = graph != null && IS_DIRECTED.equals(graph.getValue("directed"));
        return (feature == Feature.DIRECTED) && isDirected;
    }

    public interface Value {
        Class<?> getType();
        Object getValue();
    }

    public static class Scalar implements Value {
        private final Object value;
        private final Class<?> type;

        public Scalar(Object value, Class<?> type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public Class<?> getType() {
            return type;
        }

        @Override
        public Object getValue() {
            return value;
        }
    }

    public static class List implements Value, Iterable<ListEntry> {
        // for now doing O(n) linear search in getValue() / getValues() rather than storing multimap for every List
        private java.util.List<ListEntry> entries = new ArrayList<>();

        public Object getValue(@Nonnull String key) {
            return entries.stream().filter(entry -> key.equals(entry.key)).findFirst().orElseGet(null);
        }

        @SuppressWarnings("unchecked")
        public <T> Collection<T> getValues(@Nonnull String key) {
            return (Collection<T>)entries.stream().filter(entry -> key.equals(entry.key)).collect(Collectors.toList());
        }

        @Override
        public Class<?> getType() {
            return List.class;
        }

        @Override
        public Object getValue() {
            return entries;
        }

        public void addEntry(ListEntry entry) {
            entries.add(entry);
        }

        @Override
        @Nonnull
        public Iterator<ListEntry> iterator() {
            return entries.iterator();
        }
    }

    public static class ListEntry {
        private String key;
        private Value value;

        public ListEntry(String key, Value value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Value getValue() {
            return value;
        }
    }
}
