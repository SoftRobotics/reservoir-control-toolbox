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

/**
 * A {@link ParserError} may be produced by a {@link Parser} when parsing text.
 * It stores a message and the position at which the error occurred.
 *
 * @see Parser
 * @see GrammarParser
 * @see InitialisationParser
 */
public class ParserError {

    private int lineIndex;
    private String message;

    /**
     * Creates a new parser error object.
     *
     * @param lineIndex The index of the parsed text at which the error
     * occurred.
     * @param message A message that describes the error.
     */
    public ParserError(int lineIndex, String message) {
        if (lineIndex < 0 || message == null) {
            throw new IllegalArgumentException("The line index has to be at least 0, the message may not be null.");
        }

        this.lineIndex = lineIndex;
        this.message = message;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public String getMessage() {
        return message;
    }

}
