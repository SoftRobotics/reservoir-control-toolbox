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

public class ParserErrorTest {

    private ParserError error;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnNull() {
        error = new ParserError(0, null);
    }

    @Test
    public void testConstructorOnNegativeIndex() {
        for (int i = -1000; i < 0; i++) {
            try {
                error = new ParserError(i, "hello");
                fail("no exception was thrown");
            } catch (IllegalArgumentException ex) {
            }
        }

        try {
            error = new ParserError(Integer.MIN_VALUE, "hello");
            fail("no exception was thrown");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testConstructorAndGetter() {
        String message = "my error message";
        error = new ParserError(5, message);

        assertEquals(5, error.getLineIndex());
        assertEquals(message, error.getMessage());
    }

}
