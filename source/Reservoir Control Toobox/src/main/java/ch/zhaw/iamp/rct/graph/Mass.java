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

import java.awt.Color;
import java.util.List;

/**
 * A {@link Mass} is part of {@link NetworkGraph}s and represents a node, which
 * may connect several springs.
 * <p>
 * The complete robot arm and its mass-spring network are technically connected
 * via masses. In order to handle logically different masses programmatically
 * different, masses have to be of a specific {@link Type}. The default is
 * {@link Type#NETWORK}, therefore is a <i>normal</i> network mass.
 */
public class Mass {

    /**
     * Defines the type of this mass. Depending on the type, it is determined
     * whether force is applied to the mass or not.
     */
    public enum Type {

        /**
         * Typically, only one mass is of the type shoulder, that one that is
         * fixed at the ground.
         * <p>
         * {@code f} stands for <i>false</i> in the context of applying mass.
         */
        /**
         * Typically, only one mass is of the type shoulder, that one that is
         * fixed at the ground.
         * <p>
         * {@code f} stands for <i>false</i> in the context of applying mass.
         */
        SHOULDER("f", Color.RED),
        /**
         * The elbow is a joint between the upper and the lower arm.
         * <p>
         * {@code i} stands for <i>input node</i>, but is also used for the
         * elbow mass.
         */
        ELBOW("i", Color.GRAY),
        /**
         * The hand is the end of the robot arm and follows the targeted curve.
         * <p>
         * {@code e} stands for <i>end effector</i>.
         */
        HAND("e", Color.BLUE),
        /**
         * The hand is the end of the robot arm and follows the targeted curve.
         * <p>
         * {@code r} stands for <i>robot arm segment</i>.
         */
        ARM_SEGMENT("r", Color.LIGHT_GRAY),
        /**
         * An input node is a point on the robot arm that is connected to both,
         * the robot arm and the mass-spring network.
         * <p>
         * {@code i} stands for <i>input node</i>.
         */
        INPUT("i", Color.GREEN),
        /**
         * A network mass is a mass that part of the mass-spring network and is
         * via springs connected to other masses and possibly to the input
         * nodes.
         * <p>
         * {@code t} stands for <i>true</i> in the context of applying mass.
         */
        NETWORK("t", Color.WHITE);

        private final String csvRepresentation;
        private final Color color;

        private Type(String csvRepresentation, Color color) {
            this.csvRepresentation = csvRepresentation;
            this.color = color;
        }

        /**
         * This returns the CSV type representation value.
         *
         * @return The value used in CSV files.
         */
        public String getCsvValue() {
            return csvRepresentation;
        }

        public static Type getByCsvValue(String csvValue) {
            for (Type value : Type.values()) {
                if (value.getCsvValue().equals(csvValue)) {
                    return value;
                }
            }

            throw new IllegalArgumentException("The value'" + csvValue + "' is not a valid type.");
        }

        public Color getColor() {
            return color;
        }
    }
    double x;
    double y;
    Type type;

    /**
     * Creates a new mass at the given coordinates. Per default, the
     * {@link Type} of this mass is <b>NETWORK</b>.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Mass(double x, double y) {
        this.x = x;
        this.y = y;
        type = Type.NETWORK;
    }

    /**
     * Creates a new mass at the given coordinates and of the given type.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param type The type of this mass.
     * @throws IllegalArgumentException If the type is null.
     */
    public Mass(double x, double y, Type type) {
        this(x, y);

        if (type == null) {
            throw new IllegalArgumentException("The type may not be null.");
        }

        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Sets the type of this mass.
     *
     * @param type The new type.
     * @throws IllegalArgumentException If the argument is null.
     */
    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * Gets the index of this {@link Mass} in the given {@link NetworkGraph}.
     *
     * @param graph The graph, in which the index of this {@link Mass} should be
     * returned.
     * @return The index of this {@link Mass}
     * @throws IllegalArgumentException If the argument is null.
     * @throws IllegalStateException If this {@link Mass} cannot be found in the
     * given {@link NetworkGraph} at the time of invocation.
     */
    public int getIndex(NetworkGraph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        if (!graph.getMasses().contains(this)) {
            throw new IllegalStateException("This mass is not in the given NetworkGraph.");
        }

        return graph.getMasses().indexOf(this);
    }

    /**
     * Calculates the distance between this and the other mass.
     *
     * @param other The other mass.
     * @return The distance between this and the other mass.
     */
    public double calculateDistance(Mass other) {
        double deltaX = this.x - other.x;
        double deltaY = this.y - other.y;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public boolean isConnected(NetworkGraph graph, Mass other) {
        List<Spring> springs = graph.getSprings(this);

        for (Spring spring : springs) {
            if (spring.isConnected(other)) {
                return true;
            }
        }

        return false;
    }

}
