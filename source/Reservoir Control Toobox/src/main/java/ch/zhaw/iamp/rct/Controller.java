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
package ch.zhaw.iamp.rct;

import ch.zhaw.iamp.rct.graph.GraphConverter;
import ch.zhaw.iamp.rct.graph.NetworkGraph;
import ch.zhaw.iamp.rct.ui.AboutWindow;
import ch.zhaw.iamp.rct.ui.GrammarHelpWindow;
import ch.zhaw.iamp.rct.ui.GrammarWindow;
import ch.zhaw.iamp.rct.ui.InitialisationHelpWindow;
import ch.zhaw.iamp.rct.ui.LoadGraphWindow;
import ch.zhaw.iamp.rct.ui.MainWindow;
import ch.zhaw.iamp.rct.ui.WeightsCalculatorWindow;
import ch.zhaw.iamp.rct.util.Components;
import ch.zhaw.iamp.rct.util.Dialogs;
import ch.zhaw.iamp.rct.weights.Weights;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.io.FileUtils;

/**
 * This controller class triggers application wide reactions such as
 * opening/closing frames.
 */
public class Controller {

    public final static int SIMULATOR_INVOCATION_PROBLEM_EXIT_CODE = 99;
    private final static String GRAMMAR_GRAPH_MASSES_FILENAME_PREFIX = "masses-";
    private final static String GRAMMAR_GRAPH_CONNECTION_MAP_FILENAME_PREFIX = "connection-map-";
    private final static String GRAMMAR_GRAPH_OUTPUT_FILE_ENDING = ".csv";
    MainWindow mainWindow;
    private GrammarWindow grammarWindow;
    private WeightsCalculatorWindow weightsCalculatorWindow;
    private GrammarHelpWindow grammarHelpWindow;
    private InitialisationHelpWindow initialisationHelpWindow;
    private AboutWindow aboutWindow;
    private LoadGraphWindow loadGraphWindow;
    private Thread weightCalulation;

    /**
     * Shows the {@link MainWindow}. If there is no instance yet, one will be
     * created.
     */
    public void showMainWindow() {
        configureGuiToolkit();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mainWindow = new MainWindow();
                Components.setIcons(mainWindow);
                mainWindow.setController(App.controller);
                mainWindow.setVisible(true);
            }
        });
    }

    private void configureGuiToolkit() {
        String operatingSystem = System.getProperty("os.name").toLowerCase();
        boolean isUnix = operatingSystem.contains("nix")
                || operatingSystem.contains("nux")
                || operatingSystem.contains("aix");

        try {
            if (isUnix) {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new IllegalStateException("The Look&Feel could not be configured correctly: " + ex.getMessage());
        }
    }

    /**
     * Shows the {@link GrammarWindow}. If there is no instance yet, one will be
     * created.
     */
    public void showGrammarWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!isGrammarWindowVisible()) {
                    grammarWindow = new GrammarWindow();
                    grammarWindow.setController(App.controller);
                    Components.setIcons(grammarWindow);
                    grammarWindow.setVisible(true);
                    grammarWindow.restoreHelpWindows();
                }

                grammarWindow.requestFocus();
                grammarWindow.toFront();
            }
        });
    }

    private boolean isGrammarWindowVisible() {
        return grammarWindow != null && grammarWindow.isVisible();
    }

    /**
     * Shows the {@link GrammarHelpWindow}. If there is no instance yet, one
     * will be created.
     *
     * @param xPosition The x position at which the window may be put.
     * @param yPosition The y position at which the window may be put.
     */
    public void showGrammarHelpWindow(final int xPosition, final int yPosition) {
        if (!isGrammarHelpWindowVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    grammarHelpWindow = new GrammarHelpWindow(grammarWindow, false);
                    grammarHelpWindow.setLocation(xPosition, yPosition);
                    Components.setIcons(grammarHelpWindow);
                    grammarHelpWindow.setVisible(true);
                }
            });
        }
    }

    /**
     * @return true, when the {@link GrammarHelpWindow} is visible, false
     * otherwise.
     */
    public boolean isGrammarHelpWindowVisible() {
        return grammarHelpWindow != null && grammarHelpWindow.isVisible();
    }

    /**
     * Shows the {@link InitialisationHelpWindow}. If there is no instance yet,
     * one will be created.
     *
     * @param xPosition The x position at which the window may be put.
     * @param yPosition The y position at which the window may be put.
     */
    public void showInitialisationHelpWindow(final int xPosition, final int yPosition) {
        if (!isInitialisationHelpWindowVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    initialisationHelpWindow = new InitialisationHelpWindow(grammarWindow, false);
                    initialisationHelpWindow.setLocation(xPosition, yPosition);
                    Components.setIcons(initialisationHelpWindow);
                    initialisationHelpWindow.setVisible(true);
                }
            });
        }
    }

    /**
     * @return true, when the {@link InitialisationHelpWindow} is visible, false
     * otherwise.
     */
    public InitialisationHelpWindow getInitialisationHelpWindow() {
        return initialisationHelpWindow;
    }

    /**
     * @return true, when the {@link InitialisationHelpWindow} is visible, false
     * otherwise.
     */
    public boolean isInitialisationHelpWindowVisible() {
        return initialisationHelpWindow != null && initialisationHelpWindow.isVisible();
    }

    /**
     * Loads the mass-spring network graph from the masses file and the
     * connection map file, as configured in the {@link LoadGraphWindow}.
     */
    public void loadGraphFromFile() {
        String massesFilePath = loadGraphWindow.getMassesFilePath();
        String connectionMapFilePath = loadGraphWindow.getConnectionMapFilePath();

        try {
            String massesFile = FileUtils.readFileToString(new File(massesFilePath));
            String connectionMapFile = FileUtils.readFileToString(new File(connectionMapFilePath));
            NetworkGraph graph = GraphConverter.toNetworkGraph(massesFile, connectionMapFile);
            grammarWindow.getGrammarParser().setGraph(graph);
            grammarWindow.repaintGraphPanel();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.WARNING, "Could not load the graph since the files could not be loaded: {0}", ex);
            JOptionPane.showMessageDialog(grammarWindow, "Could not load the given files: " + ex.getMessage(), "File Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows the {@link LoadGraphWindow}. If there is no instance yet, one will
     * be created.
     */
    public void showLoadGraphWindow() {
        if (!isLoadGraphWindowVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    loadGraphWindow = new LoadGraphWindow();
                    loadGraphWindow.setController(App.controller);
                    Components.setIcons(loadGraphWindow);
                    loadGraphWindow.setLocationRelativeTo(grammarWindow);
                    loadGraphWindow.setVisible(true);
                    loadGraphWindow.requestFocus();
                    loadGraphWindow.toFront();
                }
            });
        }
    }

    /**
     * @return true, when the {@link LoadGraphWindow} is visible, false
     * otherwise.
     */
    public boolean isLoadGraphWindowVisible() {
        return loadGraphWindow != null && loadGraphWindow.isVisible();
    }

    /**
     * @return true, when the {@link GrammarWindow} is visible, false otherwise.
     */
    public boolean isGraphWindowVisible() {
        return aboutWindow != null && aboutWindow.isVisible();
    }

    /**
     * Shows the {@link WeightsCalculatorWindow}. If there is no instance yet,
     * one will be created.
     */
    public void showWeightsCalculatorWindow() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!isWeightsCalculatorWindowVisible()) {
                    weightsCalculatorWindow = new WeightsCalculatorWindow();
                    Components.setIcons(weightsCalculatorWindow);
                    weightsCalculatorWindow.setController(App.controller);
                    weightsCalculatorWindow.setLocationRelativeTo(mainWindow);
                    weightsCalculatorWindow.setVisible(true);
                }

                weightsCalculatorWindow.requestFocus();
                weightsCalculatorWindow.toFront();
            }
        });
    }

    /**
     * @return true, when the {@link WeightsCalculatorWindow} is visible, false
     * otherwise.
     */
    private boolean isWeightsCalculatorWindowVisible() {
        return weightsCalculatorWindow != null && weightsCalculatorWindow.isVisible();
    }

    /**
     * Calculates the weights configured in {@link WeightsCalculatorWindow} and
     * writes the result to the also configure output file.
     */
    public void calculateWeights() {
        weightCalulation = new Thread() {
            @Override
            public void run() {

                try {
                    long startTime = System.currentTimeMillis();

                    Weights.calculateWeights(
                            weightsCalculatorWindow.getSpringLengthsPath(),
                            weightsCalculatorWindow.getAnglesPath(),
                            weightsCalculatorWindow.getOutputPath(),
                            weightsCalculatorWindow.getNumberOfOffsetSteps());

                    weightsCalculatorWindow.configureGuiForCalculationPhase(false);
                    JOptionPane.showMessageDialog(weightsCalculatorWindow, "Calculation complete. The opartion took " + getDuration(startTime), "Complete", JOptionPane.INFORMATION_MESSAGE);
                } catch (HeadlessException ex) {
                    JOptionPane.showMessageDialog(weightsCalculatorWindow, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private String getDuration(long startTime) {
                long durationInMilliseconds = System.currentTimeMillis() - startTime;
                long durationInSeconds = durationInMilliseconds / 1000;

                return durationInSeconds + " s";
            }
        };

        SwingUtilities.invokeLater(weightCalulation);
    }

    /**
     * Shows the {@link AboutWindow}. If there is no instance yet, one will be
     * created.
     */
    public void showAboutWindow() {
        if (!isAboutWindowVisible()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    aboutWindow = new AboutWindow();
                    Components.setIcons(aboutWindow);
                    aboutWindow.setLocationRelativeTo(mainWindow);
                    aboutWindow.setVisible(true);
                    aboutWindow.requestFocus();
                    aboutWindow.toFront();
                }
            });
        }

    }

    /**
     * @return true, when the {@link AboutWindow} is visible, false otherwise.
     */
    public boolean isAboutWindowVisible() {
        return aboutWindow != null && aboutWindow.isVisible();
    }

    public void useGrammarWindowGraph() {
        NetworkGraph graph = grammarWindow.getGrammarParser().getGraph();
        String massesAsCsv = GraphConverter.toMassesCsv(graph);
        String connectionMapAsCsv = GraphConverter.toConnectionMapCsv(graph);
        String directory = grammarWindow.getDirectoryOfInitalisationFile();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());

        String massesFilename = getMassesFilename(directory, timestamp);
        String connectionMapFilename = getConnectionMapFilename(directory, timestamp);

        try {
            FileUtils.writeStringToFile(new File(massesFilename), massesAsCsv);
            FileUtils.writeStringToFile(new File(connectionMapFilename), connectionMapAsCsv);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(grammarWindow, "Could not store the file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        mainWindow.setMassesFilePath(massesFilename);
        mainWindow.setConnectionMapFilePath(connectionMapFilename);
    }

    private String getMassesFilename(String directory, String timestamp) {
        return directory + File.separator
                + GRAMMAR_GRAPH_MASSES_FILENAME_PREFIX
                + timestamp
                + GRAMMAR_GRAPH_OUTPUT_FILE_ENDING;
    }

    private String getConnectionMapFilename(String directory, String timestamp) {
        return directory + File.separator
                + GRAMMAR_GRAPH_CONNECTION_MAP_FILENAME_PREFIX
                + timestamp
                + GRAMMAR_GRAPH_OUTPUT_FILE_ENDING;
    }

    public void shutdown() {
        System.exit(0);
    }

    /**
     * Starts the simulation by invoking a new process of the physics control
     * toolbox.
     */
    public void runSimulation() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("[J] Invoking simulation.");
                int processExitValue = SIMULATOR_INVOCATION_PROBLEM_EXIT_CODE;

                try {
                    verifyBinaryState();

                    Process process = Runtime.getRuntime().exec(getAssembledCommand());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = reader.readLine();

                    while (line != null) {
                        System.out.println(line);
                        line = reader.readLine();
                    }

                    process.waitFor();
                    processExitValue = process.exitValue();
                } catch (IOException | InterruptedException ex) {
                    System.out.println("[J] The process led to an exception: " + ex.getMessage());
                    Dialogs.showErrorPane(mainWindow, "Error", "<html>Could not invoke the Physics Toolbox:<br />" + ex.getMessage() + "</html>");
                }

                System.out.println("[J] Simulation ended with the exit code " + processExitValue + ".");

                if (processExitValue == SIMULATOR_INVOCATION_PROBLEM_EXIT_CODE) {
                    System.out.println("[J] The exit code is associated with "
                            + "SIMULATOR_INVOCATION_PROBLEM_EXIT_CODE. Please "
                            + "check if there is anything wrong with the "
                            + "environment what could have let to an invocation "
                            + "problem of the mariumapp executable.");
                }

                mainWindow.configureGuiForRunningPhase(false);
            }
        });
    }

    private void verifyBinaryState() throws InterruptedException {
        File binary = new File(mainWindow.getExecutableFilePath());

        if (!binary.exists() || !binary.canExecute()) {
            throw new InterruptedException("The binary at '" + binary.getAbsolutePath() + "' has to be existing and executable.");
        } else {
            System.out.println("[J] The binary at '" + binary.getAbsolutePath() + "' seems to be executable.");
        }
    }

    /**
     * Assembles the arguments for the interface of the Physics Toolbox.
     *
     * @return The assembled command to run.
     */
    private String[] getAssembledCommand() {
        return new String[]{
            mainWindow.getExecutableFilePath(),
            mainWindow.getMassesFilePath(),
            mainWindow.getConnectionMapFilePath(),
            mainWindow.getMassCooridinatesFilePath(),
            mainWindow.getTrajectoryFilePath(),
            mainWindow.getWeightsFilePath(),
            String.valueOf(false),
            String.valueOf(mainWindow.isLearningPhase())
        };
    }

    public GrammarHelpWindow getGrammarHelpWindow() {
        return grammarHelpWindow;
    }

}
