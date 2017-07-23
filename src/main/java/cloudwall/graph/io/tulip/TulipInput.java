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

import cloudwall.graph.io.ReaderInput;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

/**
 * Helper class that implements basic line comments for Tulip in parsecj.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@SuppressWarnings("WeakerAccess")
public class TulipInput extends ReaderInput {
    private static final Pattern COMMENT_PATTERN = Pattern.compile("\\s*;.*");

    public TulipInput(Reader r) throws IOException {
        super(r);
    }

    public TulipInput(String txt) throws IOException {
        super(txt);
    }

    @Override
    public boolean excludeLine(String line) {
        return COMMENT_PATTERN.matcher(line).matches();
    }
}
