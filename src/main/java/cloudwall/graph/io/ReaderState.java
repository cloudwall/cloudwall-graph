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
package cloudwall.graph.io;

import org.javafp.parsecj.State;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for ParsecJ input which makes a GraphInput look like a State object.
 * Unlike the default classes this 
 *
 * @author <a href="mailto:kyle.downey@gmail.com">Kyle F. Downey</a>
 */
@SuppressWarnings("WeakerAccess")
public class ReaderState implements State<Character>, Closeable {
    private Reader reader;
    private CharBuffer buffer = CharBuffer.allocate(1024);
    private List<Character> bufferList = new ArrayList<>();
    private int position;

    public ReaderState(@Nonnull Reader reader) throws IOException {
        this.reader = reader;
        this.position = 0;
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public boolean end() {
        return position < 0;
    }

    @Override
    public Character current() {
        return bufferList.get(0);
    }

    @Override
    public List<Character> current(int n) {
        if (n > bufferList.size()) {
            throw new IllegalStateException("asking for a current # of entries > amount read");
        }
        return bufferList;
    }

    @Override
    public State<Character> next(int n) {
        buffer.position(0);
        buffer.limit(n);
        bufferList.clear();
        try {
            while (true) {
                buffer.mark();
                int numRead = reader.read(buffer);
                if (numRead == -1) {
                    this.position = -1;

                    // done if we hit end of stream, regardless of bufferSize;
                    break;
                } else {
                    this.position += numRead;
                    buffer.rewind();
                    int start = buffer.position();
                    for (int i = 0; i < numRead; i++) {
                        bufferList.add(buffer.get(start + i));
                    }

                    // done reading if we have all n chars required
                    if (bufferList.size() == n) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return this;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
