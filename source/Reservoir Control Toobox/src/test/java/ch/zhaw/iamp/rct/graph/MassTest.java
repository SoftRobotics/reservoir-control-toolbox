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
import org.junit.Before;
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;

public class MassTest {

    private final double DELTA = 0.000001;
    private final double X = 12.23;
    private final double Y = 564.32;
    private Mass mass;
    private NetworkGraph graph;

    @Before
    public void setUp() {
        mass = new Mass(X, Y);
        graph = new NetworkGraph();
    }

    @Test
    public void testConstructor() {
        assertEquals(X, mass.x, DELTA);
        assertEquals(Y, mass.y, DELTA);
        assertEquals(NETWORK, mass.type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeConstructorOnNull() {
        mass = new Mass(X, Y, null);
    }

    @Test
    public void testTypeConstructor() {
        mass = new Mass(X, Y, HAND);
        assertEquals(X, mass.x, DELTA);
        assertEquals(Y, mass.y, DELTA);
        assertEquals(HAND, mass.type);
    }

    @Test
    public void testGetX() {
        assertEquals(X, mass.getX(), DELTA);
    }

    @Test
    public void testGetY() {
        assertEquals(Y, mass.getY(), DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetIndexOnNull() {
        mass.getIndex(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexOnNotUsedGraph() {
        mass.getIndex(graph);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTypeOnNull() {
        mass.setType(null);
    }

    @Test
    public void testSetType() {
        mass.type = null;
        mass.setType(ELBOW);
        assertEquals(ELBOW, mass.type);
    }

    @Test
    public void testGetType() {
        assertEquals(mass.type, mass.getType());
        mass.type = ELBOW;
        assertEquals(ELBOW, mass.getType());
    }

    @Test
    public void testGetIndex() {
        graph.addMasses(mass);
        assertEquals(0, mass.getIndex(graph));

        graph.addMasses(new Mass(X + 1, Y + 1));
        assertEquals(0, mass.getIndex(graph)); // should not affect this

        graph.getMasses().add(0, new Mass(X + 2, Y + 2));
        assertEquals(1, mass.getIndex(graph)); // should affect this

    }

    @Test
    public void testCalculateDistance() {
        mass = new Mass(1, 1);
        Mass other = new Mass(2, 1);
        assertEquals(1d, mass.calculateDistance(other), DELTA);

        other = new Mass(0, 1);
        assertEquals(1d, mass.calculateDistance(other), DELTA);

        other = new Mass(0, 0);
        assertEquals(Math.sqrt(2d), mass.calculateDistance(other), DELTA);
    }

    @Test
    public void testIsConnected() {
        Mass other = new Mass(X, X);
        assertFalse(mass.isConnected(graph, other));

        graph.addMasses(mass);
        assertFalse(mass.isConnected(graph, other));

        graph.addMasses(other);
        assertFalse(mass.isConnected(graph, other));

        graph.addSpring(mass, other);
        assertTrue(mass.isConnected(graph, other));
    }

}
