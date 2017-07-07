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

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReaderStateTest {
    @Test
    public void readOneByOne() throws Exception {
        String txt = "hello, world";
        ReaderState state = aTestState(txt);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < txt.length(); i++) {
            sb.append(state.next().current());
        }
        assertEquals(txt, sb.toString());
        assertTrue("not at end of stream", state.next().end());
    }

    @Test
    public void readAll() throws Exception {
        String txt = "hello, world";
        ReaderState state = aTestState(txt);
        assertFalse("not yet at the end", state.end());

        int count = txt.length();
        state.next(count);
        List<Character> chars = state.current(count);
        assertEquals(count, chars.size());
        assertTrue("not at end of stream", state.next().end());
    }

    @Test(expected = IllegalStateException.class)
    public void readIncomplete() throws Exception {
        String txt = "hello, world";
        ReaderState state = aTestState(txt);

        // read more than we have -- will stop at end-of-stream
        state.next(100);

        // confirm you can read the full string size
        int count = txt.length();
        assertEquals(count, state.current(count).size());
        assertTrue("should be at the end", state.end());
        assertEquals("at the end position is -1", -1, state.position());


        // this will blow up
        state.current(100);
    }

    @Test
    public void readEmpty() throws Exception {
        ReaderState state = aTestState("");
        assertTrue("not at end of stream", state.next().end());
        state.next(100);
        assertTrue("still not at end of stream", state.end());
    }

    @Test(expected = IllegalStateException.class)
    public void cannotReadAfterClose() throws Exception {
        ReaderState state = aTestState("oops");
        state.close();
        state.next();
    }

    private ReaderState aTestState(String txt) throws IOException {
        return new ReaderState(new StringReader(txt));
    }
}
