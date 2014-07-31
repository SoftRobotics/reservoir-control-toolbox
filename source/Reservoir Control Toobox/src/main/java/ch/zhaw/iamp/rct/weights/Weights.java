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
package ch.zhaw.iamp.rct.weights;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

/**
 * Allows to calculate correction weights after the learning phase.
 */
public abstract class Weights {

    /**
     * Calculates a pseudo-inverse using the values in the given files. They are
     * first read, then converted to a matrix, and finally used for the
     * calculation of the inverse, using Singular value decomposition (SVD).
     *
     * @param pathToA The file which contains the matrix A, represented in
     * comma-separated-value format.
     * @param targetTrajectoryFile The file which contains the target
     * trajectory, represented in comma-separated-value format.
     * @param weightsFile The file, to which the calculated weights should be
     * written to.
     * @param offset The numbers of first steps to ignore (to skip fading-memory
     * initialization steps).
     */
    public static void calculateWeights(final String pathToA, final String targetTrajectoryFile, final String weightsFile, final int offset) {
        try {
            RealMatrix A = csvToMatrix(pathToA);
            // cut first n elements
            A = A.getSubMatrix(offset, A.getRowDimension() - 1, 0, A.getColumnDimension() - 1);
            A = addNoise(A);

            RealMatrix b = csvToMatrix(targetTrajectoryFile);

            // adjust b to cutting
            int n = offset % b.getRowDimension();

            if (n > 0) {
                RealMatrix tmp = b.getSubMatrix(n, b.getRowDimension() - 1, 0, b.getColumnDimension() - 1);
                b = b.getSubMatrix(0, n - 1, 0, b.getColumnDimension() - 1);
                double[][] tmpArray = tmp.getData();
                double[][] tmpArray2 = b.getData();
                b = MatrixUtils.createRealMatrix(concat(tmpArray, tmpArray2));
                tmpArray = b.getData();

                for (int i = 0; tmpArray.length < A.getRowDimension(); ++i) {
                    tmpArray2 = new double[1][tmpArray[0].length];

                    for (int j = 0; j < tmpArray[i].length; ++j) {
                        tmpArray2[0][j] = tmpArray[i][j];
                    }

                    tmpArray = concat(tmpArray, tmpArray2);
                }

                b = MatrixUtils.createRealMatrix(tmpArray);
            }

            DecompositionSolver solver = new SingularValueDecomposition(A).getSolver();
            RealMatrix x = solver.solve(b).transpose();
            matrixToCsv(x, weightsFile);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not read a file: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Weights.class.getName()).log(Level.WARNING, "Could not read a file: {0}", ex);
        } catch (DimensionMismatchException ex) {
            JOptionPane.showMessageDialog(null, "<html>Could not calculate the "
                    + "pseudo-inverse since a dimension mismatch occurred.<br />"
                    + "Please make sure that all lines of the CSV file posses "
                    + "the same amount of entries.<br />Hint: Remove the last "
                    + "line and try it again.</html>", "Matrix Dimension Mismatch", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Weights.class.getName()).log(Level.WARNING, "A dimension mismatch occurred: {0}", ex);
        }
    }

    private static void matrixToCsv(final RealMatrix matrix, final String targetFilePath) throws IOException {
        Writer out = new FileWriter(targetFilePath);

        for (int i = 0; i < matrix.getRowDimension(); ++i) {
            for (int j = 0; j < matrix.getColumnDimension(); ++j) {
                out.append("" + matrix.getEntry(i, j));
                if (j < matrix.getColumnDimension() - 1) {
                    out.append(",");
                }
            }
            out.append("\n");
            out.flush();
        }

    }

    private static RealMatrix csvToMatrix(final String sourceFilePath) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(sourceFilePath));
        double[][] matrixArray = null;

        while (in.ready()) {
            String currentLine = in.readLine();

            if (currentLine.startsWith("#") || currentLine.trim().isEmpty()) {
                continue;
            }

            String[] tokens = currentLine.split(",");

            if (matrixArray == null) {
                matrixArray = new double[0][tokens.length];
            }

            double[][] tmpArray = new double[1][tokens.length];

            for (int i = 0; i < tokens.length; ++i) {
                if (!tokens[i].trim().isEmpty()) {
                    tmpArray[0][i] = Double.parseDouble(tokens[i]);
                }
            }

            matrixArray = concat(matrixArray, tmpArray);
        }

        return MatrixUtils.createRealMatrix(matrixArray);
    }

    public static double[][] concat(final double[][] first, final double[][] second) {
        double[][] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static RealMatrix addNoise(final RealMatrix A) {
        RealMatrix buffer = A.copy();
        RealMatrix noise = MatrixUtils.createRealMatrix(A.getRowDimension(), A.getColumnDimension());

        for (int i = 0; i < A.getRowDimension(); ++i) {
            for (int j = 0; j < A.getColumnDimension(); ++j) {
                double noiseSign = Math.random() > 0.5 ? 1 : -1;
                double noiseAmplitude = 1;
                noise.setEntry(i, j, noiseSign * Math.random() * noiseAmplitude);
            }
        }

        buffer = buffer.add(noise);
        return buffer;
    }
}
