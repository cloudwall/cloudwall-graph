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

import cloudwall.graph.GraphModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the Graph Modeling Language (GML) used by Graphlet.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see GMLFormat
 */
@SuppressWarnings("WeakerAccess")
public class GMLModel implements GraphModel {
    private List root;

    public GMLModel(List root) {
        this.root = root;
    }

    public List getRoot() {
        return root;
    }

    public interface Value {
        Object getValue();
    }

    public static class Scalar implements Value {
        private final Object value;

        public Scalar(Object value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }
    }

    public static class List implements Value {
        private Map<String, ListEntry> entries = new LinkedHashMap<>();

        public Object getValue(String key) {
            return entries.get(key);
        }

        @Override
        public Object getValue() {
            return entries;
        }

        public void addEntry(ListEntry entry) {
            entries.put(entry.getKey(), entry);
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
