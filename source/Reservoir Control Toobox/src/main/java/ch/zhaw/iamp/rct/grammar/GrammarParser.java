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

import ch.zhaw.iamp.rct.graph.Mass;
import static ch.zhaw.iamp.rct.graph.Mass.Type;
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;
import ch.zhaw.iamp.rct.graph.NetworkGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser parses the grammar text, entered in the upper left text area in
 * the Grammar Window.
 */
public class GrammarParser extends Parser {

    /**
     * A keyword is used to specify what attribute is configured in the
     * initialization area.
     * <p>
     * For example: {@code shoulder 2 3}, where {@code shoulder} is the keyword
     * and {@code 2 3} are the values to be parsed.
     */
    public enum Keyword {

        shoulder,
        elbow,
        hand,
        input,
        expansionRangeX,
        createMass,
        createSpring,
        options,
        productionRules,
        randomMasses,
        randomSprings;
    }

    /**
     * Options are like {@link Keyword}s, but accept only boolean values
     * ({@code true} and {@code false}).
     */
    public enum Option {

        showNotConnectedMasses,
        excludeSpringCrossings,
        allowNegativeYValues;
    }

    public static final Range DEFAULT_EXPANSION_RANGE_X = new Range(-1000, 1000);
    private static final String BASIC_DEFINITION_PATTERN = "[ \t]*DEF([ \t]+((-?[0-9]+(\\.[0-9]*)*)|([a-zA-Z]+)))+[ \t]*";
    private static final String BASIC_NUMBER_PAIR_DEFINITION_PATTERN = "[ \t]*DEF([ \t]+-?[0-9]+(\\.[0-9]*)?[ \t]+-?[0-9]+(\\.[0-9]*)?)+[ \t]*";
    private static final String BASIC_CREATION_PATTERN = "[ \t]*DEF[ \t]+([a-zA-Z])[ \t]+\\[[ \t]*(-?[0-9]+(\\.[0-9]*)?)[ \t]*,[ \t]*(-?[0-9]+(\\.[0-9]*)?)[ \t]*\\][ \t]*";
    private static final String RANGE_PATTERN = "[ \t]*DEF[ \t]+\\[[ \t]*(-?[0-9]+(\\.[0-9]*)?)[ \t]*,[ \t]*(-?[0-9]+(\\.[0-9]*)?)[ \t]*\\][ \t]*";
    private static final String OPTIONS_PATTERN = "[ \t]*options(([ \t]+[a-zA-Z]+)+)[ \t]*";
    private static final Pattern LETTER_PATTERN = Pattern.compile("([ \t]*[a-zA-Z][ \t]+)(([ \t]*[a-zA-Z])*)[ \t]*");
    private static final Pattern COORDINATES_PATTERN = Pattern.compile("[ \t]*[a-zA-Z0-9]*([ \t]*-?[0-9]+\\.?[0-9]*[ \t]*-?[0-9]+\\.?[0-9]*)+[ \t]*[- \t_a-zA-Z0-9]*");
    private static final Pattern PRODUCTION_RULE_PATTERN = Pattern.compile("[ \t]*productionRules(([ \t]+([_a-zA-Z]+[_0-9a-zA-Z]*)[ \t]*->[ \t]*([-_a-zA-Z0-9]+))+)[ \t]*");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("[ \t]*[a-zA-Z0-9]*[ \t]*(-?[0-9]+)[ \t]*[- \t_a-zA-Z0-9]*");
    private final static HashMap<Keyword, Pattern> keywordPatterns = new HashMap<>();
    NetworkGraph graph = new NetworkGraph();
    Mass shoulder, elbow, hand, upperArmSegment, lowerArmSegment;
    List<Mass> inputs = new LinkedList<>();
    List<Rule> productionRules = new LinkedList<>();
    Map<String, Range> massCreations = new HashMap<>();
    Map<String, Range> springCreations = new HashMap<>();
    List<Option> options = new LinkedList<>();
    Range expansionRangeX = DEFAULT_EXPANSION_RANGE_X;
    int numberOfRandomMasses = 0;
    int numberOfRandomSprings = 0;

    static {
        prepareKeywordPatterns();
    }

    private static void prepareKeywordPatterns() {
        for (Keyword keyword : Keyword.values()) {
            String pattern = BASIC_DEFINITION_PATTERN;

            switch (keyword) {
                case shoulder:
                case elbow:
                case hand:
                case input:
                    pattern = BASIC_NUMBER_PAIR_DEFINITION_PATTERN;
                    break;
                case createMass:
                case createSpring:
                    pattern = BASIC_CREATION_PATTERN;
                    break;
                case productionRules:
                    pattern = PRODUCTION_RULE_PATTERN.toString();
                    break;
                case options:
                    pattern = OPTIONS_PATTERN;
                    break;
                case expansionRangeX:
                    pattern = RANGE_PATTERN;
                    break;
            }

            keywordPatterns.put(keyword, Pattern.compile(pattern.replace("DEF", keyword.toString())));
        }
    }

    @Override
    public void parse(String text) {
        errors = new LinkedList<>();
        graph = new NetworkGraph();
        inputs = new LinkedList<>();
        productionRules = new LinkedList<>();
        massCreations = new HashMap<>();
        springCreations = new HashMap<>();
        numberOfRandomMasses = 0;
        numberOfRandomSprings = 0;
        options = new LinkedList<>();
        expansionRangeX = DEFAULT_EXPANSION_RANGE_X;

        if (text == null) {
            return;
        }

        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (isEmpty(lines[i]) || isComment(lines[i])) {
                continue;
            }

            if (isKeywordLine(lines[i])) {
                switch (getKeywordOfLine(lines[i])) {
                    case shoulder:
                        shoulder = parseMasses(lines[i]).get(0);
                        shoulder.setType(SHOULDER);
                        graph.addMasses(shoulder);
                        break;
                    case elbow:
                        elbow = parseMasses(lines[i]).get(0);
                        elbow.setType(ELBOW);
                        graph.addMasses(elbow);
                        break;
                    case hand:
                        hand = parseMasses(lines[i]).get(0);
                        hand.setType(HAND);
                        graph.addMasses(hand);
                        break;
                    case input:
                        Mass input = parseMasses(lines[i]).get(0);
                        input.setType(INPUT);
                        inputs.add(input);
                        graph.addMasses(input);
                        break;
                    case productionRules:
                        productionRules.addAll(parseProductionRule(lines[i]));
                        break;
                    case expansionRangeX:
                        expansionRangeX = parseExpansionRange(lines[i]);
                        break;
                    case createMass:
                        String massLetter = parseCreateLetter(lines[i]);
                        Range createMassRange = parseCreateRange(lines[i]);
                        massCreations.put(massLetter, createMassRange);
                        break;
                    case createSpring:
                        String springLetter = parseCreateLetter(lines[i]);
                        Range createSpringRange = parseCreateRange(lines[i]);
                        springCreations.put(springLetter, createSpringRange);
                        break;
                    case randomMasses:
                        numberOfRandomMasses = parseInteger(lines[i]);
                        break;
                    case randomSprings:
                        numberOfRandomSprings = parseInteger(lines[i]);
                        break;
                    case options:
                        options.addAll(parseOptions(lines[i]));
                        break;
                }

                continue;
            }

            errors.add(new ParserError(i, "Cannot understand this line."));
        }

        createAndConnectSegmentsToInputs();
    }

    private boolean isKeywordLine(String s) {
        for (Keyword keyword : Keyword.values()) {
            if (s.matches(keywordPatterns.get(keyword).pattern())) {
                return true;
            }
        }

        return false;
    }

    private void createAndConnectSegmentsToInputs() {
        upperArmSegment = null;

        if (shoulder != null && elbow != null) {
            List<Mass> upperArmInputs = getMassesBetweenYRange(shoulder.getY(), elbow.getY());

            if (upperArmInputs.size() == 2) {
                upperArmSegment = new Mass(shoulder.getX(), shoulder.getY(), ARM_SEGMENT);
                graph.addMasses(upperArmSegment);
                graph.addSpring(shoulder, upperArmSegment);
                graph.addSpring(elbow, upperArmSegment);
                graph.addSpring(upperArmInputs.get(0), upperArmSegment);
                graph.addSpring(upperArmInputs.get(1), upperArmSegment);
            }
        }

        lowerArmSegment = null;

        if (elbow != null && hand != null) {
            List<Mass> lowerArmInputs = getMassesBetweenYRange(elbow.getY(), hand.getY());

            if (lowerArmInputs.size() == 2) {
                lowerArmSegment = new Mass(elbow.getX(), elbow.getY(), ARM_SEGMENT);
                graph.addMasses(lowerArmSegment);
                graph.addSpring(elbow, lowerArmSegment);
                graph.addSpring(hand, lowerArmSegment);
                graph.addSpring(lowerArmInputs.get(0), lowerArmSegment);
                graph.addSpring(lowerArmInputs.get(1), lowerArmSegment);
            }
        }

        if (upperArmSegment != null && lowerArmSegment != null) {
            graph.addSpring(upperArmSegment, lowerArmSegment);
        }
    }

    /**
     * Gets the inputs within the given Y coordinates.
     *
     * @return A list of inputs. If no such input can be found, an empty list is
     * returned.
     */
    List<Mass> getMassesBetweenYRange(double yStart, double yEnd) {
        List<Mass> inputsBetween = new LinkedList<>();

        for (Mass input : inputs) {
            if (input.getY() >= yStart && input.getY() <= yEnd) {
                inputsBetween.add(input);
            }
        }

        return inputsBetween;
    }

    static Keyword getKeywordOfLine(String s) {
        for (Keyword keyword : Keyword.values()) {
            if (s.matches(keywordPatterns.get(keyword).pattern())) {
                return keyword;
            }
        }

        return null;
    }

    public List<Rule> getProductionRules() {
        return productionRules;
    }

    public Rule getProductionRule(String search) {
        for (Rule rule : productionRules) {
            if (rule.search.equals(search)) {
                return rule;
            }
        }

        return null;
    }

    public Map<String, Range> getMassCreations() {
        return massCreations;
    }

    public Range getMassCreationRange(String letter) {
        return massCreations.get(letter);
    }

    public boolean isMassCreation(String letter) {
        return massCreations.containsKey(letter);
    }

    public Map<String, Range> getSpringCreations() {
        return springCreations;
    }

    public Range getSpringCreationRange(String letter) {
        return springCreations.get(letter);
    }

    public boolean isSpringCreation(String letter) {
        return springCreations.containsKey(letter);
    }

    public int getNumberOfRandomMasses() {
        return numberOfRandomMasses;
    }

    public int getNumberOfRandomSprings() {
        return numberOfRandomSprings;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setGraph(NetworkGraph graph) {
        this.graph = graph;
    }

    public NetworkGraph getGraph() {
        return graph;
    }

    public Range getExpansionRangeX() {
        return expansionRangeX;
    }

    static List<Mass> parseMasses(String line) {
        Matcher matcher = COORDINATES_PATTERN.matcher(line);
        ArrayList<Mass> masses = new ArrayList<>();

        if (!matcher.find()) {
            return masses;
        }

        Type typeOfMass = getTypeForKeyword(getKeywordOfLine(line));
        String[] coordinates = matcher.group(0).split("[ \t]");

        for (int i = 1; i < coordinates.length; i += 2) {
            String x = coordinates[i];
            String y = coordinates[i + 1];
            masses.add(new Mass(Double.parseDouble(x), Double.parseDouble(y), typeOfMass));
        }

        return masses;
    }

    private static Type getTypeForKeyword(Keyword keyword) {
        for (Type type : Type.values()) {
            if (type.toString().equalsIgnoreCase(keyword.toString())) {
                return type;
            }
        }

        throw new IllegalStateException("Could not find a mass type for the "
                + "keyword " + keyword + ", but this should never happen.");
    }

    static int parseInteger(String line) {
        Matcher matcher = INTEGER_PATTERN.matcher(line);

        matcher.find();
        String number = matcher.group(1);

        return Integer.parseInt(number);
    }

    static List<Rule> parseProductionRule(String line) {
        Matcher matcher = PRODUCTION_RULE_PATTERN.matcher(line);
        LinkedList<Rule> rules = new LinkedList<>();

        matcher.find();
        String rulePart = matcher.group(1).replaceAll("[ \t]+->[ \t]", "->");
        String[] rulePairs = rulePart.split("[ \t]+");

        for (String rule : rulePairs) {
            if (rule.trim().length() > 0) {
                String search = rule.split("->")[0];
                String replace = rule.split("->")[1];
                rules.add(new Rule(search, replace));
            }
        }

        return rules;
    }

    static Set<String> parseLetters(String line) {
        Matcher matcher = LETTER_PATTERN.matcher(line);
        Set<String> set = new LinkedHashSet<>();

        if (!matcher.find()) {
            return set;
        }

        String[] letters = matcher.group(2).split("[ \t]");

        for (String letter : letters) {
            String candidate = letter.trim();

            if (candidate.length() > 0) {
                set.add(candidate);
            }
        }

        return set;
    }

    static Range parseCreateRange(String line) {
        Keyword keyword = getKeywordOfLine(line);
        Matcher matcher = keywordPatterns.get(keyword).matcher(line);

        matcher.find();
        double min = Double.parseDouble(matcher.group(2));
        double max = Double.parseDouble(matcher.group(4));

        return new Range(min, max);
    }

    static Range parseExpansionRange(String line) {
        Keyword keyword = getKeywordOfLine(line);
        Matcher matcher = keywordPatterns.get(keyword).matcher(line);

        matcher.find();
        double min = Double.parseDouble(matcher.group(1));
        double max = Double.parseDouble(matcher.group(3));

        return new Range(min, max);
    }

    static String parseCreateLetter(String line) {
        Keyword keyword = getKeywordOfLine(line);
        Matcher matcher = keywordPatterns.get(keyword).matcher(line);

        matcher.find();
        return matcher.group(1);
    }

    static List<Option> parseOptions(String line) {
        Keyword keyword = getKeywordOfLine(line);
        Matcher matcher = keywordPatterns.get(keyword).matcher(line);
        List<Option> options = new LinkedList<>();

        matcher.find();
        String optionString = matcher.group(1);

        for (String candidate : optionString.split("[ \t]+")) {
            candidate = candidate.trim();

            try {
                options.add(Option.valueOf(candidate));
            } catch (IllegalArgumentException ex) {
                System.out.println("Did not understand option: " + ex.getMessage());
            }
        }

        return options;
    }

    public boolean allowNegativeYValuesIsSet() {
        return options.contains(Option.allowNegativeYValues);
    }

}
