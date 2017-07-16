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

package cloudwall.graph.io;

import org.javafp.data.IList;
import org.javafp.parsecj.Parser;

import static org.javafp.parsecj.Combinators.*;
import static org.javafp.parsecj.Combinators.many;
import static org.javafp.parsecj.Combinators.retn;
import static org.javafp.parsecj.Text.chr;
import static org.javafp.parsecj.Text.string;
import static org.javafp.parsecj.Text.wspaces;

/**
 * Mini parser classes to embed using parsecj.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@SuppressWarnings("WeakerAccess")
public class Parsers {
    public static final Parser<Character, Byte> hexDigit =
        satisfy((Character c) -> Character.digit(c, 16) != -1)
            .bind(c -> retn((byte) Character.digit(c, 16))).label("hex digit");

    public static final Parser<Character, Character> unicodeEscapeChar =
        hexDigit.bind(d0 ->
            hexDigit.bind(d1 ->
                hexDigit.bind(d2 ->
                    hexDigit.bind(d3 ->
                        retn((d0<<0x3) & (d1<<0x2) & (d2<<0x1) & d0)
                    )
                )
            )
        ).bind(i -> retn((char) i.intValue()));

    @SuppressWarnings("unchecked")
    public static final Parser<Character, Character> stringEscape =
            choice(
                chr('"'),
                chr('\\'),
                chr('/'),
                chr('b').then(retn('\b')),
                chr('f').then(retn('\f')),
                chr('n').then(retn('\n')),
                chr('r').then(retn('\r')),
                chr('t').then(retn('\t')),
                chr('u').then(unicodeEscapeChar)
                ).label("escape character");

    public static final Parser<Character, Character> stringChar =
        (
            chr('\\').then(stringEscape)
        ).or(
            satisfy(c -> c != '"' && c != '\\')
        );

    public static final Parser<Character, String> jstring =
        token(between(
            wspaces.then(chr('"')),
            wspaces.then(chr('"')),
            many(stringChar).bind(l -> retn(IList.listToString(l)))
        )).label("string");

    public static <T> Parser<Character, T> token(Parser<Character, T> p) {
        return p.bind(x -> wspaces.then(retn(x)));
    }

    public static Parser<Character, String> keyword(String name) {
        return token(string(name));
    }
}
