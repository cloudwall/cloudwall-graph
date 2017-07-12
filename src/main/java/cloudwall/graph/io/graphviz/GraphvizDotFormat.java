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

import cloudwall.graph.io.GraphFormat;
import cloudwall.graph.io.GraphFormatException;
import com.google.common.collect.ImmutableList;
import org.javafp.parsecj.Parser;

import javax.activation.DataSource;
import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.util.function.Consumer;

import static org.javafp.parsecj.Combinators.retn;
import static org.javafp.parsecj.Text.string;
import static org.javafp.parsecj.Text.wspaces;

/**
 * Reader for the Graphviz DOT format.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 * @see <a href="http://www.graphviz.org/content/dot-language">DOT grammar</a>
 */
@SuppressWarnings("unchecked")
public class GraphvizDotFormat implements GraphFormat<GraphvizDotModel> {
    private static final MimeType[] CONTENT_TYPES;
    static {
        try {
            CONTENT_TYPES = new MimeType[] { new MimeType("text/vnd.graphviz") };
        } catch (MimeTypeParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> Parser<Character, T> token(Parser<Character, T> p) {
        return p.bind(x -> wspaces.then(retn(x)));
    }

    private static Parser<Character, String> keyword(String name) {
        return token(string(name));
    }

    private static final Parser<Character, String> graphToken = keyword("graph");
    private static final Parser<Character, String> nodeToken = keyword("node");
    private static final Parser<Character, String> edgeToken = keyword("edge");

    private static final Parser<Character, String> directedEdgeOp = token(string("->"));
    private static final Parser<Character, String> undirectedEdgeOp = token(string("--"));

    @Override
    public MimeType[] getSupportedContentTypes() {
        return CONTENT_TYPES;
    }

    @Override
    public void read(DataSource dataIn, Consumer<GraphvizDotModel> modelConsumer) throws GraphFormatException, IOException {

    }

    @Override
    public void write(DataSource dataOut, GraphvizDotModel model) {

    }

}
