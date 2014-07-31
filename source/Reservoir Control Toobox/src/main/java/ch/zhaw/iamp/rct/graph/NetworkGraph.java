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

import ch.zhaw.iamp.rct.grammar.Range;
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A {@link NetworkGraph} represents a robot arm with a mass-spring network
 * attached to it.
 *
 * @see Mass
 * @see Spring
 */
public class NetworkGraph {

    LinkedList<Mass> masses = new LinkedList<>();
    LinkedList<Spring> springs = new LinkedList<>();

    /**
     * Adds the given masses to the graph. They are added in the order how they
     * are given.
     *
     * @param masses At least one mass to add
     * @throws IllegalArgumentException If at least one argument is null.
     */
    public void addMasses(Mass... masses) {
        if (masses == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        for (Mass mass : masses) {
            if (mass == null) {
                throw new IllegalArgumentException("The argument may not contain nulls.");
            }
        }

        this.masses.addAll(Arrays.asList(masses));
    }

    /**
     * @return A list of all masses, including shoulder, elbow, hand.
     */
    public List<Mass> getMasses() {
        return masses;
    }

    /**
     * @return A list of all masses of the network.
     */
    public List<Mass> getNetworkMasses() {
        List<Mass> network = new LinkedList<>();

        for (Mass mass : masses) {
            if (mass.getType() == NETWORK) {
                network.add(mass);
            }
        }

        return network;
    }

    /**
     * Gets the input nodes ordered by the distance between the shoulder
     * (ascending). The first element of the result is the input node nearest to
     * the shoulder, the second is second nearest, and so forth.
     * <p>
     * Ideally that will lead to a order, where the input nodes are directly
     * usable as they appear on the robot arm.
     *
     * @return The list of input nodes as described above, or an empty list, if
     * no input nodes are available.
     */
    public List<Mass> getInputs() {
        List<Mass> inputs = new LinkedList<>();

        for (Mass mass : masses) {
            if (mass.getType() == INPUT) {
                inputs.add(mass);
            }
        }

        return inputs;
    }

    /**
     * Gets the shoulder.
     *
     * @return The shoulder or null, if not configured.
     */
    public Mass getShoulder() {
        for (Mass mass : masses) {
            if (mass.getType() == SHOULDER) {
                return mass;
            }
        }

        return null;
    }

    /**
     * Gets the elbow.
     *
     * @return The elbow or null, if not configured.
     */
    public Mass getElbow() {
        for (Mass mass : masses) {
            if (mass.getType() == ELBOW) {
                return mass;
            }
        }

        return null;
    }

    /**
     * Gets the hand.
     *
     * @return The hand or null, if not configured.
     */
    public Mass getHand() {
        for (Mass mass : masses) {
            if (mass.getType() == HAND) {
                return mass;
            }
        }

        return null;
    }

    /**
     * Finds all masses in this graph that are in range to the given mass, of
     * the {@link Mass.Type} INPUT or NETWORK, and are directly reachable,
     * without crossing springs between them an the given mass.
     *
     * @param mass The mass form which should be searched. This mass has to be
     * in this graph.
     * @param range The range/ring around this mass in which should be searched.
     * @param excludeCrossings If set to true, it will be checked if a new
     * spring crosses any existing springs. Such springs will not be generated.
     * @return The list of masses in range. The list is empty, if no masses in
     * range can be found.
     */
    public List<Mass> findReachableMassesAndInputs(Mass mass, Range range, boolean excludeCrossings) {
        List<Mass> massesInRange = new LinkedList<>();

        for (Mass candidate : masses) {
            if (mass == candidate) {
                continue;
            }

            if (candidate.getType() != NETWORK && candidate.getType() != INPUT) {
                continue;
            }

            if (excludeCrossings && wouldSpringToMassCrossExistingNetworkSprings(mass, candidate)) {
                continue;
            }

            double distance = mass.calculateDistance(candidate);

            if (distance >= range.getMin() && distance <= range.getMax()) {
                massesInRange.add(candidate);
            }
        }

        return massesInRange;
    }

    private boolean wouldSpringToMassCrossExistingNetworkSprings(Mass source, Mass destination) {
        Spring hypotheticalSpring = new Spring(this, source, destination);

        for (Spring existingSpring : getSprings()) {
            if (hypotheticalSpring.isCrossing(existingSpring)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds a new spring with the given source and destination to the graph.
     *
     * @param source The source mass.
     * @param destination The destination mass.
     */
    public void addSpring(Mass source, Mass destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("The arguments may not be null.");
        }

        Spring spring = new Spring(this, source, destination);
        springs.add(spring);
    }

    public List<Spring> getSprings() {
        return springs;
    }

    /**
     * Get all {@link Spring}s, the given {@link Mass} is connected to. Thereby,
     * it is does not matter if the given {@link Mass} is the source or the
     * destination of a {@link Spring}.
     *
     * @param mass The mass, to which all connected {@link Spring}s should be
     * returned.
     * @return The connected springs or an empty {@link List}, if no
     * {@link Spring} is connected to the given {@link Mass}.
     */
    public List<Spring> getSprings(Mass mass) {
        LinkedList<Spring> massSprings = new LinkedList<>();

        for (Spring spring : springs) {
            if (spring.isConnected(mass)) {
                massSprings.add(spring);
            }
        }

        return massSprings;
    }

    /**
     * Tests whether a connection between the given masses exists or not. The
     * direction respectively the order of the parameters does not matter.
     *
     * @param mass1 The one mass of the possible connection.
     * @param mass2 The other mass of the possible connection.
     * @return true, if there is a connection, false otherwise.
     */
    public boolean hasSpringBetweenMasses(Mass mass1, Mass mass2) {
        List<Spring> massSprings = getSprings(mass1);

        for (Spring spring : massSprings) {
            if (spring.isConnected(mass2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes all not connected masses of the mass-spring network.
     */
    public void removeNotConnectedNetworkMasses() {
        for (int i = masses.size() - 1; i >= 0; i--) {
            Mass mass = masses.get(i);

            if (mass.getType() == NETWORK && getSprings(mass).isEmpty()) {
                masses.remove(i);
            }
        }
    }

    /**
     * Removes the entire mass-spring network of this graph. This does not
     * affect the robot arm.
     */
    public void removeMassSpringNetwork() {
        for (int i = masses.size() - 1; i >= 0; i--) {
            if (masses.get(i).getType() == NETWORK) {
                masses.remove(i);
            }
        }

        for (int i = springs.size() - 1; i >= 0; i--) {
            Spring spring = springs.get(i);

            if (spring.getSource().getType() == NETWORK
                    || spring.getDestination().getType() == NETWORK) {
                springs.remove(i);
            }
        }
    }

    public void reset() {
        masses = new LinkedList<>();
        springs = new LinkedList<>();
    }

}
