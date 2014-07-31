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
 * A rule defines how a letter should be replaced when an initialization string
 * evolves.
 *
 * @see GrammarParser
 * @see GraphDeveloper
 */
public class Rule {

    final String search;
    final String replace;

    /**
     * Creates a new replacement rule.
     * <p>
     * Example: {@code A->B}
     *
     * @param search The string that is looked for. In the example above, the
     * search string is {@code A}.
     * @param replace The string that is used as replacement. In the example
     * above, the replace string is {@code B}.
     */
    public Rule(String search, String replace) {
        this.search = search;
        this.replace = replace;
    }

    /**
     * @return The search string. In {@code A->B}, this would be {@code A}.
     */
    public String getSearch() {
        return search;
    }

    /**
     * @return The replacement string. In {@code A->B}, this would be {@code B}.
     */
    public String getReplace() {
        return replace;
    }

}
