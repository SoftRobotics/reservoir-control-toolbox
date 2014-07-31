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

import ch.zhaw.iamp.rct.App;
import ch.zhaw.iamp.rct.Controller;
import ch.zhaw.iamp.rct.util.Components;
import static ch.zhaw.iamp.rct.util.Components.DEFAULT_BACKGROUND_COLOR;
import static ch.zhaw.iamp.rct.util.Components.ERROR_BACKGROUND_COLOR;
import ch.zhaw.iamp.rct.util.Dialogs;
import ch.zhaw.iamp.rct.util.Storage;
import ch.zhaw.iamp.rct.util.StorageException;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JTextField;

/**
 * This is the main window of the graphical configuration tool.
 */
public class MainWindow extends javax.swing.JFrame {

    private enum StorageKey {

        WINDOW_X_POSITION,
        WINDOW_Y_POSITION,
        MASS_POINTS_PATH,
        CONNECTION_MAP_PATH,
        LEARNING_PHASE,
        EXECUTION_PHASE,
        TARGET_CURVE_PATH,
        MASS_COORDINATES_PATH,
        WEIGHTS_FILE,
        EXECUTABLE_FILE;
    }
    private static final long serialVersionUID = 1L;
    public final static String STORAGE_FILENAME = "mainWindow.obj";
    private final String RUN_BUTTON_DEFAULT_TEXT = "Run";
    private final String RUN_BUTTON_LOCKED_TEXT = "Running";
    Controller controller;
    HashMap<StorageKey, String> textFieldValues;
    Storage textFieldStorage;

    public MainWindow() {
        initComponents();
        centerPosition();
        createStorage();
        restoreValues();
    }

    private void centerPosition() {
        setLocationRelativeTo(null);
    }

    @SuppressWarnings({"unchecked"})
    private void createStorage() {
        textFieldStorage = new Storage(App.getConfigDirectoryPath() + STORAGE_FILENAME);
        textFieldValues = new HashMap<>();

        File storage = new File(App.getConfigDirectoryPath() + STORAGE_FILENAME);

        if (storage.exists()) {
            try {
                textFieldValues = (HashMap<StorageKey, String>) textFieldStorage.restore();
            } catch (StorageException ex) {
                System.out.println("Warning: Could not restore the configuration. Use defaults instead.");
            }
        } else {
            textFieldStorage.store(textFieldValues);
        }
    }

    private void restoreValues() {
        for (StorageKey key : StorageKey.values()) {
            if (!textFieldValues.containsKey(key)) {
                continue;
            }

            switch (key) {
                case WINDOW_X_POSITION:
                    String xPositionAsString = textFieldValues.get(key);

                    try {
                        int xPosition = Integer.parseInt(xPositionAsString);
                        setLocation(xPosition, getY());
                    } catch (NumberFormatException ex) {
                    }
                    break;
                case WINDOW_Y_POSITION:
                    String yPositionAsString = textFieldValues.get(key);

                    try {
                        int yPosition = Integer.parseInt(yPositionAsString);
                        setLocation(getX(), yPosition);
                    } catch (NumberFormatException ex) {
                    }
                    break;
                case LEARNING_PHASE:
                    boolean isLearningPhaseSelected = "true".equals(textFieldValues.get(key));
                    learningRadioButton.setSelected(isLearningPhaseSelected);

                    if (isLearningPhaseSelected) {
                        configureMovementLabelsForLearningPhase();
                    }
                    break;
                case EXECUTION_PHASE:
                    boolean isExecutionPhaseSelected = "true".equals(textFieldValues.get(key));
                    executionRadioButton.setSelected(isExecutionPhaseSelected);

                    if (isExecutionPhaseSelected) {
                        configureMovementLabelsForExecutionPhase();
                    }
                    break;
                case TARGET_CURVE_PATH:
                    trajectoryTextField.setText(textFieldValues.get(key));
                    Components.moveCurserToEndOfTextField(trajectoryTextField);
                    break;
                case MASS_POINTS_PATH:
                    massesTextField.setText(textFieldValues.get(key));
                    Components.moveCurserToEndOfTextField(massesTextField);
                    break;
                case CONNECTION_MAP_PATH:
                    connectionMapTextField.setText(textFieldValues.get(key));
                    Components.moveCurserToEndOfTextField(connectionMapTextField);
                    break;
                case MASS_COORDINATES_PATH:
                    springLengthsTextField.setText(textFieldValues.get(key));
                    Components.moveCurserToEndOfTextField(springLengthsTextField);
                    break;
                case WEIGHTS_FILE:
                    weightsTextField.setText(textFieldValues.get(key));
                    Components.moveCurserToEndOfTextField(weightsTextField);
                    break;
                case EXECUTABLE_FILE:
                    executableTextField.setText(textFieldValues.get(key));
                    Components.moveCurserToEndOfTextField(executableTextField);
                    break;
            }
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void configureGuiForRunningPhase(boolean isRunning) {
        boolean isEnabled = !isRunning;

        runButton.setEnabled(isEnabled);
        runButton.setText(isEnabled ? RUN_BUTTON_DEFAULT_TEXT : RUN_BUTTON_LOCKED_TEXT);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        phaseButtonGroup = new javax.swing.ButtonGroup();
        basePanel = new javax.swing.JPanel();
        constructionPanel = new javax.swing.JPanel();
        massesLabel = new javax.swing.JLabel();
        massesTextField = new javax.swing.JTextField();
        massesSelectButton = new javax.swing.JButton();
        massesInfoLabel = new javax.swing.JLabel();
        connectionMapLabel = new javax.swing.JLabel();
        connectionMapTextField = new javax.swing.JTextField();
        connectionMapSelectButton = new javax.swing.JButton();
        connectionMapInfoLabel = new javax.swing.JLabel();
        armToMovementFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 15));
        simulationPanel = new javax.swing.JPanel();
        phaseLabel = new javax.swing.JLabel();
        learningRadioButton = new javax.swing.JRadioButton();
        executionRadioButton = new javax.swing.JRadioButton();
        trajectoryLabel = new javax.swing.JLabel();
        trajectoryTextField = new javax.swing.JTextField();
        trajectorySelectButton = new javax.swing.JButton();
        trajectoryInfoLabel = new javax.swing.JLabel();
        springLenghtsLabel = new javax.swing.JLabel();
        springLengthsTextField = new javax.swing.JTextField();
        springLengthsSelectButton = new javax.swing.JButton();
        springLengthsInfoLabel = new javax.swing.JLabel();
        weigthsLabel = new javax.swing.JLabel();
        weightsTextField = new javax.swing.JTextField();
        weightsSelectButton = new javax.swing.JButton();
        weightsInfoLabel = new javax.swing.JLabel();
        executableLabel = new javax.swing.JLabel();
        executableTextField = new javax.swing.JTextField();
        executableSelectButton = new javax.swing.JButton();
        executableInfoLabel = new javax.swing.JLabel();
        runPanel = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        outputToButtonFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem quitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        graphGeneratorMenu = new javax.swing.JMenuItem();
        weightsCalculatorMenu = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reservoir Control Toolbox");
        setResizable(false);

        basePanel.setLayout(new javax.swing.BoxLayout(basePanel, javax.swing.BoxLayout.PAGE_AXIS));

        constructionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Robot Arm Construction"));

        massesLabel.setText("Masses:");
        massesLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        massesTextField.setText("devArm/masses.csv");

        massesSelectButton.setText("Select...");
        massesSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                massesSelectButtonActionPerformed(evt);
            }
        });

        massesInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        massesInfoLabel.setToolTipText("<html>Contains all masses of the robot arm, i.e. shoulder, elbow, end effector, inputs and network masses.</html>");

        connectionMapLabel.setText("Connection Map:");
        connectionMapLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        connectionMapTextField.setText("devArm/connectionMap.csv");

        connectionMapSelectButton.setText("Select...");
        connectionMapSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionMapSelectButtonActionPerformed(evt);
            }
        });

        connectionMapInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        connectionMapInfoLabel.setToolTipText("<html>Contains the connections of the mass-spring network. This also defines of which type a connection is.</html>");

        javax.swing.GroupLayout constructionPanelLayout = new javax.swing.GroupLayout(constructionPanel);
        constructionPanel.setLayout(constructionPanelLayout);
        constructionPanelLayout.setHorizontalGroup(
            constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(constructionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connectionMapLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(massesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connectionMapTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                    .addComponent(massesTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, constructionPanelLayout.createSequentialGroup()
                        .addComponent(massesSelectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(massesInfoLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, constructionPanelLayout.createSequentialGroup()
                        .addComponent(connectionMapSelectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectionMapInfoLabel)))
                .addContainerGap())
        );
        constructionPanelLayout.setVerticalGroup(
            constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(constructionPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(massesLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(massesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(massesSelectButton)
                        .addComponent(massesInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(constructionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(connectionMapLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(connectionMapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(connectionMapSelectButton))
                    .addComponent(connectionMapInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        basePanel.add(constructionPanel);
        constructionPanel.getAccessibleContext().setAccessibleName("Configuration");

        basePanel.add(armToMovementFiller);

        simulationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulation"));

        phaseLabel.setText("Phase:");
        phaseLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        phaseButtonGroup.add(learningRadioButton);
        learningRadioButton.setSelected(true);
        learningRadioButton.setText("Learning");
        learningRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                learningRadioButtonItemStateChanged(evt);
            }
        });

        phaseButtonGroup.add(executionRadioButton);
        executionRadioButton.setText("Execution");
        executionRadioButton.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                executionRadioButtonItemStateChanged(evt);
            }
        });

        trajectoryLabel.setText("Trajectory:");
        trajectoryLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        trajectoryTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        trajectoryTextField.setText("devArm/angles.csv");
        trajectoryTextField.setMaximumSize(new java.awt.Dimension(2147483647, 22));

        trajectorySelectButton.setText("Select...");
        trajectorySelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trajectorySelectButtonActionPerformed(evt);
            }
        });

        trajectoryInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        trajectoryInfoLabel.setToolTipText("<html>This file has to contain angle values for both torques of the robot arm to form the target trajectory.</html>");

        springLenghtsLabel.setText("Spring Lengths Output:");
        springLenghtsLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        springLengthsTextField.setText("devArm/output.csv");

        springLengthsSelectButton.setText("Select...");
        springLengthsSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                springLengthsSelectButtonActionPerformed(evt);
            }
        });

        springLengthsInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        springLengthsInfoLabel.setToolTipText("<html>The lengths of all springs will continuously be written to this file when the simulation is running.</html>");

        weigthsLabel.setText("Weights:");
        weigthsLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        weightsTextField.setText("devArm/weights.csv");

        weightsSelectButton.setText("Select...");
        weightsSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightsSelectButtonActionPerformed(evt);
            }
        });

        weightsInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        weightsInfoLabel.setToolTipText("Enter the output file of the Weights Calculator.");

        executableLabel.setText("Physics Toolbox Executable:");

        executableTextField.setText("physics_toolbox");

        executableSelectButton.setText("Select...");
        executableSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executableSelectButtonActionPerformed(evt);
            }
        });

        executableInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        executableInfoLabel.setToolTipText("<html>Select the executable of the Physics Toolbox. Typically, this lies in the <b>dist</b> folder of the Physics Toolbox project.</html>");

        runButton.setText("Run");
        runButton.setMargin(new java.awt.Insets(10, 20, 10, 20));
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        runPanel.add(runButton);

        javax.swing.GroupLayout simulationPanelLayout = new javax.swing.GroupLayout(simulationPanel);
        simulationPanel.setLayout(simulationPanelLayout);
        simulationPanelLayout.setHorizontalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(runPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(simulationPanelLayout.createSequentialGroup()
                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(trajectoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(springLenghtsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(weigthsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(executableLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(simulationPanelLayout.createSequentialGroup()
                                .addComponent(learningRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(executionRadioButton)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(simulationPanelLayout.createSequentialGroup()
                                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(executableTextField)
                                    .addComponent(trajectoryTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(springLengthsTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(weightsTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulationPanelLayout.createSequentialGroup()
                                        .addComponent(trajectorySelectButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(trajectoryInfoLabel))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulationPanelLayout.createSequentialGroup()
                                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(weightsSelectButton)
                                            .addComponent(springLengthsSelectButton))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(springLengthsInfoLabel)
                                            .addComponent(weightsInfoLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, simulationPanelLayout.createSequentialGroup()
                                        .addComponent(executableSelectButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(executableInfoLabel)))))))
                .addContainerGap())
        );
        simulationPanelLayout.setVerticalGroup(
            simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(simulationPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(learningRadioButton)
                    .addComponent(executionRadioButton)
                    .addComponent(phaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(trajectoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trajectoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trajectorySelectButton)
                    .addComponent(trajectoryInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(simulationPanelLayout.createSequentialGroup()
                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(springLenghtsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(springLengthsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(springLengthsSelectButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(weigthsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(weightsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(weightsSelectButton))
                            .addComponent(weightsInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(simulationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(executableLabel)
                            .addComponent(executableTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(executableSelectButton)
                            .addComponent(executableInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(springLengthsInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(runPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        basePanel.add(simulationPanel);
        basePanel.add(outputToButtonFiller);

        fileMenu.setText("File");

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        toolsMenu.setText("Tools");

        graphGeneratorMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        graphGeneratorMenu.setText("Graph Generator");
        graphGeneratorMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                graphGeneratorMenuActionPerformed(evt);
            }
        });
        toolsMenu.add(graphGeneratorMenu);

        weightsCalculatorMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        weightsCalculatorMenu.setText("Weights Calculator");
        weightsCalculatorMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightsCalculatorMenuActionPerformed(evt);
            }
        });
        toolsMenu.add(weightsCalculatorMenu);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");

        aboutMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        aboutMenu.setText("About...");
        aboutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenu);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(basePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        controller.shutdown();
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void springLengthsSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_springLengthsSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, springLengthsTextField.getText(), false);

        if (selection != null) {
            springLengthsTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_springLengthsSelectButtonActionPerformed

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        verifyInputFileValues();
        verifyOutputFileValues();

        if (isCompleteInputValid()) {
            configureGuiForRunningPhase(true);
            controller.runSimulation();
        }
    }//GEN-LAST:event_runButtonActionPerformed

    private void connectionMapSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionMapSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, connectionMapTextField.getText(), false);

        if (selection != null) {
            connectionMapTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_connectionMapSelectButtonActionPerformed

    private void massesSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_massesSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, massesTextField.getText(), false);

        if (selection != null) {
            massesTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_massesSelectButtonActionPerformed

    private void graphGeneratorMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_graphGeneratorMenuActionPerformed
        controller.showGrammarWindow();
    }//GEN-LAST:event_graphGeneratorMenuActionPerformed

    private void trajectorySelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trajectorySelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, trajectoryTextField.getText(), false);

        if (selection != null) {
            trajectoryTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_trajectorySelectButtonActionPerformed

    private void executionRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_executionRadioButtonItemStateChanged
        if (evt.getItemSelectable().getSelectedObjects() != null) {
            configureMovementLabelsForExecutionPhase();
        }
    }//GEN-LAST:event_executionRadioButtonItemStateChanged

    private void learningRadioButtonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_learningRadioButtonItemStateChanged
        if (evt.getItemSelectable().getSelectedObjects() != null) {
            configureMovementLabelsForLearningPhase();
        }
    }//GEN-LAST:event_learningRadioButtonItemStateChanged

    private void aboutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuActionPerformed
        controller.showAboutWindow();
    }//GEN-LAST:event_aboutMenuActionPerformed

    private void weightsCalculatorMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightsCalculatorMenuActionPerformed
        controller.showWeightsCalculatorWindow();
    }//GEN-LAST:event_weightsCalculatorMenuActionPerformed

    private void weightsSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightsSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, weightsTextField.getText(), false);

        if (selection != null) {
            weightsTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_weightsSelectButtonActionPerformed

    private void executableSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executableSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, executableTextField.getText(), true);

        if (selection != null) {
            executableTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_executableSelectButtonActionPerformed

    private void configureMovementLabelsForExecutionPhase() {
        trajectoryLabel.setText("Trajectory:");
        trajectoryInfoLabel.setToolTipText("<html>This file has to contains "
                + "<b>weighted</b> angle values for both torques of the robot, "
                + "calculated via <b>Weights Calculator</b>.</html>");

        weigthsLabel.setEnabled(true);
        weightsTextField.setEnabled(true);
        weightsSelectButton.setEnabled(true);
        weightsInfoLabel.setEnabled(true);
    }

    private void configureMovementLabelsForLearningPhase() {
        trajectoryLabel.setText("Trajectory:");
        trajectoryInfoLabel.setToolTipText("<html>This file has to contain angle "
                + "values for both torques of the robot arm to form the target "
                + "trajectory.</html>");

        weigthsLabel.setEnabled(false);
        weightsTextField.setEnabled(false);
        weightsSelectButton.setEnabled(false);
        weightsInfoLabel.setEnabled(false);
    }

    private void verifyInputFileValues() {
        LinkedList<JTextField> fields = new LinkedList<>();
        fields.add(massesTextField);
        fields.add(connectionMapTextField);
        fields.add(trajectoryTextField);
        fields.add(executableTextField);

        if (isExecutionPhase()) {
            fields.add(weightsTextField);
        }

        for (JTextField field : fields) {
            if (isInputFileAccessable(field.getText())) {
                setDefalutBackground(field);
            } else {
                setErrorBackground(field);
            }

        }
    }

    private void verifyOutputFileValues() {
        if (isOutputFileAccessable(springLengthsTextField.getText())) {
            setDefalutBackground(springLengthsTextField);
        } else {
            setErrorBackground(springLengthsTextField);
        }
    }

    static boolean isInputFileAccessable(String file) {
        if (file == null) {
            return false;
        }

        File inputFile = new File(file);
        return inputFile.exists();
    }

    static boolean isOutputFileAccessable(String file) {
        if (file == null) {
            return false;
        }

        File outputFile = new File(file).getAbsoluteFile();

        if (outputFile.exists()) {
            return outputFile.canWrite() && outputFile.isFile();
        } else {
            return outputFile.getParentFile().canWrite();
        }
    }

    private void setErrorBackground(JTextField textField) {
        textField.setBackground(ERROR_BACKGROUND_COLOR);
    }

    private void setDefalutBackground(JTextField textField) {
        textField.setBackground(DEFAULT_BACKGROUND_COLOR);
    }

    private boolean isCompleteInputValid() {
        LinkedList<JTextField> fields = new LinkedList<>();
        fields.add(massesTextField);
        fields.add(connectionMapTextField);
        fields.add(trajectoryTextField);
        fields.add(springLengthsTextField);

        if (isExecutionPhase()) {
            fields.add(springLengthsTextField);
        }

        for (JTextField field : fields) {
            if (ERROR_BACKGROUND_COLOR.equals(field.getBackground())) {
                return false;
            }
        }

        return true;
    }

    public boolean isLearningPhase() {
        return learningRadioButton.isSelected();
    }

    public boolean isExecutionPhase() {
        return !isLearningPhase();
    }

    public String getTrajectoryFilePath() {
        return trajectoryTextField.getText();
    }

    public String getMassesFilePath() {
        return new File(massesTextField.getText()).getAbsolutePath();
    }

    public void setMassesFilePath(String path) {
        massesTextField.setText(path);
    }

    public String getConnectionMapFilePath() {
        return new File(connectionMapTextField.getText()).getAbsolutePath();
    }

    public void setConnectionMapFilePath(String path) {
        connectionMapTextField.setText(path);
    }

    public String getMassCooridinatesFilePath() {
        return new File(springLengthsTextField.getText()).getAbsolutePath();
    }

    public void setMassCooridinatesFilePath(String path) {
        springLengthsTextField.setText(path);
    }

    public String getWeightsFilePath() {
        return new File(weightsTextField.getText()).getAbsolutePath();
    }

    public void setWeightsFilePath(String path) {
        weightsTextField.setText(path);
    }

    public String getExecutableFilePath() {
        return new File(executableTextField.getText()).getAbsolutePath();
    }

    public void setExecutableFilePath(String path) {
        executableTextField.setText(path);
    }

    @Override
    public void dispose() {
        storeValues();
        super.dispose();
        controller.shutdown();
    }

    private void storeValues() {
        textFieldValues.put(StorageKey.WINDOW_X_POSITION, "" + getX());
        textFieldValues.put(StorageKey.WINDOW_Y_POSITION, "" + getY());
        textFieldValues.put(StorageKey.LEARNING_PHASE, "" + learningRadioButton.isSelected());
        textFieldValues.put(StorageKey.EXECUTION_PHASE, "" + executionRadioButton.isSelected());
        textFieldValues.put(StorageKey.TARGET_CURVE_PATH, getTrajectoryFilePath());
        textFieldValues.put(StorageKey.MASS_POINTS_PATH, getMassesFilePath());
        textFieldValues.put(StorageKey.CONNECTION_MAP_PATH, getConnectionMapFilePath());
        textFieldValues.put(StorageKey.MASS_COORDINATES_PATH, getMassCooridinatesFilePath());
        textFieldValues.put(StorageKey.WEIGHTS_FILE, getWeightsFilePath());
        textFieldValues.put(StorageKey.EXECUTABLE_FILE, getExecutableFilePath());

        textFieldStorage.store(textFieldValues);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.Box.Filler armToMovementFiller;
    private javax.swing.JPanel basePanel;
    private javax.swing.JLabel connectionMapInfoLabel;
    private javax.swing.JLabel connectionMapLabel;
    private javax.swing.JButton connectionMapSelectButton;
    private javax.swing.JTextField connectionMapTextField;
    private javax.swing.JPanel constructionPanel;
    private javax.swing.JLabel executableInfoLabel;
    private javax.swing.JLabel executableLabel;
    private javax.swing.JButton executableSelectButton;
    private javax.swing.JTextField executableTextField;
    private javax.swing.JRadioButton executionRadioButton;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem graphGeneratorMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JRadioButton learningRadioButton;
    private javax.swing.JLabel massesInfoLabel;
    private javax.swing.JLabel massesLabel;
    private javax.swing.JButton massesSelectButton;
    private javax.swing.JTextField massesTextField;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.Box.Filler outputToButtonFiller;
    private javax.swing.ButtonGroup phaseButtonGroup;
    private javax.swing.JLabel phaseLabel;
    javax.swing.JButton runButton;
    private javax.swing.JPanel runPanel;
    private javax.swing.JPanel simulationPanel;
    private javax.swing.JLabel springLenghtsLabel;
    private javax.swing.JLabel springLengthsInfoLabel;
    private javax.swing.JButton springLengthsSelectButton;
    private javax.swing.JTextField springLengthsTextField;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JLabel trajectoryInfoLabel;
    private javax.swing.JLabel trajectoryLabel;
    private javax.swing.JButton trajectorySelectButton;
    private javax.swing.JTextField trajectoryTextField;
    private javax.swing.JMenuItem weightsCalculatorMenu;
    private javax.swing.JLabel weightsInfoLabel;
    private javax.swing.JButton weightsSelectButton;
    private javax.swing.JTextField weightsTextField;
    private javax.swing.JLabel weigthsLabel;
    // End of variables declaration//GEN-END:variables
}
