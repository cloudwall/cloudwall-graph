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
import org.javafp.parsecj.Parser;
import org.jooq.lambda.tuple.Tuple2;
import org.jooq.lambda.tuple.Tuple3;

import static cloudwall.graph.io.Parsers.jstring;
import static cloudwall.graph.io.Parsers.keyword;
import static cloudwall.graph.io.Parsers.token;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

@SuppressWarnings("WeakerAccess")
class TulipParser {
    // property-decl ::= '(' 'property' cluster-id property-type quoted-string property-default-decl? property-bindings* ')'
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

    // edge-property-decl ::= '(' 'node' edge-id quoted-string ')'
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

    private static Parser<Character, Character> close() {
        return token(chr(')'));
    }
}
