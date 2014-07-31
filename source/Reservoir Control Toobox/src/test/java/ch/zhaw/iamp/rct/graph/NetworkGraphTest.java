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
package ch.zhaw.iamp.rct.graph;

import static ch.zhaw.iamp.rct.graph.Mass.Type.ARM_SEGMENT;
import static ch.zhaw.iamp.rct.graph.Mass.Type.ELBOW;
import static ch.zhaw.iamp.rct.graph.Mass.Type.HAND;
import static ch.zhaw.iamp.rct.graph.Mass.Type.INPUT;
import static ch.zhaw.iamp.rct.graph.Mass.Type.NETWORK;
import static ch.zhaw.iamp.rct.graph.Mass.Type.SHOULDER;
import ch.zhaw.iamp.rct.grammar.Range;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NetworkGraphTest {

    private boolean excludeCrossings = true;
    private NetworkGraph graph;
    private Mass mass1, mass2, mass3, mass4;

    @Before
    public void setUp() {
        graph = new NetworkGraph();
        mass1 = new Mass(0, 1);
        mass2 = new Mass(2, 3);
        mass3 = new Mass(4, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMassesOnNull() {
        graph.addMasses(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMassesOnNullMass() {
        graph.addMasses(mass1, null, mass3);
    }

    @Test
    public void testAddMasses() {
        assertEquals(0, graph.masses.size());
        graph.addMasses(mass1);
        assertEquals(1, graph.masses.size());
        assertSame(mass1, graph.masses.get(0));
    }

    @Test
    public void testAddMassesOnMasses() {
        assertEquals(0, graph.masses.size());
        graph.addMasses(mass1, mass2, mass3);
        assertEquals(3, graph.masses.size());
        assertSame(mass1, graph.masses.get(0));
        assertSame(mass2, graph.masses.get(1));
        assertSame(mass3, graph.masses.get(2));
    }

    @Test
    public void testAddMassesOnMassesArray() {
        Mass[] masses = new Mass[]{mass1, mass2, mass3};

        assertEquals(0, graph.masses.size());
        graph.addMasses(masses);
        assertEquals(3, graph.masses.size());
        assertSame(mass1, graph.masses.get(0));
        assertSame(mass2, graph.masses.get(1));
        assertSame(mass3, graph.masses.get(2));
    }

    @Test
    public void testGetMasses() {
        assertTrue(graph.getMasses().isEmpty());
        graph.masses.add(mass1);
        assertSame(mass1, graph.getMasses().get(0));
    }

    @Test
    public void testGetInputNodesOnNoInputNodes() {
        assertTrue(graph.getInputs().isEmpty());
    }

    @Test
    public void testGetInputNodes() {
        mass1 = new Mass(1, 2, INPUT);
        mass2 = new Mass(3, 4);
        graph.addMasses(mass1, mass2);

        assertEquals(1, graph.getInputs().size());
        assertSame(mass1, graph.getInputs().get(0));
    }

    @Test
    public void testGetShoulder() {
        Mass input = new Mass(4, 4, INPUT);
        Mass shoulder = new Mass(2, 8, SHOULDER);

        assertNull(graph.getShoulder());
        graph.addMasses(input, shoulder);
        assertSame(shoulder, graph.getShoulder());
    }

    @Test
    public void testGetElbow() {
        Mass input = new Mass(4, 4, INPUT);
        Mass elbow = new Mass(2, 8, ELBOW);

        assertNull(graph.getElbow());
        graph.addMasses(input, elbow);
        assertSame(elbow, graph.getElbow());
    }

    @Test
    public void testGetHand() {
        Mass input = new Mass(4, 4, INPUT);
        Mass hand = new Mass(2, 8, HAND);

        assertNull(graph.getHand());
        graph.addMasses(input, hand);
        assertSame(hand, graph.getHand());
    }

    @Test
    public void testFindReachableMassesAndInputsOnEmptyList() {
        mass1 = new Mass(1, 2);
        graph.addMasses(mass1);

        assertTrue(graph.findReachableMassesAndInputs(mass1, new Range(10, 10), excludeCrossings).isEmpty());
    }

    @Test
    public void testFindReachableMassesAndInputsOnIgnoringUnwantedTypes() {
        mass1 = new Mass(0, 0); // center
        mass2 = new Mass(2, 0); // distance = 2
        graph.addMasses(mass1, mass2);
        Range range = new Range(1, 2);

        for (Mass.Type type : Mass.Type.values()) {
            mass2.setType(type);

            if (type == INPUT || type == NETWORK) {
                assertEquals(1, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).size());
                assertSame(mass2, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).get(0));
            } else {
                assertTrue(graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).isEmpty());
            }
        }
    }

    @Test
    public void testFindReachableMassesAndInputsOnCrossingSprings() {
        Range range = new Range(0, 10);
        mass1 = new Mass(-100, 1);
        mass2 = new Mass(100, 1);
        mass3 = new Mass(0, 0);
        mass4 = new Mass(0, 2);
        graph.addMasses(mass1, mass2, mass3, mass4);

        List<Mass> reachableMasses = graph.findReachableMassesAndInputs(mass3, range, excludeCrossings);
        assertEquals(1, reachableMasses.size());
        assertSame(mass4, reachableMasses.get(0));

        graph.addSpring(mass1, mass2);
        assertEquals(0, graph.findReachableMassesAndInputs(mass3, range, excludeCrossings).size());
        excludeCrossings = false;
        assertEquals(1, graph.findReachableMassesAndInputs(mass3, range, excludeCrossings).size());
        assertSame(mass4, reachableMasses.get(0));
    }

    @Test
    public void testFindReachableMassesAndInputsOnConnectingToInputs() {
        Range range = new Range(0, 10);
        mass1 = new Mass(0, 0, INPUT);
        mass2 = new Mass(0, 10, INPUT);
        mass3 = new Mass(5, 5);

        graph.addMasses(mass1, mass2, mass3);

        List<Mass> reachableMasses = graph.findReachableMassesAndInputs(mass3, range, excludeCrossings);
        assertEquals(2, reachableMasses.size());
        assertSame(mass1, reachableMasses.get(0));
        assertSame(mass2, reachableMasses.get(1));
    }

    @Test
    public void testFindReachableMassesAndInputsOnConnectingFromInputs() {
        Range range = new Range(0, 10);
        mass1 = new Mass(0, 0, INPUT);
        mass2 = new Mass(0, 3, INPUT);
        mass3 = new Mass(5, 5);

        graph.addMasses(mass1, mass2, mass3);

        List<Mass> reachableMasses = graph.findReachableMassesAndInputs(mass1, range, excludeCrossings);
        assertEquals(2, reachableMasses.size());
        assertSame(mass2, reachableMasses.get(0));
        assertSame(mass3, reachableMasses.get(1));
    }

    @Test
    public void testFindReachableMassesAndInputsOnConnectingFromNetworkSpring() {
        Range range = new Range(0, 10);
        mass1 = new Mass(0, 0, INPUT);
        mass2 = new Mass(0, 10, INPUT);
        mass3 = new Mass(5, 5);
        mass4 = new Mass(6, 5);

        graph.addMasses(mass1, mass2, mass3, mass4);
        graph.addSpring(mass1, mass2);
        graph.addSpring(mass3, mass4);

        List<Mass> reachableMasses = graph.findReachableMassesAndInputs(mass3, range, excludeCrossings);
        assertEquals(2, reachableMasses.size());
        assertSame(mass1, reachableMasses.get(0));
        assertSame(mass2, reachableMasses.get(1));

        reachableMasses = graph.findReachableMassesAndInputs(mass1, range, excludeCrossings);
        assertEquals(2, reachableMasses.size());
        assertSame(mass3, reachableMasses.get(0));
        assertSame(mass4, reachableMasses.get(1));

        reachableMasses = graph.findReachableMassesAndInputs(mass2, range, excludeCrossings);
        assertEquals(2, reachableMasses.size());
        assertSame(mass3, reachableMasses.get(0));
        assertSame(mass4, reachableMasses.get(1));
    }

    @Test
    public void testFindReachableMassesAndInputsOnInputNodes() {
        Range range = new Range(0, 10);
        mass1 = new Mass(0, 1, INPUT);
        mass2 = new Mass(1, 1);
        graph.addMasses(mass1, mass2);

        List<Mass> reachableMasses = graph.findReachableMassesAndInputs(mass2, range, excludeCrossings);
        assertEquals(1, reachableMasses.size());
        assertSame(mass1, reachableMasses.get(0));
    }

    @Test
    public void testFindReachableMassesAndInputs() {
        mass1 = new Mass(0, 0); // center
        mass2 = new Mass(2, 0); // distance = 2
        mass3 = new Mass(2, 2); // distance = 2.8284271247461900976 (sqrt(2^2 + 2^2))
        graph.addMasses(mass1, mass2, mass3);

        Range range = new Range(0, 2);
        assertEquals(1, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).size());
        assertSame(mass2, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).get(0));

        range = new Range(2.6, 2.8);
        assertEquals(0, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).size());

        range = new Range(2.8, 2.9);
        assertEquals(1, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).size());
        assertSame(mass3, graph.findReachableMassesAndInputs(mass1, range, excludeCrossings).get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpringOnNulls() {
        graph.addSpring(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpringOnNullSource() {
        graph.addSpring(null, mass2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSpringOnNullDestination() {
        graph.addSpring(mass1, null);
    }

    @Test
    public void testAddSpring() {
        assertEquals(0, graph.springs.size());
        graph.addSpring(mass1, mass2);
        assertEquals(1, graph.springs.size());
    }

    @Test
    public void testGetSprings() {
        assertTrue(graph.getSprings().isEmpty());
        graph.addSpring(mass1, mass2);
        assertSame(graph.springs, graph.getSprings());
    }

    @Test
    public void testGetSpringsByMass() {
        assertTrue(graph.getSprings(mass1).isEmpty());

        graph.addSpring(mass1, mass2);
        List<Spring> springs = graph.getSprings(mass1);

        assertEquals(1, springs.size());
        assertSame(mass1, springs.get(0).getSource());
        assertSame(mass2, springs.get(0).getDestination());

        graph.addSpring(mass1, mass3);
        springs = graph.getSprings(mass1);

        assertEquals(2, springs.size());
        assertSame(mass1, springs.get(0).getSource());
        assertSame(mass2, springs.get(0).getDestination());
        assertSame(mass1, springs.get(1).getSource());
        assertSame(mass3, springs.get(1).getDestination());
    }

    @Test
    public void testRemoveNotConnectedNetworkMasses() {
        mass1 = new Mass(1, 2);
        mass2 = new Mass(3, 4);
        mass3 = new Mass(5, 6);
        mass4 = new Mass(5, 6);
        mass4.setType(HAND);

        graph.addMasses(mass1, mass2, mass3, mass4);
        graph.addSpring(mass1, mass2);

        graph.removeNotConnectedNetworkMasses();
        assertEquals(3, graph.getMasses().size());
        assertSame(mass1, graph.getMasses().get(0));
        assertSame(mass2, graph.getMasses().get(1));
        assertSame(mass4, graph.getMasses().get(2));
    }

    @Test
    public void testRemoveMassSpringNetwork() {
        mass1 = new Mass(1, 2);
        mass2 = new Mass(3, 4);
        mass3 = new Mass(5, 6);
        mass4 = new Mass(5, 6, SHOULDER);
        Mass mass5 = new Mass(5, 6, ELBOW);
        Mass mass6 = new Mass(5, 6, HAND);
        Mass mass7 = new Mass(5, 6, ARM_SEGMENT);
        Mass mass8 = new Mass(5, 6, ARM_SEGMENT);
        Mass mass9 = new Mass(5, 6, INPUT);

        graph.addMasses(mass1, mass2, mass3, mass4, mass5, mass6, mass7, mass8, mass9);

        // removable 
        graph.addSpring(mass1, mass2);

        // not removable
        graph.addSpring(mass4, mass7);
        graph.addSpring(mass7, mass5);
        graph.addSpring(mass5, mass8);
        graph.addSpring(mass8, mass6);
        graph.addSpring(mass4, mass9);

        Spring spring1 = graph.getSprings().get(1);
        Spring spring2 = graph.getSprings().get(2);
        Spring spring3 = graph.getSprings().get(3);
        Spring spring4 = graph.getSprings().get(4);
        Spring spring5 = graph.getSprings().get(5);

        graph.removeMassSpringNetwork();
        assertEquals(6, graph.getMasses().size());
        assertSame(mass4, graph.getMasses().get(0));
        assertSame(mass5, graph.getMasses().get(1));
        assertSame(mass6, graph.getMasses().get(2));
        assertSame(mass7, graph.getMasses().get(3));
        assertSame(mass8, graph.getMasses().get(4));
        assertSame(mass9, graph.getMasses().get(5));

        assertEquals(5, graph.getSprings().size());
        assertSame(spring1, graph.getSprings().get(0));
        assertSame(spring2, graph.getSprings().get(1));
        assertSame(spring3, graph.getSprings().get(2));
        assertSame(spring4, graph.getSprings().get(3));
        assertSame(spring5, graph.getSprings().get(4));
    }

    @Test
    public void testRemoveMassSpringNetworkOnSourceAndDestinationMass() {
        mass1 = new Mass(1, 2, INPUT);
        mass2 = new Mass(1, 2, INPUT);
        mass3 = new Mass(3, 4);
        mass4 = new Mass(5, 6);

        graph.addMasses(mass1, mass2, mass3, mass4);

        // not removable
        graph.addSpring(mass1, mass2);
        graph.addSpring(mass2, mass1);

        // removable 
        graph.addSpring(mass1, mass3);
        graph.addSpring(mass4, mass2);
        graph.addSpring(mass2, mass3);
        graph.addSpring(mass3, mass2);

        Spring spring1 = graph.getSprings().get(0);
        Spring spring2 = graph.getSprings().get(1);
        graph.getSprings().get(2);
        graph.getSprings().get(3);
        graph.getSprings().get(4);
        graph.getSprings().get(5);

        graph.removeMassSpringNetwork();
        assertEquals(2, graph.getMasses().size());
        assertSame(mass1, graph.getMasses().get(0));
        assertSame(mass2, graph.getMasses().get(1));

        assertEquals(2, graph.getSprings().size());
        assertSame(spring1, graph.getSprings().get(0));
        assertSame(spring2, graph.getSprings().get(1));
    }

    @Test
    public void testReset() {
        LinkedList<Mass> originalMasses = graph.masses;
        LinkedList<Spring> originalSprings = graph.springs;

        graph.reset();

        assertFalse(graph.masses == originalMasses);
        assertFalse(graph.springs == originalSprings);
        assertTrue(graph.masses.isEmpty());
        assertTrue(graph.springs.isEmpty());
    }

}
