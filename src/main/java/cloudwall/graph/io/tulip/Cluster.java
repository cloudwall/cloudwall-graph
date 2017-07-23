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
import java.util.*;

/**
 * A cluster (subgraph) in the Tulip file format.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see TulipFormat
 */
public class Cluster {
    private final int id;
    private final String name;

    private List<Integer> nodes = new ArrayList<>();
    private List<Integer> edges = new ArrayList<>();
    private List<Cluster> clusters = new ArrayList<>();

    public Cluster() {
        this(0, "root");
    }

    public Cluster(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<Integer> getNodes() {
        return nodes;
    }

    public void addNode(@Nonnull Integer nodeId) {
        nodes.add(nodeId);
    }

    public Collection<Integer> getEdges() {
        return edges;
    }

    public void addEdge(@Nonnull Integer edgeId) {
        edges.add(edgeId);
    }

    public Collection<Cluster> getClusters() {
        return clusters;
    }

    public void addCluster(@Nonnull Cluster cluster) {
        clusters.add(cluster);
    }

}
