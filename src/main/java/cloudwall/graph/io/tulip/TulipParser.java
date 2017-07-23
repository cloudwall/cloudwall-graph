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

import com.google.common.annotations.VisibleForTesting;
import org.javafp.data.IList;
import org.javafp.data.Unit;
import org.javafp.parsecj.Parser;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static cloudwall.graph.io.Parsers.jstring;
import static cloudwall.graph.io.Parsers.keyword;
import static cloudwall.graph.io.Parsers.token;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

@SuppressWarnings("WeakerAccess")
class TulipParser {
    // tulip ::= tulip-full | tulip-light
    public static Parser<Character, TulipModel> newInstance() {
        return or(tulipLight(), tulipFull());
    }

    // tulip-light ::= nodes-decl edge-decl* cluster-decl* property-decl* attributes-decl? displaying-decl?
    @VisibleForTesting
    static Parser<Character, TulipModel> tulipLight() {
        return tulipRootCluster(new TulipModel());
    }

    // tulip-full ::= header nodes-decl edge-decl* clusters-decl* property-decl* attributes-decl? displaying-decl? ')'
    private static Parser<Character, TulipModel> tulipFull() {
        return tulipHeader().bind(model -> tulipRootCluster(model).bind(close -> retn(model)));
    }

    // header ::= '(' 'tlp' quoted-string date-attr? author-attr? comments-attr?
    private static Parser<Character, TulipModel> tulipHeader() {
        return openDecl("tlp")
                .bind(decl -> quotedString()
                        .bind(version -> option(dateAttribute(), null)
                                .bind(date -> option(authorAttribute(), null)
                                        .bind(author -> option(commentsAttribute(), null)
                                                .bind(comments -> {
                                                            TulipModel model = new TulipModel();
                                                            model.setDate(date);
                                                            model.setAuthor(author);
                                                            model.setComments(comments);
                                                            return retn(model);
                                                        }
                                                )
                                        )
                                )
                        )
                );
    }

    private static Parser<Character, TulipModel> tulipRootCluster(TulipModel model) {
        return nodes()
                .bind(nodes -> many(edge())
                        .bind(edges -> many(propertyDeclaration())
                                .bind(properties -> many(cluster())
                                        .bind(clusters -> optional(attributesDeclaration())
                                                .bind(controller -> optional(controllerDeclaration())
                                                        .bind(attributes -> optional(displayingDeclaration())
                                                                .bind(displaying -> {
                                                                    nodes.forEach(model::addNode);
                                                                    edges.forEach(model::addEdge);
                                                                    clusters.forEach(model::addCluster);
                                                                    properties.forEach(model::addProperty);
                                                                    return retn(model);
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    // date-attr ::= '(' 'date' quoted-string ')'
    private static Parser<Character, LocalDate> dateAttribute() {
        return openDecl("date")
                .bind(decl -> quotedString()
                        .bind(value -> close()
                                .bind(close -> retn(LocalDate.parse(value, DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                                )
                        )
                );
    }

    // author-attr ::= '(' 'author' quoted-string ')'
    @SuppressWarnings("Convert2MethodRef")
    private static Parser<Character, String> authorAttribute() {
        return openDecl("author")
                .bind(decl -> quotedString()
                        .bind(value -> close()
                                .bind(close -> retn(value)
                                )
                        )
                );
    }

    // comments-attr ::= '(' 'comments' quoted-string ')'
    @SuppressWarnings("Convert2MethodRef")
    private static Parser<Character, String> commentsAttribute() {
        return openDecl("comments")
                .bind(decl -> quotedString()
                        .bind(value -> close()
                                .bind(close -> retn(value)
                                )
                        )
                );
    }

    // nodes-decl ::= '(' 'nodes' node-id+ ')'
    @SuppressWarnings("Convert2MethodRef")
    static Parser<Character, IList<Node>> nodes() {
        return openDecl("nodes").then(many1(id())
                .bind(value -> close()
                        .bind(close -> retn(value.map(id -> new Node(id)))
                        )
                )
        );
    }

    // edges-decl ::= '(' 'edge' edge-id node-id node-id ')'
    private static Parser<Character, Edge> edge() {
        return openDecl("edge").then(id()
                .bind(edgeId -> id()
                        .bind(node0 -> id()
                                .bind(node1 -> close()
                                        .bind(close -> retn(new Edge(edgeId, node0, node1))
                                        )
                                )
                        )
                )
        );
    }

    // cluster-decl ::= '(' 'cluster' cluster-id quoted-string node-list edge-list cluster* ')'
    private static Parser<Character, Cluster> cluster() {
        return openDecl("cluster").then(id()
                .bind(clusterId -> jstring
                        .bind(clusterName -> clusterNodeList()
                                .bind(clusterNodes -> clusterEdgeList()
                                        .bind(clusterEdges -> many(cluster())
                                                .bind(clusters -> close()
                                                        .bind(close -> {
                                                                    Cluster cluster = new Cluster(clusterId, clusterName);
                                                                    clusterNodes.forEach(cluster::addNode);
                                                                    clusterEdges.forEach(cluster::addEdge);
                                                                    clusters.forEach(cluster::addCluster);
                                                                    return retn(cluster);
                                                                }
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    // node-list ::= '(' 'nodes' node-id+ ')'
    private static Parser<Character, IList<Integer>> clusterNodeList() {
        return openDecl("nodes").then(many1(id()).bind(nodeIds -> close().bind(close -> retn(nodeIds))));
    }

    // edge-list ::= '(' 'edges' edge-id+ ')'
    private static Parser<Character, IList<Integer>> clusterEdgeList() {
        return openDecl("edges").then(many1(id()).bind(edgeIds -> close().bind(close -> retn(edgeIds))));
    }


    // property-decl ::= '(' 'property' cluster-id property-type quoted-string property-default-decl? applied-properties* ')'
    @VisibleForTesting
    static Parser<Character, Property> propertyDeclaration() {
        return openDecl("property")
                .then(id()
                        .bind(clusterId -> propertyType()
                                .bind(type -> quotedString()
                                        .bind(name -> propertyDefault()
                                                .bind(defaults -> appliedProperties()
                                                        .bind(properties -> close()
                                                                .bind(close -> {
                                                                            Property prop = new Property(clusterId, type, name);
                                                                            prop.setNodeDefaultValue(defaults.v1());
                                                                            prop.setEdgeDefaultValue(defaults.v2());

                                                                            properties.forEach(p -> {
                                                                                if (p.v1().equals("node")) {
                                                                                    prop.setNodeValue(p.v2(), p.v3());
                                                                                } else {
                                                                                    prop.setEdgeValue(p.v2(), p.v3());
                                                                                }
                                                                            });

                                                                            return retn(prop);
                                                                        }
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );
    }

    // attributes-decl ::= '(' 'attributes' attribute-decl* ')'
    private static Parser<Character, Unit> attributesDeclaration() {
        return openDecl("attributes")
                .bind(attributes -> many(attributeDeclaration())
                        .bind(attr -> closeAndSkipAll()
                        )
                );
    }

    // displaying-decl ::= '(' 'displaying' attribute-decl* ')'
    private static Parser<Character, Unit> displayingDeclaration() {
        return openDecl("displaying")
                .then(many(attributeDeclaration()))
                .then(closeAndSkipAll());
    }

    // controller-decl ::= '(' 'controller' data-set* attribute-decl* ')'
    private static Parser<Character, Unit> controllerDeclaration() {
        return openDecl("controller")
                .then(many(dataSet()))
                .then(many(attributeDeclaration()))
                .then(closeAndSkipAll());
    }

    // data-set ::= '(' 'DataSet' quoted-string ')'
    private static Parser<Character, Unit> dataSet() {
        return openDecl("DataSet")
                .bind(dataSet -> quotedString()
                        .bind(name -> closeAndSkipAll()
                        )
                );
    }

    // attribute-decl ::= '(' property-type quoted-string | bool-value | double | integer ')'
    private static Parser<Character, Unit> attributeDeclaration() {
        return open()
                .then(propertyType()
                        .bind(type -> quotedString()
                                .bind(attribName -> attributeValues()
                                        .bind(attribValue -> closeAndSkipAll()
                                        )
                                )
                        )
                );
    }

    private static Parser<Character, Unit> attributeValues() {
        IList<Parser<Character, Unit>> valueParsers = IList.of();
        valueParsers = valueParsers.add(intr.bind(value -> retn(Unit.unit)));
        valueParsers = valueParsers.add(dble.bind(value -> retn(Unit.unit)));
        valueParsers = valueParsers.add(jstring.bind(value -> retn(Unit.unit)));
        valueParsers = valueParsers.add(keyword("true").bind(value -> retn(Unit.unit)));
        valueParsers = valueParsers.add(keyword("false").bind(value -> retn(Unit.unit)));
        return choice(valueParsers);
    }

    // property-type ::= 'bool' | 'double' | 'layout' | 'int' | 'size' | 'string'
    private static Parser<Character, PropertyType> propertyType() {
        IList<Parser<Character, PropertyType>> choices = IList.of();
        for (PropertyType propertyType : PropertyType.values()) {
            choices = choices.add(keyword(propertyType.toString()).then(retn(propertyType)));
        }
        return wspaces.then(choice(choices));
    }

    // property-default-decl ::= '(' 'default' quoted-string quoted-string ')'
    private static Parser<Character, Tuple2<String, String>> propertyDefault() {
        return openDecl("default")
                .then(quotedString()
                        .bind(nodeDefaultValue -> quotedString()
                                .bind(edgeDefaultValue -> close()
                                        .bind(close -> retn(new Tuple2<>(nodeDefaultValue, edgeDefaultValue))
                                        )
                                )
                        )
                );
    }

    // applied-properties ::= '(' ('node' | 'edge') id quoted-string ')'
    private static Parser<Character, IList<Tuple3<String, Integer, String>>> appliedProperties() {
        return many(
                open()
                        .then(or(keyword("node"), keyword("edge"))
                                .bind(applyType -> id()
                                        .bind(edgeId -> quotedString()
                                                .bind(value -> close()
                                                        .bind(close -> retn(new Tuple3<>(applyType, edgeId, value))
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static Parser<Character, String> quotedString() {
        return wspaces.then(jstring);
    }

    private static Parser<Character, Integer> id() {
        return wspaces.then(intr);
    }

    private static Parser<Character, Character> open() {
        return token(chr('('));
    }

    private static Parser<Character, Unit> openDecl(String name) {
        return open().label("(" + name).then(keyword(name)).then(wspaces);
    }

    private static Parser<Character, Character> close() {
        return token(chr(')'));
    }

    // for elements we want to check for well-formedness but not consume, just finish by returning Unit
    private static Parser<Character, Unit> closeAndSkipAll() {
        return close().bind(close -> retn(Unit.unit));
    }
}
