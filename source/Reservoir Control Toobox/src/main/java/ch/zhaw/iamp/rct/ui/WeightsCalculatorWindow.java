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
import ch.zhaw.iamp.rct.util.Storage;
import ch.zhaw.iamp.rct.util.Components;
import ch.zhaw.iamp.rct.util.Dialogs;
import java.io.File;
import java.util.HashMap;
import javax.swing.JTextField;

public class WeightsCalculatorWindow extends javax.swing.JFrame {

    private enum StorageKey {

        outputPath,
        offset,
        anglesPath,
        weightsPath;
    }
    private static final long serialVersionUID = 1L;
    public final static String STORAGE_FILENAME = "weightsCalculatorWindow.obj";
    private Controller controller;
    HashMap<String, String> storageValues;
    Storage storage;

    public WeightsCalculatorWindow() {
        initComponents();
        createStorage();
        restoreValues();
    }

    @SuppressWarnings({"unchecked"})
    private void createStorage() {
        storage = new Storage(App.getConfigDirectoryPath() + STORAGE_FILENAME);
        storageValues = new HashMap<>();

        File storageFile = new File(App.getConfigDirectoryPath() + STORAGE_FILENAME);

        if (storageFile.exists()) {
            storageValues = (HashMap< String, String>) storage.restore();
        } else {
            storage.store(storageValues);
        }
    }

    private void restoreValues() {
        for (StorageKey key : StorageKey.values()) {
            if (!storageValues.containsKey(key.toString())) {
                continue;
            }

            switch (key) {
                case outputPath:
                    springLenghtsTextField.setText(storageValues.get(key.toString()));
                    Components.moveCurserToEndOfTextField(springLenghtsTextField);
                    break;
                case offset:
                    offsetTextField.setText(storageValues.get(key.toString()));
                    Components.moveCurserToEndOfTextField(offsetTextField);
                    break;
                case anglesPath:
                    anglesFileTextField.setText(storageValues.get(key.toString()));
                    Components.moveCurserToEndOfTextField(anglesFileTextField);
                    break;
                case weightsPath:
                    outputFileTextField.setText(storageValues.get(key.toString()));
                    Components.moveCurserToEndOfTextField(outputFileTextField);
                    break;
            }
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourcePanel = new javax.swing.JPanel();
        springLengthsFileLabel = new javax.swing.JLabel();
        springLenghtsTextField = new javax.swing.JTextField();
        springLenghtsSelectButton = new javax.swing.JButton();
        springLengthsInfoLabel = new javax.swing.JLabel();
        offsetLabel = new javax.swing.JLabel();
        offsetTextField = new javax.swing.JTextField();
        offsetExplanationLabel = new javax.swing.JLabel();
        offsetInfoLabel = new javax.swing.JLabel();
        anglesLabel = new javax.swing.JLabel();
        anglesFileTextField = new javax.swing.JTextField();
        anglesSelectButton = new javax.swing.JButton();
        anglesInfoLabel = new javax.swing.JLabel();
        inputToWeightsFiller = new javax.swing.Box.Filler(new java.awt.Dimension(0, 3), new java.awt.Dimension(0, 3), new java.awt.Dimension(32767, 3));
        calculationPanel = new javax.swing.JPanel();
        outputLabel = new javax.swing.JLabel();
        outputFileTextField = new javax.swing.JTextField();
        outputSelectButton = new javax.swing.JButton();
        outputInfoLabel = new javax.swing.JLabel();
        centererPanel = new javax.swing.JPanel();
        calculateButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Weights Calculator");
        setLocationByPlatform(true);
        setResizable(false);

        sourcePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input"));

        springLengthsFileLabel.setText("Spring Lengths:");
        springLengthsFileLabel.setMaximumSize(new java.awt.Dimension(150, 15));
        springLengthsFileLabel.setMinimumSize(new java.awt.Dimension(150, 15));
        springLengthsFileLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        springLenghtsTextField.setText("devArm/output.csv");

        springLenghtsSelectButton.setText("Select...");
        springLenghtsSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                springLenghtsSelectButtonActionPerformed(evt);
            }
        });

        springLengthsInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        springLengthsInfoLabel.setToolTipText("<html>This file has to contain the length values of the network springs, generated during <b>Learning Phase</b>.</html>");

        offsetLabel.setText("Offset:");

        offsetTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        offsetTextField.setText("1000");

        offsetExplanationLabel.setText("first steps to ignore");
        offsetExplanationLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        offsetInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        offsetInfoLabel.setToolTipText("Use a value equals or greater than the length of the fading memory of the used mass-spring network. If this value is not available, the length of one (1) cycle of the target trajectory has also shown to be a good choice.");

        anglesLabel.setText("Angles:");

        anglesFileTextField.setText("devArm/angles.csv");

        anglesSelectButton.setText("Select...");
        anglesSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anglesSelectButtonActionPerformed(evt);
            }
        });

        anglesInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        anglesInfoLabel.setToolTipText("<html>Use the same angles file that was used to generate the output file above.</html>");

        javax.swing.GroupLayout sourcePanelLayout = new javax.swing.GroupLayout(sourcePanel);
        sourcePanel.setLayout(sourcePanelLayout);
        sourcePanelLayout.setHorizontalGroup(
            sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(springLengthsFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(offsetLabel)
                    .addComponent(anglesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sourcePanelLayout.createSequentialGroup()
                        .addComponent(offsetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(offsetExplanationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(offsetInfoLabel))
                    .addGroup(sourcePanelLayout.createSequentialGroup()
                        .addComponent(anglesFileTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anglesSelectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(anglesInfoLabel))
                    .addGroup(sourcePanelLayout.createSequentialGroup()
                        .addComponent(springLenghtsTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(springLenghtsSelectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(springLengthsInfoLabel)))
                .addContainerGap())
        );
        sourcePanelLayout.setVerticalGroup(
            sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sourcePanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(springLengthsInfoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(springLengthsFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(springLenghtsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(springLenghtsSelectButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(offsetInfoLabel)
                    .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(offsetLabel)
                        .addComponent(offsetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(offsetExplanationLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(anglesInfoLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, sourcePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(anglesLabel)
                        .addComponent(anglesFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(anglesSelectButton)))
                .addGap(6, 6, 6))
        );

        calculationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Weights Calculation"));

        outputLabel.setText("Output:");
        outputLabel.setMaximumSize(new java.awt.Dimension(104, 15));
        outputLabel.setMinimumSize(new java.awt.Dimension(104, 15));
        outputLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        outputFileTextField.setText("devArm/weights.csv");

        outputSelectButton.setText("Select...");
        outputSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputSelectButtonActionPerformed(evt);
            }
        });

        outputInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/information.png"))); // NOI18N
        outputInfoLabel.setToolTipText("<html>The calculeated weights will be stored into this file. Afterwards, it can be used as <b>Execution Curve</b> when running in execution phase.</html>");

        calculateButton.setText("Calculate");
        calculateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculateButtonActionPerformed(evt);
            }
        });
        centererPanel.add(calculateButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        centererPanel.add(closeButton);

        javax.swing.GroupLayout calculationPanelLayout = new javax.swing.GroupLayout(calculationPanel);
        calculationPanel.setLayout(calculationPanelLayout);
        calculationPanelLayout.setHorizontalGroup(
            calculationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calculationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(calculationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(centererPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(calculationPanelLayout.createSequentialGroup()
                        .addComponent(outputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outputFileTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outputSelectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outputInfoLabel)))
                .addContainerGap())
        );
        calculationPanelLayout.setVerticalGroup(
            calculationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calculationPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(calculationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(outputSelectButton)
                    .addComponent(outputInfoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(centererPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inputToWeightsFiller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(293, 293, 293))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourcePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(calculationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sourcePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputToWeightsFiller, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calculationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculateButtonActionPerformed
        configureGuiForCalculationPhase(true);

        String outputFile = springLenghtsTextField.getText();
        String anglesFile = anglesFileTextField.getText();
        String correctionsFile = outputFileTextField.getText();

        if (isFileAccessible(outputFile)
                && isFileAccessible(anglesFile)
                && isParentFolderAccessible(correctionsFile)
                && containsValidInteger(offsetTextField)) {
            controller.calculateWeights();
        } else {
            configureGuiForCalculationPhase(false);
        }
    }//GEN-LAST:event_calculateButtonActionPerformed

    private boolean isFileAccessible(String path) {
        if (path == null) {
            return false;
        }

        File file = new File(path);
        return file.exists();
    }

    private boolean isParentFolderAccessible(String path) {
        if (path == null) {
            return false;
        }

        File file = new File(path);
        return new File(file.getParent()).exists();
    }

    private boolean containsValidInteger(JTextField field) {
        String content = field.getText();

        try {
            int value = Integer.parseInt(content);
            return value > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public void configureGuiForCalculationPhase(boolean inCalculationPhase) {
        springLenghtsTextField.setEnabled(!inCalculationPhase);
        springLenghtsSelectButton.setEnabled(!inCalculationPhase);

        offsetTextField.setEnabled(!inCalculationPhase);

        anglesFileTextField.setEnabled(!inCalculationPhase);
        anglesSelectButton.setEnabled(!inCalculationPhase);

        outputFileTextField.setEnabled(!inCalculationPhase);
        outputSelectButton.setEnabled(!inCalculationPhase);

        calculateButton.setEnabled(!inCalculationPhase);
    }

    public String getSpringLengthsPath() {
        return springLenghtsTextField.getText();
    }

    public int getNumberOfOffsetSteps() {
        return Integer.parseInt(offsetTextField.getText());
    }

    public String getAnglesPath() {
        return anglesFileTextField.getText();
    }

    public String getOutputPath() {
        return outputFileTextField.getText();
    }

    private void springLenghtsSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_springLenghtsSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this,springLenghtsTextField.getText(), false);

        if (selection != null) {
            springLenghtsTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_springLenghtsSelectButtonActionPerformed

    private void outputSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, outputFileTextField.getText(), false);

        if (selection != null) {
            outputFileTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_outputSelectButtonActionPerformed

    private void anglesSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anglesSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, anglesFileTextField.getText(), false);

        if (selection != null) {
            anglesFileTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_anglesSelectButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    @Override
    public void dispose() {
        storeValues();
        super.dispose();
    }

    private void storeValues() {
        storageValues.put(StorageKey.outputPath.toString(), springLenghtsTextField.getText());
        storageValues.put(StorageKey.offset.toString(), offsetTextField.getText());
        storageValues.put(StorageKey.anglesPath.toString(), anglesFileTextField.getText());
        storageValues.put(StorageKey.weightsPath.toString(), outputFileTextField.getText());

        storage.store(storageValues);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anglesFileTextField;
    private javax.swing.JLabel anglesInfoLabel;
    private javax.swing.JLabel anglesLabel;
    private javax.swing.JButton anglesSelectButton;
    private javax.swing.JButton calculateButton;
    private javax.swing.JPanel calculationPanel;
    private javax.swing.JPanel centererPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.Box.Filler inputToWeightsFiller;
    private javax.swing.JLabel offsetExplanationLabel;
    private javax.swing.JLabel offsetInfoLabel;
    private javax.swing.JLabel offsetLabel;
    private javax.swing.JTextField offsetTextField;
    private javax.swing.JTextField outputFileTextField;
    private javax.swing.JLabel outputInfoLabel;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JButton outputSelectButton;
    private javax.swing.JPanel sourcePanel;
    private javax.swing.JButton springLenghtsSelectButton;
    private javax.swing.JTextField springLenghtsTextField;
    private javax.swing.JLabel springLengthsFileLabel;
    private javax.swing.JLabel springLengthsInfoLabel;
    // End of variables declaration//GEN-END:variables
}
