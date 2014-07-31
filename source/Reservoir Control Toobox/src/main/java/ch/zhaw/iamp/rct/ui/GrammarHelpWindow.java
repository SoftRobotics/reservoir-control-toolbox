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

import static ch.zhaw.iamp.rct.grammar.GrammarParser.Option.values;

/**
 *
 * @author René Bernhardsgrütter <rene.bernhardsgruetter@posteo.ch>
 */
public class GrammarHelpWindow extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;
    static final String HTML_PREFIX = "<html>";
    static final String HTML_POSTFIX = "</html>";
    private static final String OPTION_VALUE_SEPARATOR = ", ";

    public GrammarHelpWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        assembleOptionValues();
    }

    void assembleOptionValues() {
        StringBuilder options = new StringBuilder(HTML_PREFIX);

        for (int i = 0; i < values().length; i++) {
            options.append(values()[i].toString());

            if (i < values().length - 1) {
                options.append(OPTION_VALUE_SEPARATOR);
            }
        }

        options.append(HTML_POSTFIX);
        optionValuesLabel.setText(options.toString());
    }
    
    @Override
    public void setVisible(boolean isVisible) {
        super.setVisible(isVisible);
        optionValuesLabel.revalidate();
        pack();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        codeLabel = new javax.swing.JLabel();
        availabeOptionsLabel = new javax.swing.JLabel();
        optionValuesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Grammar Help");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setFocusable(false);
        setFocusableWindowState(false);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(300, 0));

        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getStyle() | java.awt.Font.BOLD));
        titleLabel.setText("Example of a Grammar File");

        codeLabel.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        codeLabel.setText("<html># Arm construction<br /> shoulder 0 0.25<br /> elbow 5 0<br /> hand 10 0<br /> <br /> # Network connection to arm<br /> input 1 0<br /> input 4 0<br /> input 6 0<br /> input 9 0<br /> <br /> # Define grammar<br /> productionRules A->Acc B->AC C->ccc<br /> <br /> # Network construction<br /> createMass a [1, 8]<br /> createSpring c [3,10]<br /> expansionRangeX [-3, 5]<br >#randomMasses 30<br />#randomSprings 12<br /> <br /> # Options to influcence behaviour<br /> options excludeSpringCrossings</html>");

        availabeOptionsLabel.setFont(availabeOptionsLabel.getFont().deriveFont(availabeOptionsLabel.getFont().getStyle() | java.awt.Font.BOLD));
        availabeOptionsLabel.setText("Available Options");

        optionValuesLabel.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        optionValuesLabel.setText("Placeholder for options");
        optionValuesLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        optionValuesLabel.setMaximumSize(new java.awt.Dimension(269, 10000));
        optionValuesLabel.setMinimumSize(new java.awt.Dimension(269, 14));
        optionValuesLabel.setPreferredSize(new java.awt.Dimension(269, 42));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(titleLabel)
                    .addComponent(codeLabel)
                    .addComponent(availabeOptionsLabel)
                    .addComponent(optionValuesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(availabeOptionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optionValuesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel availabeOptionsLabel;
    private javax.swing.JLabel codeLabel;
    javax.swing.JLabel optionValuesLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
