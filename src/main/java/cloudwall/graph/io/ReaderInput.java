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

import org.javafp.parsecj.State;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

/**
 * Helper class that wraps around a Reader and translates it to a parsecj State object.
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
public class ReaderInput {
    private StringBuilder scannedText = new StringBuilder();

    public ReaderInput(Reader r) throws IOException {
        Scanner scanner = new Scanner(r);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!excludeLine(line)) {
                scannedText.append(line);
            }
        }
    }

    public ReaderInput(String txt) throws IOException {
        this(new StringReader(txt));
    }

    public State<Character> toInput() {
        return State.of(scannedText.toString());
    }

    public boolean excludeLine(String line) {
        return false;
    }
}
