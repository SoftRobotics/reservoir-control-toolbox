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

import static ch.zhaw.iamp.rct.grammar.GrammarParser.Option.excludeSpringCrossings;
import static ch.zhaw.iamp.rct.grammar.GrammarParser.Option.showNotConnectedMasses;
import static ch.zhaw.iamp.rct.grammar.GrammarParser.Option.allowNegativeYValues;
import ch.zhaw.iamp.rct.graph.Mass;
import static ch.zhaw.iamp.rct.graph.Mass.Type.INPUT;
import static ch.zhaw.iamp.rct.graph.Mass.Type.NETWORK;
import ch.zhaw.iamp.rct.graph.Spring;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GraphDeveloper {

    final static Random random = new SecureRandom();
    final static double MINIMAL_MASS_Y_VALUE = 0d;
    final static double  PROBABILITY_TO_RANDOMLY_CHOOSE_INPUT = 0.2;
    final static int MAXIMAL_RANDOM_LOOP_COUNT_LIMIT = 1000;
    GrammarParser grammar;
    String developedInit;
    List<ParserError> errors;
    Mass currentPoint;

    public GraphDeveloper(GrammarParser grammar, String developedInitialisation) {
        this.grammar = grammar;
        this.developedInit = developedInitialisation;
        this.errors = new LinkedList<>();
    }

    /**
     * Develops the graph with the configured resources.
     *
     * @throws IllegalStateException If no shoulder is configured.
     */
    public void develop() {
        if (grammar.getGraph().getInputs().isEmpty()) {
            System.out.println("There are not input nodes - add some to develop a graph.");
            return;
        }

        grammar.getGraph().removeMassSpringNetwork();
        currentPoint = grammar.getGraph().getInputs().get(0);

        for (int i = 0; i < developedInit.length(); i++) {
            String letter = developedInit.substring(i, i + 1);

            if (grammar.isMassCreation(letter)) {
                Mass createdMass = createMass(letter);
                grammar.getGraph().addMasses(createdMass);
                continue;
            }

            if (grammar.isSpringCreation(letter)) {
                createSpring(letter);
                continue;
            }

            errors.add(new ParserError(i, letter));
        }

        addRandomMasses();
        addRandomSprings();
        removeSingleConectedMasses();

        if (!grammar.options.contains(showNotConnectedMasses)) {
            grammar.getGraph().removeNotConnectedNetworkMasses();
        }
    }

    void addRandomMasses() {
        for (int i = 0; i < grammar.getNumberOfRandomMasses(); i++) {
            Range expansionRangeX = grammar.getExpansionRangeX();
            Range expansionRangeY = new Range(grammar.shoulder.getY(), grammar.hand.getY());            
            Mass randomMass = createMass(expansionRangeX, expansionRangeY);
            
            
            if (randomMass.getY() > grammar.hand.getY()) {
                randomMass = new Mass(randomMass.getX(), grammar.hand.getY(), randomMass.getType());
            }

            grammar.getGraph().addMasses(randomMass);
        }
    }

    void addRandomSprings() {
        List<Mass> network = grammar.getGraph().getNetworkMasses();
        List<Mass> inputs = grammar.getGraph().getInputs();

        if (network.isEmpty()) {
            return;
        }

        int maxMassIndex = network.size() - 1;
        int maxChoiceIndex = maxMassIndex + inputs.size();

        int createdSprings = 0;
        int loopCount = 0;

        while (loopCount < MAXIMAL_RANDOM_LOOP_COUNT_LIMIT && createdSprings < grammar.getNumberOfRandomSprings()) {
            loopCount++;

            int sourceIndex = random.nextInt(maxChoiceIndex);
            int destinationIndex = random.nextInt(maxChoiceIndex);

            if (sourceIndex == destinationIndex) {
                continue;
            }

            Mass source;
            Mass destination;

            if (sourceIndex > maxMassIndex) {
                int inputIndex = sourceIndex - maxMassIndex;
                source = inputs.get(inputIndex);
            } else {
                source = network.get(sourceIndex);
            }

            if (destinationIndex > maxMassIndex) {
                int inputIndex = destinationIndex - maxMassIndex;
                destination = inputs.get(inputIndex);
            } else {
                destination = network.get(destinationIndex);
            }
            
            if (source.getType() == NETWORK && PROBABILITY_TO_RANDOMLY_CHOOSE_INPUT >= random.nextDouble()) {
                if (destination.getType() != INPUT) {
                    destination = inputs.get(random.nextInt(inputs.size()));
                }
            }
            
            if (source.getType() == INPUT && destination.getType() == INPUT) {
                continue;
            }

            if (grammar.getGraph().hasSpringBetweenMasses(source, destination)) {
                continue;
            }

            grammar.getGraph().addSpring(source, destination);

            createdSprings++;
        }
    }

    void removeSingleConectedMasses() {
        List<Mass> network = grammar.getGraph().getNetworkMasses();

        for (Mass mass : network) {
            List<Spring> springs = grammar.getGraph().getSprings(mass);

            if (springs.size() == 1) {
                grammar.getGraph().getSprings().remove(springs.get(0));
            }
        }
    }

    Mass createMass(String letter) {
        Range distanceRangeX = grammar.getMassCreationRange(letter);
        Range distanceRangeY = grammar.getMassCreationRange(letter);

        return createMass(distanceRangeX, distanceRangeY);
    }

    Mass createMass(Range distanceRangeX, Range distanceRangeY) {
        boolean takePositiveX = random.nextBoolean();
        boolean takePositiveY = random.nextBoolean();
        double minimalX, maximalX, minimalY, maximalY;

        if (takePositiveX) {
            minimalX = currentPoint.getX() + distanceRangeX.getMin();
            maximalX = currentPoint.getX() + distanceRangeX.getMax();
        } else {
            minimalX = currentPoint.getX() - distanceRangeX.getMax();
            maximalX = currentPoint.getX() - distanceRangeX.getMin();
        }

        if (takePositiveY) {
            minimalY = currentPoint.getY() + distanceRangeY.getMin();
            maximalY = currentPoint.getY() + distanceRangeY.getMax();
        } else {
            minimalY = currentPoint.getY() - distanceRangeY.getMax();
            maximalY = currentPoint.getY() - distanceRangeY.getMin();
        }

        Range xRange = getTigtherRange(new Range(minimalX, maximalX), grammar.getExpansionRangeX());
        Range yRange = new Range(minimalY, maximalY);

        double x = getNumberInRange(xRange);
        double y = getNumberInRange(yRange);

        if (!grammar.options.contains(allowNegativeYValues)) {
            y = Math.abs(y);
        }

        return new Mass(x, y);
    }

    static Range getTigtherRange(Range range1, Range range2) {
        double tighterMinValue = range1.getMin() > range2.getMin() ? range1.getMin() : range2.getMin();
        double tighterMaxValue = range1.getMax() < range2.getMax() ? range1.getMax() : range2.getMax();

        return new Range(tighterMinValue, tighterMaxValue);
    }

    void createSpring(String letter) {
        Range distanceRange = grammar.getSpringCreationRange(letter);
        boolean excludeCrossings = grammar.getOptions().contains(excludeSpringCrossings);
        List<Mass> usableMasses = grammar.getGraph().findReachableMassesAndInputs(currentPoint, distanceRange, excludeCrossings);
        removeMassesInOwnSprings(currentPoint, usableMasses);

        if (usableMasses.isEmpty()) {
            return;
        }

        int randomIndex = random.nextInt(usableMasses.size());
        Mass randomValidMassInRange = usableMasses.get(randomIndex);
        grammar.getGraph().addSpring(currentPoint, randomValidMassInRange);
        currentPoint = randomValidMassInRange;
    }

    private void removeMassesInOwnSprings(Mass involvedMass, List<Mass> masses) {
        for (int i = masses.size() - 1; i >= 0; i--) {
            Mass candidate = masses.get(i);

            if (involvedMass.isConnected(grammar.getGraph(), candidate)) {
                masses.remove(i);
            }
        }
    }

    static double getNumberInRange(Range range) {
        return random.nextDouble() * (range.getMax() - range.getMin()) + range.getMin();
    }

    public List<ParserError> getErrors() {
        return errors;
    }

}
