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

import cloudwall.graph.GraphModel;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Top level model for the Tulip file format.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see TulipFormat
 */
@SuppressWarnings("WeakerAccess")
public class TulipModel implements GraphModel {
    public static final String CURRENT_VERSION = "2.0";

    private final String version;
    private LocalDate date;
    private String author;
    private String comments;

    private Collection<Node> nodes = new ArrayList<>();
    private Collection<Edge> edges = new ArrayList<>();
    private Collection<Cluster> clusters = new ArrayList<>();
    private Collection<Property> properties = new ArrayList<>();

    public TulipModel() {
        this(CURRENT_VERSION);
    }

    public TulipModel(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Collection<Node> getNodes() {
        return nodes;
    }

    public Collection<Integer> getNodeIds() {
        return nodes.stream().map(Node::getId).collect(Collectors.toList());
    }

    public void addNode(@Nonnull Node node) {
        nodes.add(node);
    }

    public Collection<Edge> getEdges() {
        return edges;
    }

    public void addEdge(@Nonnull Edge edge) {
        edges.add(edge);
    }

    public Collection<Cluster> getClusters() {
        return clusters;
    }

    public void addCluster(@Nonnull Cluster cluster) {
        clusters.add(cluster);
    }

    public Collection<Property> getProperties() {
        return properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }
}
