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
package cloudwall.graph.io.tulip;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Declared attribute in the Tulip file format.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see TulipFormat
 */
public class Property {
    private final int clusterId;
    private final PropertyType propertyType;
    private final String name;

    private Object nodeDefaultValue;
    private Object edgeDefaultValue;
    private Map<Integer, Object> nodeValues = new HashMap<>();
    private Map<Integer, Object> edgeValues = new HashMap<>();

    public Property(int clusterId, @Nonnull PropertyType propertyType, @Nonnull String name) {
        this.clusterId = clusterId;
        this.propertyType = propertyType;
        this.name = name;
    }

    public int getClusterId() {
        return clusterId;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public String getName() {
        return name;
    }

    public Object getNodeDefaultValue() {
        return nodeDefaultValue;
    }

    public void setNodeDefaultValue(Object nodeDefaultValue) {
        this.nodeDefaultValue = nodeDefaultValue;
    }

    public Object getEdgeDefaultValue() {
        return edgeDefaultValue;
    }

    public void setEdgeDefaultValue(Object edgeDefaultValue) {
        this.edgeDefaultValue = edgeDefaultValue;
    }

    public Map<Integer, Object> getNodeValues() {
        return nodeValues;
    }

    public void setNodeValue(int nodeId, @Nonnull Object value) {
        this.nodeValues.put(nodeId, value);
    }

    public Map<Integer, Object> getEdgeValues() {
        return edgeValues;
    }

    public void setEdgeValue(int edgeId, @Nonnull Object value) {
        this.edgeValues.put(edgeId, value);
    }
}
