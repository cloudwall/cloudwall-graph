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

/**
 * Edge in the Tulip file format.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see TulipFormat
 */
public class Edge {
    private final int id;
    private final int node0;
    private final int node1;

    public Edge(int id, int node0, int node1) {
        this.id = id;
        this.node0 = node0;
        this.node1 = node1;
    }

    public int getId() {
        return id;
    }

    public int getNode0() {
        return node0;
    }

    public int getNode1() {
        return node1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return id == edge.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String toString() {
        return "(edge " + id + " " + node0 + " " + node1 + ")";
    }
}
