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

package cloudwall.graph.io.graphlet;

import org.javafp.parsecj.Parser;

import static cloudwall.graph.io.Parsers.jstring;
import static cloudwall.graph.io.Parsers.token;
import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Text.*;

/**
 * Parser for the raw GML format which puts no constraints on what's being represented -- it will accept any model
 * in this format, but it may or may not be translatable to a graph object:
 * <p>
 * <pre>
 *     GML ::= List
 *     List ::= (whitespace* Key whitespace+ Value)
 *     Value ::= Integer | Real | String | [ List ]
 *     Key ::= [ a-z A-Z ] [ a-z A-Z 0-9 ]
 *     Integer ::= sign digit+
 *     Real ::= sign digit* . digit* Mantissa
 *     String ::= " instring "
 *     sign ::= empty | + | -
 *     digit ::= [0-9]
 *     Mantissa ::= empty | E sign digit
 *     instring ::= ASCII - {&,"} | & character+ ;
 *     whitespace ::= space | tabulator | newline
 * </pre>
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="http://www.fim.uni-passau.de/fileadmin/files/lehrstuhl/brandenburg/projekte/gml/gml-technical-report.pdf">GML: A portable Graph File Format</a>
 */
@SuppressWarnings("WeakerAccess")
public class GMLParser {

    public static Parser<Character, GMLModel> newInstance() {
        return list().bind(list -> retn(new GMLModel(list)));
    }

    private static Parser<Character, GMLModel.List> list() {
        return many(attempt(listEntry())).bind(entries -> {
            GMLModel.List list = new GMLModel.List();
            entries.forEach(list::addEntry);
            return retn(list);
        });
    }

    private static Parser<Character, GMLModel.ListEntry> listEntry() {
        return alphaNum
                .bind(key -> wspaces.then(value()
                .bind(value -> retn(new GMLModel.ListEntry(key, value)))));
    }

    @SuppressWarnings("unchecked")
    private static Parser<Character, GMLModel.Value> value() {
        return choice(
                scalarValue(token(dble), Double.class),
                scalarValue(token(intr), Integer.class),
                scalarValue(jstring, String.class),
                token(chr('[')).then(list()
                        .bind(list -> token(chr(']'))
                                .bind(endList -> retn(list)
                                )
                        )
                )
        );
    }

    private static <T> Parser<Character, GMLModel.Value> scalarValue(Parser<Character, T> p, Class<?> clazz) {
        return p.bind(parsedValue -> retn(new GMLModel.Scalar(parsedValue, clazz)));
    }
}
