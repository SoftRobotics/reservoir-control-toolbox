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

public class ParserTest {

    private class TestParser extends Parser {

        @Override
        public void parse(String text) {
        }

    }
    private TestParser parser;

    @Before
    public void setUp() {
        parser = new TestParser();
    }

    @Test
    public void testConstructor() {
        assertNotNull(parser.errors);
    }

    @Test
    public void testGetErrors() {
        assertSame(parser.errors, parser.getErrors());
    }

    @Test
    public void testHasErrors() {
        assertFalse(parser.hasErrors());
        parser.errors.add(new ParserError(0, "not really an error"));
        assertTrue(parser.hasErrors());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Parser.isEmpty(null));
        assertTrue(Parser.isEmpty(""));
        assertTrue(Parser.isEmpty("  "));
        assertTrue(Parser.isEmpty("\n"));
        assertTrue(Parser.isEmpty("\t"));
        assertFalse(Parser.isEmpty(" a"));
        assertFalse(Parser.isEmpty("  0  "));
        assertFalse(Parser.isEmpty("  hi  "));
        assertFalse(Parser.isEmpty("this is a test"));
    }

    @Test
    public void testIsComment() {
        assertFalse(Parser.isComment(null));
        assertFalse(Parser.isComment(""));
        assertFalse(Parser.isComment("  "));
        assertTrue(Parser.isComment("#\n"));
        assertTrue(Parser.isComment("   # \t"));
        assertTrue(Parser.isComment(" #a"));
        assertTrue(Parser.isComment(" ### 0  "));
        assertTrue(Parser.isComment(" #$ hi  "));
        assertTrue(Parser.isComment("#this is a test"));
    }

}
