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

import org.javafp.data.IList;
import org.javafp.data.Unit;
import org.javafp.parsecj.Parser;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import java.time.LocalDate;

import static cloudwall.graph.io.Parsers.jstring;
import static cloudwall.graph.io.Parsers.keyword;
import static cloudwall.graph.io.Parsers.token;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

@SuppressWarnings("WeakerAccess")
class TulipParser {
    // tulip ::= tulip-full | tulip-light

    // tulip-light ::= nodes-decl edges-decl* clusters-decl* property-decl* attribute-decl* displaying-decl*

    // tulip-full ::= header nodes-decl edges-decl* clusters-decl* property-decl* ')'

    // header ::= '(' 'tlp' quoted-string date-attr? author-attr? comments-attr?
    private static Parser<Character, TulipModel> tulipHeader() {
        return openDecl("tlp")
                .bind(decl -> jstring
                        .bind(version -> option(dateAttribute(), null)
                                .bind(date -> option(authorAttribute(), null)
                                        .bind(author -> option(commentsAttribute(), null)
                                                .bind(comments -> {
                                                            TulipModel model = new TulipModel();
                                                            return retn(model);
                                                        }
                                                )
                                        )
                                )
                        )
                );
    }

    // date-attr ::= '(' 'date' quoted-string ')'
    private static Parser<Character, LocalDate> dateAttribute() {
        return openDecl("date")
                .bind(decl -> jstring
                        .bind(value -> retn(LocalDate.parse(value))
                        )
                );
    }

    // author-attr ::= '(' 'author' quoted-string ')'
    @SuppressWarnings("Convert2MethodRef")
    private static Parser<Character, String> authorAttribute() {
        return openDecl("author")
                .bind(decl -> jstring
                        .bind(value -> retn(value)));
    }

    // comments-attr ::= '(' 'comments' quoted-string ')'
    @SuppressWarnings("Convert2MethodRef")
    private static Parser<Character, String> commentsAttribute() {
        return openDecl("comments")
                .bind(decl -> jstring
                        .bind(value -> retn(value))
                );
    }

    // nodes-decl ::= '(' 'nodes' node-id+ ')'
    @SuppressWarnings("Convert2MethodRef")
    private static Parser<Character, IList<Integer>> nodes() {
        return openDecl("nodes").then(many1(intr).bind(value -> retn(value)));
    }

    // edges-decl ::= '(' 'edge' edge-id node-id node-id ')'

    // clusters-decl ::= '(' 'cluster' cluster-id node-list edge-list ')'

    // node-list ::= '(' 'nodes' node-id+ ')'

    // edge-list ::= '(' 'edges' edge-id+ ')'

    // property-decl ::= '(' 'property' cluster-id property-type quoted-string property-default-decl? applied-properties* ')'
    static Parser<Character, Property> propertyParser() {
        return open()
                .then(keyword("property")
                        .then(intr
                                .bind(clusterId -> propertyType()
                                        .bind(type -> jstring
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
                        )
                );
    }

    // attributes-decl ::= '(' 'attributes' attribute-decl* ')'
    private static Parser<Character, Unit> attributesDeclaration() {
        return open()
                .then(keyword("attributes")
                        .bind(attributes -> many(attributeDeclaration())
                                .bind(attr -> closeAndSkipAll()
                                )
                        )
                );
    }

    // displaying-decl ::= '(' 'displaying' attribute-decl* ')'
    private static Parser<Character, Unit> displayingDeclaration() {
        return open()
                .then(keyword("displaying"))
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
        return open()
                .then(keyword("DataSet")
                        .bind(dataSet -> jstring
                                .bind(name -> closeAndSkipAll()
                                )
                        )
                );
    }

    // attribute-decl ::= '(' property-type quoted-string | bool-value | double | integer ')'
    private static Parser<Character, Unit> attributeDeclaration() {
        return open()
                .then(propertyType()
                        .bind(type -> jstring
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
        return open()
                .then(keyword("default")
                        .then(jstring
                                .bind(nodeDefaultValue -> jstring
                                        .bind(edgeDefaultValue -> close()
                                                .bind(close -> retn(new Tuple2<>(nodeDefaultValue, edgeDefaultValue))
                                                )
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
                                .bind(applyType -> intr
                                        .bind(edgeId -> jstring
                                                .bind(value -> close()
                                                        .bind(close -> retn(new Tuple3<>(applyType, edgeId, value))
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static Parser<Character, Character> open() {
        return token(chr('('));
    }

    private static Parser<Character, String> openDecl(String name) {
        return open().then(keyword(name));
    }

    private static Parser<Character, Character> close() {
        return token(chr(')'));
    }

    // for elements we want to check for well-formedness but not consume, just finish by returning Unit
    private static Parser<Character, Unit> closeAndSkipAll() {
        return close().bind(close -> retn(Unit.unit));
    }
}
