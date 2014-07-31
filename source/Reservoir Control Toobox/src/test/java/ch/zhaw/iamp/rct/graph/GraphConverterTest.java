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

import java.io.File;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static ch.zhaw.iamp.rct.graph.GraphConverter.*;
import org.junit.Before;
import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType.*;
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;

public class GraphConverterTest {

    private final String FILENAME = "convertTest.csv";
    private final File FILE = new File(FILENAME);
    private final int X1 = 5, Y1 = 6, X2 = 11, Y2 = 12, X3 = 7, Y3 = 13, X4 = 8, Y4 = 14;
    private NetworkGraph graph;
    private String result;
    private Mass mass1, mass2, mass3, mass4;

    @Before
    public void setUp() {
        mass1 = new Mass(X1, Y1);
        mass2 = new Mass(X2, Y2);
        mass3 = new Mass(X3, Y3);
        mass4 = new Mass(X4, Y4);
        graph = new NetworkGraph();
    }

    @After
    public void cleanUp() {
        FILE.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToMassesCsvOnNull() {
        GraphConverter.toMassesCsv(null);
    }

    @Test
    public void testToMassesCsvOnEmptyGraph() {
        result = GraphConverter.toMassesCsv(graph);
        assertEquals(MASSES_PREAMBLE, result);
    }

    @Test
    public void testToMassesCsvOnSingleMass() {
        graph.addMasses(mass1);

        result = GraphConverter.toMassesCsv(graph);
        assertEquals(MASSES_PREAMBLE
                + "t,5.0,6.0,0\n", result);
    }

    @Test
    public void testToMassesCsvOnTwoIndependantMasses() {
        graph.addMasses(mass1, mass2);

        result = GraphConverter.toMassesCsv(graph);
        assertEquals(MASSES_PREAMBLE
                + "t,5.0,6.0,0\n"
                + "t,11.0,12.0,0\n", result);
    }

    @Test
    public void testToMassesCsvOnTwoConnectedMasses() {
        graph.addMasses(mass1, mass2);
        graph.addSpring(mass1, mass2);

        result = GraphConverter.toMassesCsv(graph);
        assertEquals(MASSES_PREAMBLE
                + "t,5.0,6.0,0\n"
                + "t,11.0,12.0,0,0\n", result);
    }

    @Test
    public void testToMassesCsvOnDifferentTypes() {
        mass1.setType(SHOULDER);
        mass2.setType(ARM_SEGMENT);
        mass3.setType(HAND);
        mass4.setType(INPUT);
        graph.addMasses(mass1, mass2, mass3, mass4);

        result = GraphConverter.toMassesCsv(graph);
        assertEquals(MASSES_PREAMBLE
                + "f,5.0,6.0,0\n"
                + "r,11.0,12.0,0\n"
                + "e,7.0,13.0,0\n"
                + "i,8.0,14.0,0\n", result);
    }

    @Test
    public void testGetConnectionsToEarlierConfiguredMassesOnSingleMass() {
        graph.addMasses(mass1);

        assertEquals("", GraphConverter.getConnectionsToPriorMasses(mass1, graph));
    }

    @Test
    public void testGetConnectionsToPriorMassesOnConnectedMasses() {
        graph.addMasses(mass1, mass2);
        graph.addSpring(mass2, mass1);

        assertEquals("", GraphConverter.getConnectionsToPriorMasses(mass1, graph));
        assertEquals(",0", GraphConverter.getConnectionsToPriorMasses(mass2, graph));
    }

    @Test
    public void testGetConnectionsToPriorMassesOnWrongAraundConnectedMasses() {
        graph.addMasses(mass1, mass2);
        graph.addSpring(mass1, mass2); // This is actually the 'worng way round' but this has also to work 

        assertEquals("", GraphConverter.getConnectionsToPriorMasses(mass1, graph));
        assertEquals(",0", GraphConverter.getConnectionsToPriorMasses(mass2, graph));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToConnectionMapCsvOnNull() {
        GraphConverter.toConnectionMapCsv(null);
    }

    @Test
    public void testToConnectionMapCsvOnEmptyGraph() {
        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE, result);
    }

    @Test
    public void testToConnectionMapCsvOnSingleMass() {
        graph.addMasses(mass1);

        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE, result);
    }

    @Test
    public void testToConnectionMapCsvOnSortingOrder() {
        Mass mass5 = new Mass(X1, X1);
        Mass mass6 = new Mass(X1, X1);
        Mass mass7 = new Mass(X1, X1);
        Mass mass8 = new Mass(X1, X1);
        graph.addMasses(mass1, mass2, mass3, mass4, mass5, mass6, mass7, mass8);

        // mass 4 has higher index value => has to be first in connectin map in 
        // this spring entry, but on the second line since mass index 3 > 1 of
        // the below added spring
        graph.addSpring(mass4, mass3);

        // mass 2 has higher index value => has to be first in connectin map in 
        // this spring entry, but on the 1st line since mass index 1 < rest
        graph.addSpring(mass1, mass2);

        // mass 8 has higher index value => has to be first in connectin map in 
        // this spring entry, but on the 3rd line since the order of 1st and 2nd
        // column
        graph.addSpring(mass1, mass8);

        // mass 6 has higher index value => has to be first in connectio map in 
        // this entry and on the 3rd line.
        graph.addSpring(mass5, mass6);

        // mass 8 has higher index value => has to be first in connectio map in 
        // this entry and on the 4th line.
        graph.addSpring(mass8, mass7);

        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE
                + "1,0,1\n"
                + "3,2,1\n"
                + "5,4,1\n"
                + "7,0,1\n"
                + "7,6,1\n", result);
    }

    @Test
    public void testToConnectionMapCsvOnTwoConnectedMasses() {
        graph.addMasses(mass1, mass2);
        graph.addSpring(mass1, mass2);

        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE + "1,0,1\n", result);
    }

    @Test
    public void testToConnectionMapCsvOnMoreConnectedMasses() {
        graph.addMasses(mass1, mass2, mass3, mass4);
        graph.addSpring(mass1, mass2);
        graph.addSpring(mass2, mass3);
        graph.addSpring(mass4, mass3); // mass4 -> mass3 | Important since this tests if
        graph.addSpring(mass4, mass2); // mass4 -> mass2 | the springs are sorted first!
        graph.getSprings().get(0).setConnectionType(FIXED_CONSTRAINT);

        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE
                + "1,0," + FIXED_CONSTRAINT + "\n"
                + "2,1,1\n"
                + "3,1,1\n" // here, it has to be in acending order
                + "3,2,1\n",
                result);
    }

    @Test
    public void testToConnectionMapCsvOnCompleteArm() {
        Mass shoulder = new Mass(0, 1, SHOULDER);
        Mass upperArm = new Mass(0, 1, ARM_SEGMENT);
        Mass shoulderInput = new Mass(0, 2, INPUT);
        Mass elbowUpperArmInput = new Mass(0, 10, INPUT);
        Mass elbow = new Mass(0, 11, ELBOW);
        Mass lowerArm = new Mass(0, 11, ARM_SEGMENT);
        Mass elbowLowerArmInput = new Mass(0, 12, INPUT);
        Mass handInput = new Mass(0, 20, INPUT);
        Mass hand = new Mass(0, 21, HAND);

        graph.addMasses(shoulder); // index 0
        graph.addMasses(upperArm); // index 1
        graph.addMasses(shoulderInput); // index 2
        graph.addMasses(elbowUpperArmInput); // index 3
        graph.addMasses(elbow); // index 4
        graph.addMasses(lowerArm); // index 5
        graph.addMasses(elbowLowerArmInput); // index 6
        graph.addMasses(handInput); // index 7
        graph.addMasses(hand); // index 8

        graph.addSpring(shoulder, upperArm);
        graph.addSpring(upperArm, shoulderInput);
        graph.addSpring(upperArm, elbowUpperArmInput);
        graph.addSpring(upperArm, lowerArm);
        graph.addSpring(lowerArm, elbowLowerArmInput);
        graph.addSpring(lowerArm, handInput);
        graph.addSpring(lowerArm, hand);

        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE
                + "1,0,6\n"
                + "2,1,5\n"
                + "3,1,5\n"
                + "5,1,7\n"
                + "6,5,5\n"
                + "7,5,5\n"
                + "8,5,5\n",
                result);

        // Reversed springs
        graph.springs.clear();

        graph.addSpring(upperArm, shoulder);
        graph.addSpring(shoulderInput, upperArm);
        graph.addSpring(elbowUpperArmInput, upperArm);
        graph.addSpring(lowerArm, upperArm);
        graph.addSpring(elbowLowerArmInput, lowerArm);
        graph.addSpring(handInput, lowerArm);
        graph.addSpring(hand, lowerArm);

        result = GraphConverter.toConnectionMapCsv(graph);
        assertEquals(CONNECTION_MAP_PREAMBLE
                + "1,0,6\n"
                + "2,1,5\n"
                + "3,1,5\n"
                + "5,1,7\n"
                + "6,5,5\n"
                + "7,5,5\n"
                + "8,5,5\n",
                result);
    }

}
