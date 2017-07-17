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

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Supported value types for Tulip properties.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public enum PropertyType {
    BOOLEAN("bool"),
    COLOR("color"),
    DOUBLE("double"),
    FLOAT("float"),
    LAYOUT("layout"),
    INT("int"),
    UINT("uint"),
    SIZE("size"),
    STRING("string"),
    COORDINATES("coord");

    private static Map<String,PropertyType> TYPE_MAP = new HashMap<>();
    static {
        for (PropertyType value : PropertyType.values()) {
            TYPE_MAP.put(value.typeName, value);
        }
    }

    public static @Nullable
    PropertyType getPropertyTypeByName(String name) {
        return TYPE_MAP.get(name.trim().toLowerCase());
    }


    private String typeName;

    PropertyType(String typeName) {
        this.typeName = typeName;
    }

    String getTypeName() {
        return typeName;
    }


    public String toString() {
        return typeName;
    }
}
