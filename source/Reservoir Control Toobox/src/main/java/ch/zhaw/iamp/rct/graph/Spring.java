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

import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType.*;
import static ch.zhaw.iamp.rct.graph.Mass.Type.*;
import static ch.zhaw.iamp.rct.graph.Mass.Type;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A {@link Spring} represents a connection part of a robot arm (segments) or
 * mass-spring networks (springs).
 *
 * @see NetworkGraph
 * @see Mass
 */
public class Spring implements Comparable<Spring> {

    private static final double POINT_DISTNACE_TO_INTERPRET_AS_CONNECTED = 0.00000001;

    /**
     * The {@link ConnectionType} of a spring defines how it behaves in the
     * simulation. The configured codes will be exported to the Physics Toolbox
     * where they are interpreted appropriately.
     */
    public enum ConnectionType {

        SPRING_CONSTRAINT(1),
        POINT_TO_POINT_CONSTRAINT(2),
        SLIDER_CONSTRAINT(3),
        CONE_TWIST_CONSTRAINT(4),
        FIXED_CONSTRAINT(5),
        ROBOT_ARM_BASE_CONSTRAINT(6),
        ROBOT_ARM_JOINT_CONSTRAINT(7);

        private final int value;

        private ConnectionType(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }
    NetworkGraph graph;
    Mass source;
    Mass destination;
    Line2D line;
    ConnectionType connectionType;

    /**
     * Creates a new {@link Spring} between the given {@link Mass}es. The
     * default {@link ConnectionType} is {@code SPRING_CONSTRAINT}.
     *
     * @param graph The {@link NetworkGraph}, this {@link Spring} is in.
     * @param source The source {@link Mass}.
     * @param destination The destination {@link Mass}.
     * @throws IllegalArgumentException If at least one argument is null.
     */
    public Spring(NetworkGraph graph, Mass source, Mass destination) {
        if (graph == null || source == null || destination == null) {
            throw new IllegalArgumentException("The arguments may not be null.");
        }

        this.graph = graph;
        this.source = source;
        this.destination = destination;
        this.line = new Line2D.Double(source.getX(), source.getY(),
                destination.getX(), destination.getY());

        evaluateConnectionType();
    }

    private void evaluateConnectionType() {
        Type destinationType = destination.type;

        switch (source.type) {
            case SHOULDER:
                connectionType = destinationType == ARM_SEGMENT ? ROBOT_ARM_BASE_CONSTRAINT : FIXED_CONSTRAINT;
                break;
            case ARM_SEGMENT:
                switch (destinationType) {
                    case ARM_SEGMENT:
                        connectionType = ROBOT_ARM_JOINT_CONSTRAINT;
                        break;
                    case SHOULDER:
                        connectionType = ROBOT_ARM_BASE_CONSTRAINT;
                        break;
                    default:
                        connectionType = FIXED_CONSTRAINT;
                }
                break;
            case INPUT:
                connectionType = destinationType == NETWORK ? SPRING_CONSTRAINT : FIXED_CONSTRAINT;
                break;
            case ELBOW:
                connectionType = FIXED_CONSTRAINT;
                break;
            case HAND:
                connectionType = FIXED_CONSTRAINT;
                break;
            case NETWORK:
                connectionType = SPRING_CONSTRAINT;
                break;
            default:
                throw new IllegalStateException("The given masses of the type "
                        + source.getType().toString() + " (source) respectively "
                        + destinationType + " (destination) cannot be connected "
                        + "via springs.");
        }

    }

    public Mass getSource() {
        return source;
    }

    public Mass getDestination() {
        return destination;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public Line2D getLine() {
        return line;
    }

    /**
     * Sets the given {@link ConnectionType} to this {@link Spring}.
     *
     * @param type The type to set.
     * @throws IllegalArgumentException If the argument is null.
     */
    public void setConnectionType(ConnectionType type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        connectionType = type;
    }

    /**
     * Tests whether the given mass is either source mass or destination mass of
     * this spring.
     *
     * @param mass The mass to test.
     * @return true, if the mass is part of this spring, false otherwise.
     */
    public boolean isConnected(Mass mass) {
        return source.equals(mass) || destination.equals(mass);
    }

    /**
     * Tests whether the given spring crosses geometrically in a 2D pane this
     * spring or not.
     *
     * @param spring The other spring
     * @return true, if this and the other spring cross each other, false
     * otherwise.
     */
    public boolean isCrossing(Spring spring) {
        boolean isCrossing = getLine().intersectsLine(spring.getLine());
        boolean isConnectedAtOnlyOneEnd = isGeometricallyConnectedAtOnlyOneEnd(this, spring);

        if (isCrossing && !isConnectedAtOnlyOneEnd) {
            return true;
        } else if (isCrossing && isConnectedAtOnlyOneEnd) {
            return false;
        }

        return false;
    }

    private static boolean isGeometricallyConnectedAtOnlyOneEnd(Spring spring1, Spring spring2) {
        Point2D spring1Point1 = spring1.getLine().getP1();
        Point2D spring1Point2 = spring1.getLine().getP2();
        Point2D spring2Point1 = spring2.getLine().getP1();
        Point2D spring2Point2 = spring2.getLine().getP2();

        boolean s1P1AndS2P1AreConnected = spring1Point1.distance(spring2Point1) < POINT_DISTNACE_TO_INTERPRET_AS_CONNECTED;
        boolean s1P1AndS2P2AreConnected = spring1Point1.distance(spring2Point2) < POINT_DISTNACE_TO_INTERPRET_AS_CONNECTED;
        boolean s1P2AndS2P1AreConnected = spring1Point2.distance(spring2Point1) < POINT_DISTNACE_TO_INTERPRET_AS_CONNECTED;
        boolean s1P2AndS2P2AreConnected = spring1Point2.distance(spring2Point2) < POINT_DISTNACE_TO_INTERPRET_AS_CONNECTED;

        return (s1P1AndS2P1AreConnected && !s1P2AndS2P2AreConnected)
                || (s1P1AndS2P2AreConnected && !s1P2AndS2P1AreConnected)
                || (s1P2AndS2P1AreConnected && !s1P1AndS2P2AreConnected)
                || (s1P2AndS2P2AreConnected && !s1P1AndS2P1AreConnected);
    }

    /**
     * Compares this {@link Spring} to a given other {@link Spring}. This one is
     * greater than the other one, when the higher index of its two masses is is
     * higher than the others higher index of that masses. If they are equal,
     * the same comparison is performed on the masses of each spring, that has
     * the lower index. If both {@link Spring}s are equal, zero is returned.
     * <p>
     * This is used in {@link GraphConverter} to sort the springs for the
     * connection map output.
     *
     * @param other The {@link Spring} to compare this to.
     * @return An {@code int} lower to zero, if this is lower than other, zero,
     * if this is equals other, or greater zero, if this is greater other.
     * @throws IllegalArgumentException If the argument is null.
     */
    @Override
    public int compareTo(Spring other) {
        if (other == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        int thisHigherIndex = this.getMassWithHigherIndex().getIndex(graph);
        int otherHigherIndex = other.getMassWithHigherIndex().getIndex(graph);

        if (thisHigherIndex != otherHigherIndex) {
            return thisHigherIndex - otherHigherIndex;
        }

        int thisLowerIndex = this.getMassWithLowerIndex().getIndex(graph);
        int otherLowerIndex = other.getMassWithLowerIndex().getIndex(graph);

        return thisLowerIndex - otherLowerIndex;
    }

    /**
     * @return That mass of this spring, which has the higher index in
     * {@link NetworkGraph} it is in.
     */
    public Mass getMassWithHigherIndex() {
        int sourceIndex = getSource().getIndex(graph);
        int destinationIndex = getDestination().getIndex(graph);

        return sourceIndex > destinationIndex ? getSource() : getDestination();
    }

    /**
     * @return That mass of this spring, which has the lower index in
     * {@link NetworkGraph} it is in.
     */
    public Mass getMassWithLowerIndex() {
        int sourceIndex = getSource().getIndex(graph);
        int destinationIndex = getDestination().getIndex(graph);

        return sourceIndex < destinationIndex ? getSource() : getDestination();
    }

}
