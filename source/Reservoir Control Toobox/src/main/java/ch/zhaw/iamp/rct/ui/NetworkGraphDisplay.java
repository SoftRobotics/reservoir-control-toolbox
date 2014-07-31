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
package ch.zhaw.iamp.rct.ui;

import ch.zhaw.iamp.rct.graph.Mass;
import static ch.zhaw.iamp.rct.graph.Mass.Type.ARM_SEGMENT;
import static ch.zhaw.iamp.rct.graph.Mass.Type.SHOULDER;
import ch.zhaw.iamp.rct.graph.NetworkGraph;
import ch.zhaw.iamp.rct.graph.Spring;
import static ch.zhaw.iamp.rct.graph.Spring.ConnectionType.SPRING_CONSTRAINT;
import java.awt.BasicStroke;
import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.JOIN_MITER;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class NetworkGraphDisplay extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int MAX_SCORE = 20;
    private static final Stroke STROKE = new BasicStroke(1f);
    private static final Stroke ARM_STROKE = new BasicStroke(6f);
    private static final float EXPANSION_RANGE_DASH[] = {2.0f};
    private static final Stroke EXPANSION_RANGE_STROKE = new BasicStroke(1.0f, CAP_BUTT, JOIN_MITER, 10.0f, EXPANSION_RANGE_DASH, 0.0f);
    private static final Color SPRING_COLOR = Color.green;
    private static final Color ARM_COLOR = Color.LIGHT_GRAY;
    private static final Color BORDER_COLOR = Color.DARK_GRAY;
    private static final Color EXPANSION_RANGE_COLOR = Color.ORANGE;
    private static final int MASS_RADIUS = 6;
    private static final int HATCH_LENGTH = 10;
    private static final int MASS_AND_SPRING_Y_SCALING = -1;
    GrammarWindow grammarWindow;
    NetworkGraph networkGraph;
    Graphics2D g2;
    Stroke defaultStroke;
    double xScale;
    double yScale;
    int zoomDimensionCorrection = 2;
    Point mouseDragTransformation = new Point(getHeight() / 2, 0);
    Point mouseClickPoint;

    public NetworkGraphDisplay() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent event) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                mouseClickPoint = event.getPoint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                mouseClickPoint = event.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent event) {
                if (grammarWindow == null) {
                    return;
                }

                int mouseX = event.getX() - getWidth() / 2;
                int mouseY = getHeight() / 2 - event.getY();
                double zoomLevel = grammarWindow.getZoomLevel();

                for (Mass mass : networkGraph.getMasses()) {
                    int massX = (int) (mass.getX() * xScale * zoomLevel + zoomLevel * mouseDragTransformation.x);
                    int massY = (int) (mass.getY() * yScale * zoomLevel - zoomLevel * mouseDragTransformation.y);

                    if (mouseX >= massX - MASS_RADIUS
                            && mouseX <= massX + MASS_RADIUS
                            && mouseY >= massY - MASS_RADIUS
                            && mouseY <= massY + MASS_RADIUS) {
                        grammarWindow.setCoordinatesLabel(mass.getX(), mass.getY());
                        return;
                    }
                }

                grammarWindow.resetCoordinatesLabel();
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                double zoomLevel = grammarWindow.getZoomLevel();
                int dx = (int) Math.round((1.0d * (event.getX() - mouseClickPoint.x) / zoomLevel) + mouseDragTransformation.x);
                int dy = (int) Math.round((1.0d * (event.getY() - mouseClickPoint.y) / zoomLevel) + mouseDragTransformation.y);
                mouseDragTransformation.setLocation(dx, dy);
                mouseClickPoint = event.getPoint();
                repaint();
            }

        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        zoom();
        g2.translate(getWidth() / 2 + mouseDragTransformation.x, getHeight() / 2 + mouseDragTransformation.y);
        defaultStroke = g2.getStroke();

        enableAntialiasing();
        calculateScaling();
        drawYAxis();
        drawYHatches();

        drawRobotArm();
        drawExpansionRanges();
        drawSprings();
        drawMasses();
    }

    private void zoom() {
        if (grammarWindow == null) {
            return;
        }

        double zoomFactor = grammarWindow.getZoomLevel();
        int realWidth = getWidth();
        int realHeiht = getHeight();

        g2.translate(realWidth / 2, realHeiht / 2);
        g2.scale(zoomFactor, zoomFactor);
        g2.translate(-realWidth / 2, -realHeiht / 2);
    }

    private void enableAntialiasing() {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void calculateScaling() {
        xScale = ((double) getWidth()) / (2 * MAX_SCORE);
        yScale = ((double) getHeight()) / (2 * MAX_SCORE);
    }

    private void drawYHatches() {
        int numberOfYHatches = calculateNumberOfHatchesAsOddNumber();
        int distanceBetweenHatches = (int) Math.round(1.0d * getHeight() / numberOfYHatches);
        int x0 = -HATCH_LENGTH / 2;
        int x1 = HATCH_LENGTH / 2;
        int y0, y1;

        for (int i = 0; i < numberOfYHatches; i++) {
            if (i == 0) {
                drawXAxis();
            }

            y0 = i * distanceBetweenHatches;
            y1 = y0;
            g2.drawLine(x0, y0, x1, y1);

            y0 = -i * distanceBetweenHatches;
            y1 = y0;
            g2.drawLine(x0, y0, x1, y1);
        }
    }

    private int calculateNumberOfHatchesAsOddNumber() {
        int numberOfYHatches = Math.round((float) (getHeight() / yScale));
        numberOfYHatches += numberOfYHatches % 2 == 0 ? 1 : 0;

        return numberOfYHatches;
    }

    private void drawXAxis() {
        int y0 = 0;
        int y1 = y0;
        g2.drawLine(-getWidth() * zoomDimensionCorrection, y0, getWidth() * zoomDimensionCorrection, y1);
        drawXHatches(y1);
    }

    private void drawYAxis() {
        g2.drawLine(0, -getHeight(), 0, getHeight());
    }

    private void drawXHatches(int yPosition) {
        int numberOfXHatches = (int) Math.round(1.0d * getWidth() / xScale);
        int distanceBetweenHatches = (int) Math.round(1.0d * getWidth() / numberOfXHatches);

        int x0, x1;
        int y0 = yPosition - HATCH_LENGTH / 2;
        int y1 = yPosition + HATCH_LENGTH / 2;

        for (int i = -numberOfXHatches * zoomDimensionCorrection + 1;
                i < numberOfXHatches * zoomDimensionCorrection;
                i++) {
            x0 = i * distanceBetweenHatches;
            x1 = x0;
            g2.drawLine(x0, y0, x1, y1);
        }
    }

    private void drawRobotArm() {
        Mass shoulder = null;
        Mass elbow = null;
        Mass hand = null;

        for (Mass mass : networkGraph.getMasses()) {
            switch (mass.getType()) {
                case SHOULDER:
                    shoulder = mass;
                    break;
                case ELBOW:
                    elbow = mass;
                    break;
                case HAND:
                    hand = mass;
            }
        }

        g2.setColor(ARM_COLOR);
        g2.setStroke(ARM_STROKE);

        if (shoulder != null && elbow != null) {
            int shoulderX = (int) (xScale * shoulder.getX());
            int shoulderY = (int) (yScale * shoulder.getY() * MASS_AND_SPRING_Y_SCALING);
            int elbowX = (int) (xScale * elbow.getX());
            int elbowY = (int) (yScale * elbow.getY() * MASS_AND_SPRING_Y_SCALING);
            g2.drawLine(shoulderX, shoulderY, elbowX, elbowY);
        }

        if (elbow != null && hand != null) {
            int elbowX = (int) (xScale * elbow.getX());
            int elbowY = (int) (yScale * elbow.getY() * MASS_AND_SPRING_Y_SCALING);
            int handX = (int) (xScale * hand.getX());
            int handY = (int) (yScale * hand.getY() * MASS_AND_SPRING_Y_SCALING);
            g2.drawLine(elbowX, elbowY, handX, handY);
        }
    }

    private void drawExpansionRanges() {
        final double X_CORRECTURE = 0.0276;
        int minRangeX = (int) ((X_CORRECTURE + grammarWindow.getGrammarParser().getExpansionRangeX().getMin()) * xScale);
        int maxRangeX = (int) ((X_CORRECTURE + grammarWindow.getGrammarParser().getExpansionRangeX().getMax()) * xScale);

        g2.setStroke(EXPANSION_RANGE_STROKE);
        g2.setColor(EXPANSION_RANGE_COLOR);
        g2.drawLine(minRangeX, -getHeight(), minRangeX, getHeight());
        g2.drawLine(maxRangeX, -getHeight(), maxRangeX, getHeight());
    }

    private void drawSprings() {
        g2.setColor(SPRING_COLOR);
        g2.setStroke(STROKE);

        for (int i = 0; i < networkGraph.getSprings().size(); i++) {
            Spring spring = networkGraph.getSprings().get(i);

            if (skipThisSpring(spring)) {
                continue;
            }

            int x1 = (int) (xScale * spring.getSource().getX());
            int y1 = (int) (yScale * spring.getSource().getY() * MASS_AND_SPRING_Y_SCALING);
            int x2 = (int) (xScale * spring.getDestination().getX());
            int y2 = (int) (yScale * spring.getDestination().getY() * MASS_AND_SPRING_Y_SCALING);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    private boolean skipThisSpring(Spring spring) {
        return spring.getConnectionType() != SPRING_CONSTRAINT;
    }

    private void drawMasses() {
        g2.setStroke(defaultStroke);
        int circleRadius = MASS_RADIUS * 2;

        for (Mass mass : networkGraph.getMasses()) {
            if (mass.getType() == ARM_SEGMENT) {
                continue;
            }

            int x = (int) (xScale * mass.getX()) - MASS_RADIUS;
            int y = (int) (yScale * mass.getY() * MASS_AND_SPRING_Y_SCALING) - MASS_RADIUS;

            g2.setColor(mass.getType().getColor());
            g2.fillOval(x, y, circleRadius, circleRadius);
            g2.setColor(BORDER_COLOR);
            g2.drawOval(x, y, circleRadius, circleRadius);
        }
    }

    public void setGrammarWindow(GrammarWindow grammarWindow) {
        if (grammarWindow == null) {
            throw new IllegalArgumentException("The arugment may not be null.");
        }

        this.grammarWindow = grammarWindow;
    }

    public void setNetworkGraph(NetworkGraph networkGraph) {
        if (networkGraph == null) {
            throw new IllegalArgumentException("The arugment may not be null.");
        }

        this.networkGraph = networkGraph;
    }

}
