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
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static ch.zhaw.iamp.rct.grammar.GrammarParser.Keyword.*;
import static ch.zhaw.iamp.rct.grammar.GrammarParser.Option.showNotConnectedMasses;
import ch.zhaw.iamp.rct.graph.Spring;
import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType;
import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType.*;
import org.junit.Ignore;

public class GrammarParserTest {

    private final double DELTA = 0.00001;
    private GrammarParser parser;

    @Before
    public void setUp() {
        parser = new GrammarParser();
    }

    @Test
    public void testConstructor() {
        assertNotNull(parser.inputs);
        assertNotNull(parser.productionRules);
        assertNotNull(parser.massCreations);
        assertNotNull(parser.springCreations);
        assertNotNull(parser.graph);
    }

    @Test
    public void testParseOnNull() {
        parser.parse(null);
        assertNoErrors();
    }

    @Test
    public void testGetMassesBetweenYRange() {
        Mass input1 = new Mass(0, 2, INPUT);
        Mass input2 = new Mass(0, 10, INPUT);
        Mass input3 = new Mass(0, 12, INPUT);
        Mass input4 = new Mass(0, 20, INPUT);
        parser.inputs.add(input1);
        parser.inputs.add(input2);
        parser.inputs.add(input3);
        parser.inputs.add(input4);

        List<Mass> inputs = parser.getMassesBetweenYRange(1, 11);
        assertEquals(2, inputs.size());
        assertSame(input1, inputs.get(0));
        assertSame(input2, inputs.get(1));

        inputs = parser.getMassesBetweenYRange(12, 20);
        assertEquals(2, inputs.size());
        assertSame(input3, inputs.get(0));
        assertSame(input4, inputs.get(1));
    }

    @Test
    public void testGetKeywordOfLine() {
        assertEquals(expansionRangeX, GrammarParser.getKeywordOfLine("expansionRangeX [23, 24]"));
        assertEquals(productionRules, GrammarParser.getKeywordOfLine("productionRules A->a"));
        assertEquals(null, GrammarParser.getKeywordOfLine("hand 23"));
    }

    @Test
    public void testGetProductionRules() {
        assertSame(parser.productionRules, parser.getProductionRules());
        parser.productionRules = null;
        assertNull(parser.getProductionRules());
    }

    @Test
    public void testGetProductionRule() {
        assertNull(parser.getProductionRule("A"));
        Rule rule = new Rule("A", "a");
        parser.productionRules.add(rule);
        assertSame(rule, parser.getProductionRule("A"));
    }

    @Test
    public void testGetMassCreations() {
        assertSame(parser.massCreations, parser.getMassCreations());
        parser.massCreations = null;
        assertNull(parser.getMassCreations());
    }

    @Test
    public void testGetMassCreationRange() {
        assertNull(parser.getMassCreationRange("Not existing"));
        Range range = new Range(DELTA, DELTA);
        parser.massCreations.put("A", range);
        assertSame(range, parser.getMassCreationRange("A"));
    }

    @Test
    public void testIsMassCration() {
        assertFalse(parser.isMassCreation("Not existing"));
        Range range = new Range(DELTA, DELTA);
        parser.massCreations.put("A", range);
        assertTrue(parser.isMassCreation("A"));
    }

    @Test
    public void testGetSpringCreations() {
        assertSame(parser.springCreations, parser.getSpringCreations());
        parser.springCreations = null;
        assertNull(parser.getSpringCreations());
    }

    @Test
    public void testGetSpringCreationRange() {
        assertNull(parser.getSpringCreationRange("Not existing"));
        Range range = new Range(DELTA, DELTA);
        parser.springCreations.put("A", range);
        assertSame(range, parser.getSpringCreationRange("A"));
    }

    @Test
    public void testIsSpringCration() {
        assertFalse(parser.isSpringCreation("Not existing"));
        Range range = new Range(DELTA, DELTA);
        parser.springCreations.put("A", range);
        assertTrue(parser.isSpringCreation("A"));
    }

    @Test
    public void testGetOptions() {
        assertSame(parser.options, parser.getOptions());
        parser.options = null;
        assertNull(parser.getOptions());
    }

    @Test
    public void testGetGraph() {
        assertSame(parser.graph, parser.getGraph());
        parser.graph = null;
        assertNull(parser.getGraph());
    }

    @Test
    public void testGetExpansionRangeX() {
        assertNotNull(parser.expansionRangeX);
        assertSame(GrammarParser.DEFAULT_EXPANSION_RANGE_X, parser.getExpansionRangeX());
        parser.expansionRangeX = null;
        assertNull(parser.getExpansionRangeX());
    }

    @Test
    public void testParseRule() {
        List<Rule> rules = GrammarParser.parseProductionRule("productionRules x -> y");
        assertEquals(1, rules.size());
        assertEquals("x", rules.get(0).getSearch());
        assertEquals("y", rules.get(0).getReplace());

        rules = GrammarParser.parseProductionRule("productionRules x -> y a->b");
        assertEquals(2, rules.size());
        assertEquals("x", rules.get(0).getSearch());
        assertEquals("y", rules.get(0).getReplace());
        assertEquals("a", rules.get(1).getSearch());
        assertEquals("b", rules.get(1).getReplace());
    }

    @Test
    public void testOnErrors() {
        assertFalse(parser.hasErrors());
        parser.parse("var e = f \n x - > 234\n  y-->hello");
        assertTrue(parser.hasErrors());
        assertEquals(3, parser.getErrors().size());
    }

    @Test
    public void testParseMassesOnNoMasses() {
        List<Mass> masses = GrammarParser.parseMasses("");
        assertTrue(masses.isEmpty());

        masses = GrammarParser.parseMasses("hand");
        assertTrue(masses.isEmpty());
    }

    @Test
    public void testParseMassesOnOneMass() {
        List<Mass> masses = GrammarParser.parseMasses("hand 5.2 -6.4");
        assertEquals(1, masses.size());
        assertEquals(5.2, masses.get(0).getX(), DELTA);
        assertEquals(-6.4, masses.get(0).getY(), DELTA);
    }

    @Test
    public void testParseMassesOnTwoMasses() {
        List<Mass> masses = GrammarParser.parseMasses("hand 5 6 -7.1 -90");
        assertEquals(2, masses.size());
        assertEquals(5, masses.get(0).getX(), DELTA);
        assertEquals(6, masses.get(0).getY(), DELTA);
        assertEquals(-7.1, masses.get(1).getX(), DELTA);
        assertEquals(-90, masses.get(1).getY(), DELTA);
    }

    @Test
    public void testParseOnMassTypesAndSprings() {
        parser.parse("shoulder 0 1\n"
                + "elbow 0 11\n"
                + "hand 0 21");
        List<Mass> masses = parser.graph.getMasses();
        assertMass(masses.get(0), 0, 1, SHOULDER);
        assertMass(masses.get(1), 0, 11, ELBOW);
        assertMass(masses.get(2), 0, 21, HAND);
        assertEquals(3, masses.size());

        List<Spring> springs = parser.graph.getSprings();
        assertEquals(0, springs.size());
    }

    @Test
    public void testParseOnSpringsToInputs() {
        parser.parse("shoulder 0 1\n"
                + "input 0 2\n"
                + "input 0 10\n"
                + "elbow 0 11\n"
                + "input 0 12\n"
                + "input 0 20\n"
                + "hand 0 21");
        List<Mass> masses = parser.graph.getMasses();
        assertMass(masses.get(0), 0, 1, SHOULDER);
        assertMass(masses.get(1), 0, 2, INPUT);
        assertMass(masses.get(2), 0, 10, INPUT);
        assertMass(masses.get(3), 0, 11, ELBOW);
        assertMass(masses.get(4), 0, 12, INPUT);
        assertMass(masses.get(5), 0, 20, INPUT);
        assertMass(masses.get(6), 0, 21, HAND);
        assertMass(masses.get(7), 0, 1, ARM_SEGMENT);
        assertMass(masses.get(8), 0, 11, ARM_SEGMENT);
        assertEquals(9, masses.size());

        List<Spring> springs = parser.graph.getSprings();
        assertSpring(springs.get(0), masses.get(0), masses.get(7), ROBOT_ARM_BASE_CONSTRAINT);
        assertSpring(springs.get(1), masses.get(3), masses.get(7), FIXED_CONSTRAINT);
        assertSpring(springs.get(2), masses.get(1), masses.get(7), FIXED_CONSTRAINT);
        assertSpring(springs.get(3), masses.get(2), masses.get(7), FIXED_CONSTRAINT);
        assertSpring(springs.get(4), masses.get(3), masses.get(8), FIXED_CONSTRAINT);
        assertSpring(springs.get(5), masses.get(6), masses.get(8), FIXED_CONSTRAINT);
        assertSpring(springs.get(6), masses.get(4), masses.get(8), FIXED_CONSTRAINT);
        assertSpring(springs.get(7), masses.get(5), masses.get(8), FIXED_CONSTRAINT);
        assertSpring(springs.get(8), masses.get(7), masses.get(8), ROBOT_ARM_JOINT_CONSTRAINT);
        assertEquals(9, springs.size());
    }

    private void assertMass(Mass mass, double x, double y, Type type) {
        assertEquals("Mass type = " + type, x, mass.getX(), DELTA);
        assertEquals("Mass type = " + type, y, mass.getY(), DELTA);
        assertEquals(type, mass.getType());
    }

    private void assertSpring(Spring spring, Mass source, Mass destination, ConnectionType type) {
        assertSame(source, spring.getSource());
        assertSame(destination, spring.getDestination());
        assertEquals(type, spring.getConnectionType());
    }

    @Ignore
    @Test
    public void testParseOnExpansionRange() {
        parser.parse("expansionRangeX [-10.1, 4.3]");
        assertEquals(-10.1, parser.expansionRangeX.getMin(), DELTA);
        assertEquals(4.3, parser.expansionRangeX.getMax(), DELTA);
    }

    @Test
    public void testParseInteger() {
        assertEquals(42, GrammarParser.parseInteger("maxMasses 42"));
        assertEquals(-42, GrammarParser.parseInteger("maxMasses -42"));
        assertEquals(0, GrammarParser.parseInteger("maxMasses 0"));
    }

    @Test
    public void testParseLettersOnNoLetters() {
        Set<String> set = GrammarParser.parseLetters("");
        assertTrue(set.isEmpty());
    }

    @Test
    public void testParseLetters() {
        Set<String> set = GrammarParser.parseLetters("nonTerminals A B C d word");
        assertEquals(5, set.size());
        assertTrue(set.contains("A"));
        assertTrue(set.contains("B"));
        assertTrue(set.contains("C"));
        assertTrue(set.contains("d"));
        assertTrue(set.contains("word"));
        assertFalse(set.contains("a"));
        assertFalse(set.contains("b"));
        assertFalse(set.contains("c"));
        assertFalse(set.contains("D"));
    }

    private void assertNoErrors() {
        assertFalse(parser.hasErrors());
        assertTrue(parser.getErrors().isEmpty());
    }

    @Test
    public void testParseCreateRange() {
        assertEquals(4.2, GrammarParser.parseCreateRange("createMass B [4.2, 4.4]").getMin(), DELTA);
        assertEquals(4.4, GrammarParser.parseCreateRange("createMass B [4.2, 4.4]").getMax(), DELTA);

        assertEquals(4.3, GrammarParser.parseCreateRange("createSpring B [4.3, 4.5]").getMin(), DELTA);
        assertEquals(4.5, GrammarParser.parseCreateRange("createSpring B [4.3, 4.5]").getMax(), DELTA);
    }

    @Test
    public void testParseExpansionRange() {
        assertEquals(4.2, GrammarParser.parseExpansionRange("expansionRangeX [4.2, 4.4]").getMin(), DELTA);
        assertEquals(4.4, GrammarParser.parseExpansionRange("expansionRangeX [4.2, 4.4]").getMax(), DELTA);
    }

    @Test
    public void testParseCreateLetter() {
        assertEquals("A", GrammarParser.parseCreateLetter("createMass A [4.2, 4.4]"));
        assertEquals("B", GrammarParser.parseCreateLetter("createMass B [4.2, 4.4]"));
    }

    @Test
    public void testParseOptions() {
        assertEquals(1, GrammarParser.parseOptions("options showNotConnectedMasses two three").size());
        assertEquals(showNotConnectedMasses, GrammarParser.parseOptions("options showNotConnectedMasses two three").get(0));
    }

}
