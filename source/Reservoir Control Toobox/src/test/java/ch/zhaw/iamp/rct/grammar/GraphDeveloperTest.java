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
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;
import ch.zhaw.iamp.rct.graph.NetworkGraph;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class GraphDeveloperTest {

    private final double DELTA = 0.00001;
    private NetworkGraph graph;
    private Mass shoulder, inputShoulder, elbow, inputElbow1, inputElbow2, hand, inputHand;
    private GrammarParser grammar;
    private String developedInit;
    private GraphDeveloper developer;
    private Range expansionRangeX;

    @Before
    public void setUp() {
        grammar = new GrammarParser();
        expansionRangeX = new Range(-1000, 1000);
        grammar.expansionRangeX = expansionRangeX;
        graph = grammar.getGraph();
        developedInit = "AaBbCc";
        developer = new GraphDeveloper(grammar, developedInit);

        setUpMasses();
    }

    private void setUpMasses() {
        shoulder = new Mass(0, 0);
        inputShoulder = new Mass(1, 0);
        inputElbow1 = new Mass(4, 0);
        elbow = new Mass(5, 0);
        inputElbow2 = new Mass(6, 0);
        inputHand = new Mass(9, 0);
        hand = new Mass(10, 0);

        shoulder.setType(SHOULDER);
        inputShoulder.setType(INPUT);
        inputElbow1.setType(INPUT);
        elbow.setType(ELBOW);
        inputElbow2.setType(INPUT);
        inputHand.setType(INPUT);
        hand.setType(HAND);

        graph.addMasses(shoulder);
        graph.addMasses(inputShoulder);
        graph.addMasses(inputElbow1);
        graph.addMasses(elbow);
        graph.addMasses(inputElbow2);
        graph.addMasses(inputHand);
        graph.addMasses(hand);
    }

    @Test
    public void testConstructor() {
        assertSame(grammar, developer.grammar);
        assertEquals(developedInit, developer.developedInit);
        assertNotNull(developer.errors);
    }

    @Test
    public void testGetNumberInRange() {
        Range range = new Range(3, 4);
        double number = GraphDeveloper.getNumberInRange(range);
        assertTrue(number >= 3 && number <= 4);

        range = new Range(4, 4);
        number = GraphDeveloper.getNumberInRange(range);
        assertEquals(4, number, 0.00000001);
    }

    @Test
    public void testCreateMass() {
        for (int i = -50; i < 50; i++) {
            double min = (i < 0 ? 4 : 2) * i;
            double max = (i < 0 ? 2 : 4) * i;
            grammar.getMassCreations().put("A", new Range(min, max));
            developer.currentPoint = shoulder;
            Mass created = developer.createMass("A");
            assertTrue((created.getX() >= min && created.getX() <= max)
                    || (created.getX() <= -min && created.getX() >= -max));
            assertTrue((created.getY() >= min && created.getY() <= max)
                    || (created.getY() <= -min && created.getY() >= -max));
        }
    }

    @Test
    public void testCreateMassOnExpansionRangeX() {
        Range unlimitedRange = new Range(-500, 500);
        grammar.getMassCreations().put("A", unlimitedRange);
        developer.currentPoint = new Mass(0, 0, SHOULDER);

        int minExpansionRange = -5;
        int maxExpansionRange = 5;

        for (; minExpansionRange < maxExpansionRange; minExpansionRange++, maxExpansionRange--) {
            developer.grammar.expansionRangeX = new Range(minExpansionRange, maxExpansionRange);
            Mass created = developer.createMass("A");
            System.out.println("min: " + minExpansionRange);
            System.out.println("max: " + maxExpansionRange);
            System.out.println("created: " + created.getX());

            assertTrue(created.getX() >= minExpansionRange && created.getX() <= maxExpansionRange);
        }
    }

    @Test
    public void testCreateSpringOnConnectionToSameMass() {
        Mass mass1 = new Mass(50, 50);
        Mass mass2 = new Mass(51, 51);

        grammar.getGraph().addMasses(mass1, mass2);
        grammar.getGraph().addSpring(mass1, mass2);
        grammar.getSpringCreations().put("A", new Range(0, 4));
        developer.currentPoint = mass1;

        assertEquals(1, developer.grammar.getGraph().getSprings().size());
        developer.createSpring("A");
        assertEquals(1, developer.grammar.getGraph().getSprings().size());
    }

    @Test
    public void testGetTighterRange() {
        Range range1 = new Range(-5, 5);
        Range range2 = new Range(-2, 2);
        Range result = GraphDeveloper.getTigtherRange(range1, range2);
        assertEquals(-2, result.getMin(), DELTA);
        assertEquals(2, result.getMax(), DELTA);

        range1 = new Range(-5, 5);
        range2 = new Range(-2, 10);
        result = GraphDeveloper.getTigtherRange(range1, range2);
        assertEquals(-2, result.getMin(), DELTA);
        assertEquals(5, result.getMax(), DELTA);

        range1 = new Range(-500, 500);
        range2 = new Range(-2, 10);
        result = GraphDeveloper.getTigtherRange(range1, range2);
        assertEquals(-2, result.getMin(), DELTA);
        assertEquals(10, result.getMax(), DELTA);

        range1 = new Range(-2, 10);
        range2 = new Range(-500, 500);
        result = GraphDeveloper.getTigtherRange(range1, range2);
        assertEquals(-2, result.getMin(), DELTA);
        assertEquals(10, result.getMax(), DELTA);
    }

    @Test
    public void testGetErrors() {
        assertSame(developer.errors, developer.getErrors());
    }

}
