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

import ch.zhaw.iamp.rct.ui.GrammarWindow;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser parses the initialization string, entered in the lower left text
 * area in the {@link GrammarWindow}.
 */
public class InitialisationParser extends Parser {

    final static String TOO_LONG_OUTPUT_TEXT = "The output gets too long!";
    final static long TEXT_LENGTH_LIMIT = 1000;
    final static String SYMBOL_REGEX = "[a-zA-Z].*";
    final static String LOOP_START_REGEX = "\\(([0-9]+)\\)\\{.*";
    final static String LOOP_END_REGEX = "\\}.*";
    final static Pattern LOOP_NUMBER_PATTERN = Pattern.compile(LOOP_START_REGEX);
    final static Random RANDOM = new SecureRandom();
    GrammarParser grammar;
    StringBuilder builder;
    boolean endParsing;

    public InitialisationParser(GrammarParser grammar) {
        this.grammar = grammar;
    }

    @Override
    public void parse(String text) {
        errors = new LinkedList<>();
        builder = new StringBuilder(text);
        endParsing = false;
        parse(grammar, builder, 0);
    }

    int parse(GrammarParser grammar, StringBuilder text, int offset) {
        while (offset < text.length()) {
            if (endParsing || text.length() > TEXT_LENGTH_LIMIT) {
                endParsing = true;
                text.delete(0, builder.length());
                text.append("The output gets too long!");
                return -1;
            }

            if (isSymbol(text, offset)) {
                String replacement = getReplacement(grammar, text.charAt(offset));
                text.replace(offset, offset + 1, replacement);
                offset += replacement.length();
                continue;
            }

            if (isLoopStart(text, offset)) {
                int loopNumber = getLoopNumber(text, offset);
                int loopStartBlockLength = getLoopStartBlockLength(text, offset);
                text.replace(offset, offset + loopStartBlockLength, "");
                int loopStartIndex = offset;

                for (int i = 0; i < loopNumber; i++) {
                    offset = parse(grammar, text, loopStartIndex);
                }

                if (endParsing) {
                    return -1;
                }

                text.replace(offset, offset + 1, "");

                continue;
            }

            if (isLoopEnd(text, offset)) {
                return offset;
            }

            errors.add(new ParserError(0, "Could not understand the symbol: " + text.charAt(offset)));
            offset++;
        }

        return offset;
    }

    public String getOutputString() {
        return builder.toString();
    }

    static boolean isSymbol(StringBuilder text, int offset) {
        return text.substring(offset).matches(SYMBOL_REGEX);
    }

    static String getReplacement(GrammarParser grammar, char c) {
        Rule rule = grammar.getProductionRule(String.valueOf(c));
        return rule == null ? String.valueOf(c) : rule.replace;
    }

    static boolean isLoopStart(StringBuilder text, int offset) {
        return text.substring(offset).matches(LOOP_START_REGEX);
    }

    static boolean isLoopEnd(StringBuilder text, int offset) {
        return text.substring(offset).matches(LOOP_END_REGEX);
    }

    static int getLoopNumber(StringBuilder text, int offset) {
        Matcher matcher = LOOP_NUMBER_PATTERN.matcher(text);
        matcher.find();

        return Integer.valueOf(matcher.group(1));
    }

    static int getLoopStartBlockLength(StringBuilder text, int offset) {
        return text.substring(offset).indexOf('{') + 1;
    }

}
