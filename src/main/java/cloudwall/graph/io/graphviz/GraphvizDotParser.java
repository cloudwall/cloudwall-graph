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

package cloudwall.graph.io.graphviz;

import cloudwall.graph.io.graphviz.GraphvizDotModel.*;
import org.javafp.data.IList;
import org.javafp.parsecj.Parser;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;

import static cloudwall.graph.io.Parsers.*;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

/**
 * Helper class for {@link GraphvizDotFormat} that implements a basic recursive descent parser for the
 * Graphviz DOT language.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="http://www.graphviz.org/content/dot-language">DOT grammar</a>
 */
@SuppressWarnings("WeakerAccess")
class GraphvizDotParser {
    // graph        : [ strict ] (graph | digraph) [ ID ] '{' stmt_list '}'
    @SuppressWarnings("unchecked")
    static Parser<Character, GraphvizDotModel> newInstance() {
        return option(keyword("strict").then(retn(true)), false)
                .bind(strict -> choice(keyword("graph"), keyword("digraph"))
                        .bind(graphType -> option(id(), null)
                                .bind(id -> wspaces.then(between(token(chr('{')), token(chr('}')), statementList())
                                        .bind(stmts -> {
                                                    GraphvizDotModel model = new GraphvizDotModel(id);
                                                    model.setStrict(strict);
                                                    model.setDigraph(graphType.equalsIgnoreCase("digraph"));
                                                    stmts.forEach(model::addStatement);
                                                    return retn(model);
                                                }
                                        )
                                )
                        )
                ));
    }

    // subgraph     : [ subgraph [ ID ] ] '{' stmt_list '}'
    static Parser<Character, Subgraph> subgraph() {
        return keyword("subgraph").then(option(id(), null)
                .bind(id -> wspaces.then(between(token(chr('{')), token(chr('}')), statementList()
                        .bind(stmts -> {
                            Subgraph subgraph = new Subgraph();
                            stmts.forEach(subgraph::addStatement);
                            return retn(null);
                        }))
                )
        ));
    }

    // stmt_list    : [ stmt [ ';' ] stmt_list ]
    @SuppressWarnings("unchecked")
    static Parser<Character, IList<Statement>> statementList() {
        return statement().bind(initialStmt -> many(statementListTail()).bind(tail -> {
            IList<Statement> stmts = IList.of(initialStmt);
            stmts.addAll((IList<Statement>)tail);
            return retn(stmts);
        }));
    }

    @SuppressWarnings("unchecked")
    static Parser<Character, ? extends Statement> statementListTail() {
        return token(chr(';')).then(statement());
    }

    // node_stmt | edge_stmt | attr_stmt | ID '=' ID | subgraph
    @SuppressWarnings("unchecked")
    static Parser<Character, ? extends Statement> statement() {
        IList<Parser<Character, ? extends Statement>> parsers = IList.of();
        parsers = parsers.add(attempt(attribute()));
        parsers = parsers.add(attempt(attributeStatement()));
        parsers = parsers.add(attempt(nodeStatement()));
        parsers = parsers.add(attempt(edgeStatement()));
        parsers = parsers.add(attempt(subgraph()));
        return choice((IList)parsers);
    }

    // edge_stmt    : (node_id | subgraph) edgeRHS [ attr_list ]
    static Parser<Character, EdgeStatement> edgeStatement() {
        return edgeTerminal().bind(lhs -> many1(edgeRHS())
                .bind(rhs -> option(attributeList(), new AttributeList())
                        .bind(attrs -> {
                            EdgeOp edgeOp;
                            Set<EdgeOp> edgeOps = new HashSet<>();
                            List<EdgeTerminal> rhsTerminals = new ArrayList<>();

                            rhs.forEach(terminal -> {
                                edgeOps.add(terminal.v1);
                                rhsTerminals.add(terminal.v2);
                            });

                            if (edgeOps.size() != 1) {
                                throw new IllegalStateException("expected exactly one edge op type; got " + edgeOps);
                            } else {
                                edgeOp = edgeOps.iterator().next();
                            }

                            EdgeStatement edgeStmt = new EdgeStatement(lhs, edgeOp, rhsTerminals, attrs);
                            return retn(edgeStmt);
                        })));
    }

    // edgeRHS      : edgeop (node_id | subgraph) [ edgeRHS ]
    @SuppressWarnings("unchecked")
    static Parser<Character, Tuple2<EdgeOp, EdgeTerminal>> edgeRHS() {
        return edgeOp().bind(op -> edgeTerminal().bind(terminal -> retn(new Tuple2(op, terminal))));
    }

    static Parser<Character, EdgeOp> edgeOp() {
        return regex("\\s*--|->\\s*").bind(op -> {
            switch (op) {
                case "--":
                    return retn(EdgeOp.UNDIRECTED);
                case "->":
                    return retn(EdgeOp.DIRECTED);
                default:
                    throw new IllegalArgumentException("unrecognized operator: " + op);
            }
        });
    }

    @SuppressWarnings("unchecked")
    static Parser<Character, EdgeTerminal> edgeTerminal() {
        IList<Parser<Character, ? extends EdgeTerminal>> parsers = IList.of();
        parsers = parsers.add(attempt(nodeId()));
        parsers = parsers.add(attempt(subgraph()));
        return choice((IList)parsers);
    }

    // node_stmt    : node_id [ attr_list ]
    static Parser<Character, NodeStatement> nodeStatement() {
        return nodeId().bind(nodeId -> attributeLists().bind(parsedAttrLists -> {
            Collection<AttributeList> attrLists = new ArrayList<>();
            parsedAttrLists.forEach(attrLists::add);
            NodeStatement nodeStatement = new NodeStatement(nodeId, attrLists);
            return retn(nodeStatement);
        }));
    }

    // attr_stmt    : (graph | node | edge) attr_list
    @SuppressWarnings("unchecked")
    static Parser<Character, AttributeStatement> attributeStatement() {
        return choice(
                keyword("graph"),
                keyword("node"),
                keyword("edge"))
                .bind(scopeName -> retn(AttributeScope.valueOf(scopeName.toUpperCase())))
                .bind(scope -> attributeLists().bind(parsedAttrLists -> {
                    Collection<AttributeList> attrLists = new ArrayList<>();
                    parsedAttrLists.forEach(attrLists::add);
                    return retn(new AttributeStatement(scope, attrLists));
                }));
    }

    // attr_list    : '[' [ a_list ] ']' [ attr_list ]
    static Parser<Character, IList<AttributeList>> attributeLists() {
        return many1(attributeList());
    }

    // a_list       : ID '=' ID [ (';' | ',') ] [ a_list ]
    static Parser<Character, AttributeList> attributeList() {
        return chr('[').then(attribute()
                .bind(attr -> many(or(chr(','), chr(';')).then(attribute()))))
                .bind(attrs -> chr(']').bind(endAttrs -> {
                    AttributeList attrList = new AttributeList();
                    attrs.forEach(attrList::addAttribute);
                    return retn(attrList);
                }));
    }

    static Parser<Character, Attribute> attribute() {
        return id().bind(attr -> token(chr('='))
                .bind(op -> id()
                        .bind(value -> retn(new Attribute(attr, value)))
                )
        );
    }

    // node_id      : ID [ port ]
    // port	        : ':' ID [ ':' compass_pt ] | ':' compass_pt
    @SuppressWarnings("unchecked")
    static Parser<Character, NodeId> nodeId() {
        return choice
                (
                        attempt(id().bind(id -> chr(':').then(id()
                                .bind(portId -> chr(':').then(compassPoint()
                                        .bind(pt -> retn(new NodeId(id, portId, pt)))))))),
                        attempt(id().bind(id -> chr(':').then(compassPoint()
                                .bind(pt -> retn(new NodeId(id, pt)))))),
                        attempt(id().bind(id -> chr(':').then(id()
                                .bind(portId -> retn(new NodeId(id, portId)))))),
                        attempt(id().bind(id -> retn(new NodeId(id))))
                );
    }

    // compass_pt	: (n | ne | e | se | s | sw | w | nw | c | _)
    @SuppressWarnings("unchecked")
    static Parser<Character, CompassPoint> compassPoint() {
        return choice
                (
                        attempt(keyword("ne").bind(pt -> retn(CompassPoint.NORTHEAST))),
                        attempt(keyword("nw").bind(pt -> retn(CompassPoint.NORTHWEST))),
                        attempt(keyword("n").bind(pt -> retn(CompassPoint.NORTH))),
                        attempt(keyword("se").bind(pt -> retn(CompassPoint.SOUTHEAST))),
                        attempt(keyword("sw").bind(pt -> retn(CompassPoint.SOUTHWEST))),
                        attempt(keyword("s").bind(pt -> retn(CompassPoint.SOUTH))),
                        attempt(keyword("e").bind(pt -> retn(CompassPoint.EAST))),
                        attempt(keyword("w").bind(pt -> retn(CompassPoint.WEST))),
                        attempt(keyword("c").bind(pt -> retn(CompassPoint.CENTER))),
                        attempt(keyword("_").bind(pt -> retn(CompassPoint.DEFAULT)))
                );
    }

    @SuppressWarnings("unchecked")
    static Parser<Character, Id> id() {
        return choice
                (
                        regex("[a-zA-Z\\0200-\\0377_]+[a-zA-Z\\0200-\\0377_0-9]*|\\d+").bind(id -> retn(new Id(id))),
                        jstring.bind(id -> retn(new Id(id, true)))
                );
    }
}
