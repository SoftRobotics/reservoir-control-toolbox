/*
 * Copyright © 2014 René Bernhardsgrütter, Christoph Walter Senn at Zurich
 * University of Applied Sciences; 2014 Helmut Hauser at University of Zurich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ch.zhaw.iamp.rct.grammar;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class InitialisationParserTest {

    private GrammarParser grammar;
    private InitialisationParser parser;

    @Before
    public void setUp() {
        grammar = new GrammarParser();
        grammar.productionRules.add(new Rule("A", "a"));
        grammar.productionRules.add(new Rule("B", "bb"));
        grammar.productionRules.add(new Rule("C", "BB"));
        grammar.productionRules.add(new Rule("D", "DD"));
        parser = new InitialisationParser(grammar);
    }

    @Test
    public void testParseOnEmptyText() {
        parser.parse("");
        assertEquals("", parser.getOutputString());
    }

    @Test
    public void testParseOneRound() {
        parser.parse("ABC");
        assertEquals("abbBB", parser.getOutputString());
    }

    @Test
    public void testParseLoop() {
        parser.parse("A(2){BC}ABbaBB");
        assertEquals("abbbbbbabbbabbbb", parser.getOutputString());

        grammar = new GrammarParser();
        grammar.productionRules.add(new Rule("A", "a"));
        grammar.productionRules.add(new Rule("B", "BB"));
        parser = new InitialisationParser(grammar);
        parser.parse("ab(3){B}aB");
        assertEquals("abBBBBBBBBaBB", parser.getOutputString());
    }

    @Test
    public void testParseOnTooManyLoops() {
        parser.parse("a(50){DD}");
        assertEquals(InitialisationParser.TOO_LONG_OUTPUT_TEXT, parser.getOutputString());
    }

}
