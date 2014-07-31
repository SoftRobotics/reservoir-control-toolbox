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

import ch.zhaw.iamp.rct.Controller;
import static ch.zhaw.iamp.rct.util.Components.DEFAULT_BACKGROUND_COLOR;
import static ch.zhaw.iamp.rct.util.Components.ERROR_BACKGROUND_COLOR;
import ch.zhaw.iamp.rct.util.Dialogs;
import java.io.File;
import javax.swing.JTextField;

public class LoadGraphWindow extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private Controller controller;

    public LoadGraphWindow() {
        initComponents();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getMassesFilePath() {
        return new File(massesTextField.getText()).getAbsolutePath();
    }

    public String getConnectionMapFilePath() {
        return new File(connectionMapTextField.getText()).getAbsolutePath();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        massesLabel = new javax.swing.JLabel();
        massesTextField = new javax.swing.JTextField();
        massesSelectButton = new javax.swing.JButton();
        connectionMapLabel = new javax.swing.JLabel();
        connectionMapTextField = new javax.swing.JTextField();
        connectionMapSelectButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Graph...");
        setResizable(false);

        massesLabel.setText("Masses:");
        massesLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        massesSelectButton.setText("Select...");
        massesSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                massesSelectButtonActionPerformed(evt);
            }
        });

        connectionMapLabel.setText("Connection Map:");
        connectionMapLabel.setPreferredSize(new java.awt.Dimension(150, 15));

        connectionMapSelectButton.setText("Select...");
        connectionMapSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionMapSelectButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        loadButton.setText("Load");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(massesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(massesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(massesSelectButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(connectionMapLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectionMapTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectionMapSelectButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(loadButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(massesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(massesSelectButton)
                    .addComponent(massesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectionMapLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectionMapSelectButton)
                    .addComponent(connectionMapTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(loadButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void massesSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_massesSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, massesTextField.getText(), false);

        if (selection != null) {
            massesTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_massesSelectButtonActionPerformed

    private void connectionMapSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionMapSelectButtonActionPerformed
        File selection = Dialogs.selectFileFromDialog(this, connectionMapTextField.getText(), false);

        if (selection != null) {
            connectionMapTextField.setText(selection.getAbsolutePath());
        }
    }//GEN-LAST:event_connectionMapSelectButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        verifyInputFileValues();

        if (isCompleteInputValid()) {
            controller.loadGraphFromFile();
            dispose();
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void verifyInputFileValues() {
        if (isInputFileAccessable(massesTextField.getText())) {
            setDefalutBackground(massesTextField);
        } else {
            setErrorBackground(massesTextField);
        }

        if (isInputFileAccessable(connectionMapTextField.getText())) {
            setDefalutBackground(connectionMapTextField);
        } else {
            setErrorBackground(connectionMapTextField);
        }
    }

    private void setErrorBackground(JTextField textField) {
        textField.setBackground(ERROR_BACKGROUND_COLOR);
    }

    private void setDefalutBackground(JTextField textField) {
        textField.setBackground(DEFAULT_BACKGROUND_COLOR);
    }

    static boolean isInputFileAccessable(String file) {
        if (file == null) {
            return false;
        }

        File inputFile = new File(file);
        return inputFile.exists();
    }

    private boolean isCompleteInputValid() {
        return massesTextField.getBackground() != ERROR_BACKGROUND_COLOR
                && connectionMapTextField.getBackground() != ERROR_BACKGROUND_COLOR;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel connectionMapLabel;
    private javax.swing.JButton connectionMapSelectButton;
    private javax.swing.JTextField connectionMapTextField;
    private javax.swing.JButton loadButton;
    private javax.swing.JLabel massesLabel;
    private javax.swing.JButton massesSelectButton;
    private javax.swing.JTextField massesTextField;
    // End of variables declaration//GEN-END:variables
}
