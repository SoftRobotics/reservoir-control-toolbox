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

import org.junit.Test;
import static org.junit.Assert.*;
import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType;
import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType.*;
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;
import static ch.zhaw.iamp.rct.graph.Mass.Type;
import org.junit.Before;

public class SpringTest {

    private final double DELTA = 0.0000001;
    private NetworkGraph graph;
    private Spring spring;
    private Mass mass1, mass2;

    @Before
    public void setUp() {
        graph = new NetworkGraph();
        mass1 = new Mass(0, 1);
        mass2 = new Mass(2, 3);

        spring = new Spring(graph, mass1, mass2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnNull() {
        spring = new Spring(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnGraph() {
        spring = new Spring(null, mass1, mass2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnSource() {
        spring = new Spring(graph, null, mass2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnDestination() {
        spring = new Spring(graph, mass1, null);
    }

    @Test
    public void testConstructor() {
        spring = new Spring(graph, mass1, mass2);

        assertSame(graph, spring.graph);
        assertSame(mass1, spring.source);
        assertSame(mass2, spring.destination);
        assertEquals(mass1.getX(), spring.line.getP1().getX(), DELTA);
        assertEquals(mass1.getY(), spring.line.getP1().getY(), DELTA);
        assertEquals(mass2.getX(), spring.line.getP2().getX(), DELTA);
        assertEquals(mass2.getY(), spring.line.getP2().getY(), DELTA);
        assertEquals(SPRING_CONSTRAINT, spring.connectionType);
    }

    @Test
    public void testConstructorOnTypes() {
        assertMassesToConstraint(0, 1, SHOULDER, 0, 1, ARM_SEGMENT, ROBOT_ARM_BASE_CONSTRAINT);
        assertMassesToConstraint(0, 1, SHOULDER, 0, 2, INPUT, FIXED_CONSTRAINT);
        assertMassesToConstraint(0, 2, INPUT, 3, 3, NETWORK, SPRING_CONSTRAINT);
        assertMassesToConstraint(3, 3, NETWORK, 4, 4, NETWORK, SPRING_CONSTRAINT);
        assertMassesToConstraint(4, 4, NETWORK, 0, 10, INPUT, SPRING_CONSTRAINT);
        assertMassesToConstraint(0, 10, INPUT, 0, 11, ELBOW, FIXED_CONSTRAINT);
        assertMassesToConstraint(0, 11, ARM_SEGMENT, 0, 12, INPUT, FIXED_CONSTRAINT);
        assertMassesToConstraint(0, 11, ARM_SEGMENT, 0, 11, ARM_SEGMENT, ROBOT_ARM_JOINT_CONSTRAINT);
        assertMassesToConstraint(0, 11, ELBOW, 0, 11, ARM_SEGMENT, FIXED_CONSTRAINT);
        assertMassesToConstraint(0, 11, ELBOW, 0, 11, INPUT, FIXED_CONSTRAINT);
        assertMassesToConstraint(0, 20, INPUT, 0, 21, HAND, FIXED_CONSTRAINT);
        assertMassesToConstraint(0, 21, ARM_SEGMENT, 0, 21, HAND, FIXED_CONSTRAINT);
    }

    private void assertMassesToConstraint(double x1, double y1, Type type1, double x2, double y2, Type type2, ConnectionType expectedSpringType) {
        mass1 = new Mass(x1, y1, type1);
        mass2 = new Mass(x2, y2, type2);
        spring = new Spring(graph, mass1, mass2);
        assertEquals(expectedSpringType, spring.connectionType);
        spring = new Spring(graph, mass2, mass1);
        assertEquals(expectedSpringType, spring.connectionType);
    }

    @Test
    public void testGetSource() {
        assertSame(mass1, spring.getSource());
    }

    @Test
    public void testGetDestination() {
        assertSame(mass2, spring.getDestination());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetConnectionTypeOnNull() {
        spring.setConnectionType(null);
    }

    @Test
    public void testSetConnectionType() {
        spring.connectionType = null;
        spring.setConnectionType(FIXED_CONSTRAINT);
        assertEquals(FIXED_CONSTRAINT, spring.connectionType);
    }

    @Test
    public void testGetConnectionType() {
        spring.connectionType = FIXED_CONSTRAINT;
        assertSame(FIXED_CONSTRAINT, spring.getConnectionType());
    }

    @Test
    public void testGetLine() {
        assertSame(spring.line, spring.getLine());
    }

    @Test
    public void testIsConnected() {
        assertFalse(spring.isConnected(null));
        assertFalse(spring.isConnected(new Mass(1, 2)));
        assertTrue(spring.isConnected(mass1));
        assertTrue(spring.isConnected(mass2));
    }

    @Test
    public void testIsCrossing() {
        Spring horizontalAndYequals1 = new Spring(graph, new Mass(-100, 1), new Mass(100, 1));
        Spring horizontalAndYequals2 = new Spring(graph, new Mass(-100, 2), new Mass(100, 2));
        Spring vertical = new Spring(graph, new Mass(1, 0), new Mass(1, 10));

        assertFalse(horizontalAndYequals1.isCrossing(horizontalAndYequals2));
        assertFalse(horizontalAndYequals2.isCrossing(horizontalAndYequals1));

        assertTrue(horizontalAndYequals1.isCrossing(vertical));
        assertTrue(vertical.isCrossing(horizontalAndYequals1));
        assertTrue(vertical.isCrossing(horizontalAndYequals2));
        assertTrue(vertical.isCrossing(vertical)); // connected at both ends
    }

    @Test
    public void testIsCrossingOnSameStartingPoint() {
        Spring spring1 = new Spring(graph, new Mass(0, 0), new Mass(1, 1));
        Spring spring2 = new Spring(graph, new Mass(0, 2), new Mass(1, 1));

        assertFalse(spring1.isCrossing(spring2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareToOnNull() {
        spring.compareTo(null);
    }

    @Test
    public void testCompareTo() {
        mass1 = new Mass(1, 1);
        mass2 = new Mass(2, 2);
        Mass mass3 = new Mass(3, 3);
        Mass mass4 = new Mass(4, 4);
        graph.addMasses(mass1, mass2, mass3, mass4);

        graph.addSpring(mass2, mass4); // => indices 3, 1
        graph.addSpring(mass1, mass2); // => indices 1, 0
        graph.addSpring(mass3, mass4); // => indices 3, 2
        graph.addSpring(mass1, mass3); // => indices 2, 1

        Spring spring0 = graph.getSprings().get(0);
        Spring spring1 = graph.getSprings().get(1);
        Spring spring2 = graph.getSprings().get(2);
        Spring spring3 = graph.getSprings().get(3);

        assertTrue(spring0.compareTo(spring1) > 0);
        assertTrue(spring1.compareTo(spring0) < 0);
        assertTrue(spring1.compareTo(spring3) < 0);
        assertTrue(spring3.compareTo(spring1) > 0);
        assertTrue(spring3.compareTo(spring2) < 0);
        assertTrue(spring0.compareTo(spring2) < 0);
        assertTrue(spring0.compareTo(spring3) > 0);
        assertTrue(spring2.compareTo(spring3) > 0);
        assertTrue(spring2.compareTo(spring0) > 0);
        assertTrue(spring0.compareTo(spring0) == 0);
    }

    @Test
    public void testGetMassWithHigherIndex() {
        mass1 = new Mass(1, 1);
        mass2 = new Mass(2, 2);
        graph.addMasses(mass1, mass2);

        graph.addSpring(mass1, mass2);
        assertSame(mass1, graph.getSprings().get(0).getMassWithLowerIndex());
        assertSame(mass2, graph.getSprings().get(0).getMassWithHigherIndex());
    }

    @Test
    public void testGetMassWithLowerIndex() {
        mass1 = new Mass(1, 1);
        mass2 = new Mass(2, 2);
        graph.addMasses(mass2, mass1);

        graph.addSpring(mass2, mass1);
        assertSame(mass1, graph.getSprings().get(0).getMassWithHigherIndex());
        assertSame(mass2, graph.getSprings().get(0).getMassWithLowerIndex());
    }
}
