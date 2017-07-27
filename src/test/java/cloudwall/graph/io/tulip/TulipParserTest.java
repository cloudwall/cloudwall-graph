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

import com.google.common.collect.ImmutableList;
import org.javafp.data.IList;
import org.javafp.data.Unit;
import org.javafp.parsecj.Combinators;
import org.javafp.parsecj.Parser;
import org.javafp.parsecj.State;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TulipParserTest {
    @Test
    public void parseTestCasesFromTulip() throws Exception {
        Collection<String> testCases = ImmutableList.of(
                "grid1010.tlp",
                "k33k55.tlp",
                "openmetanode1.tlp",
                "planar30drawnFPP.tlp",
                "planar30drawnMM.tlp",
                "unbiconnected.tlp",
                "unconnected.tlp"
        );

        for (String testCase : testCases) {
            Reader r = new InputStreamReader(getClass().getResourceAsStream(testCase));
            TulipInput in = new TulipInput(r);
            TulipParser.newInstance().parse(in.toInput()).getResult();
        }
    }
}
