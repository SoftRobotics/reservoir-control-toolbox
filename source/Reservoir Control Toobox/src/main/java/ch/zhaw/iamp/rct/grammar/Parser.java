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

import java.util.LinkedList;
import java.util.List;

/**
 * A {@link Parser} takes a given text and parses it. After the parsing, the
 * processed text may result in read values.
 *
 * @see GrammarParser
 * @see InitialisationParser
 */
public abstract class Parser {

    List<ParserError> errors = new LinkedList<>();

    /**
     * Parses the given text.
     *
     * @param text The text to parse.
     */
    abstract public void parse(String text);

    /**
     * @return true, when the last parsing run produced {@link ParserError}s,
     * false otherwise.
     */
    final public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * @return a list of {@link ParserError}s. If there are no errors, an empty
     * list will be returned.
     */
    final public List<ParserError> getErrors() {
        return errors;
    }

    static boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    static boolean isComment(String s) {
        return s != null && s.trim().length() > 0 && s.trim().charAt(0) == '#';
    }

}
