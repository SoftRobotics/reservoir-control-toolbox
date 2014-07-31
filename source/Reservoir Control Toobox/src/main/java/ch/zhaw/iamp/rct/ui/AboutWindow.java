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
import java.awt.event.KeyEvent;

public class AboutWindow extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    public AboutWindow() {
        initComponents();
        appNameLabel.setText(App.APP_NAME);
        appVersionLabel.setText("Version " + App.APP_VERSION);
        requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutLabel = new javax.swing.JLabel();
        auhtorsLabel = new javax.swing.JLabel();
        authorsPanel = new javax.swing.JPanel();
        author1Panel = new javax.swing.JPanel();
        author1Label = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));
        author2Panel = new javax.swing.JPanel();
        author2Label = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));
        supervisorsLabel = new javax.swing.JLabel();
        supervisorsPanel = new javax.swing.JPanel();
        supervisor1Panel = new javax.swing.JPanel();
        supervisor1Label = new javax.swing.JLabel();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));
        supervisor2Panel = new javax.swing.JPanel();
        supervisior2Label = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));
        supervisor3Panel = new javax.swing.JPanel();
        supervisor3Label = new javax.swing.JLabel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));
        supervisor4Panel = new javax.swing.JPanel();
        supervisor4Label = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 15), new java.awt.Dimension(0, 15), new java.awt.Dimension(32767, 15));
        copyrightLabel = new javax.swing.JLabel();
        appVersionLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        appNameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        aboutLabel.setText("<html>This application is part of a Bachelor's Thesis and allows to configure and run the robot arm simulation toolbox.</html>");

        auhtorsLabel.setFont(auhtorsLabel.getFont().deriveFont((auhtorsLabel.getFont().getStyle() | java.awt.Font.ITALIC), auhtorsLabel.getFont().getSize()+2));
        auhtorsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        auhtorsLabel.setText("Authors");

        authorsPanel.setLayout(new java.awt.GridLayout(1, 0));

        author1Panel.setLayout(new java.awt.BorderLayout());

        author1Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        author1Label.setText("<html><center>René Bernhardsgrütter<br />Institute of Applied Mathematics and Physics<br />Zurich University of Applied Sciences<br /><code>rene.bernhardsgruetter@posteo.ch</code></center></html>");
        author1Panel.add(author1Label, java.awt.BorderLayout.CENTER);
        author1Panel.add(filler1, java.awt.BorderLayout.PAGE_END);

        authorsPanel.add(author1Panel);

        author2Panel.setLayout(new java.awt.BorderLayout());

        author2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        author2Label.setText("<html><center>Christoph W. Senn<br />Institute of Applied Mathematics and Physics<br />Zurich University of Applied Sciences<br /><code>chrigi.senn@gmail.com</code></center></html>");
        author2Panel.add(author2Label, java.awt.BorderLayout.CENTER);
        author2Panel.add(filler2, java.awt.BorderLayout.PAGE_END);

        authorsPanel.add(author2Panel);

        supervisorsLabel.setFont(supervisorsLabel.getFont().deriveFont((supervisorsLabel.getFont().getStyle() | java.awt.Font.ITALIC), supervisorsLabel.getFont().getSize()+2));
        supervisorsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supervisorsLabel.setText("Supervisors");

        supervisorsPanel.setLayout(new java.awt.GridLayout(2, 0));

        supervisor1Panel.setLayout(new java.awt.BorderLayout());

        supervisor1Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supervisor1Label.setText("<html><center>Rudolf M. Füchslin<br />Institute of Applied Mathematics and Physics<br />Zurich University of Applied Sciences<br /><code>rudolf.fuechslin@zhaw.ch</code></center></html>");
        supervisor1Panel.add(supervisor1Label, java.awt.BorderLayout.CENTER);
        supervisor1Panel.add(filler3, java.awt.BorderLayout.PAGE_END);

        supervisorsPanel.add(supervisor1Panel);

        supervisor2Panel.setLayout(new java.awt.BorderLayout());

        supervisior2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supervisior2Label.setText("<html><center>Helmut Hauser<br />Artificial Intelligence Laboratory<br />Department of Informatics - University of Zurich<br /><code>hhauser@ifi.uzh.ch</code></center></html>");
        supervisor2Panel.add(supervisior2Label, java.awt.BorderLayout.CENTER);
        supervisor2Panel.add(filler4, java.awt.BorderLayout.PAGE_END);

        supervisorsPanel.add(supervisor2Panel);

        supervisor3Panel.setLayout(new java.awt.BorderLayout());

        supervisor3Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supervisor3Label.setText("<html><center>Christian Jaeger<br />Institute of Applied Mathematics and Physics<br />Zurich University of Applied Sciences<br /><code>christian.jaeger@zhaw.ch</code></center></html>");
        supervisor3Panel.add(supervisor3Label, java.awt.BorderLayout.CENTER);
        supervisor3Panel.add(filler5, java.awt.BorderLayout.PAGE_END);

        supervisorsPanel.add(supervisor3Panel);

        supervisor4Panel.setLayout(new java.awt.BorderLayout());

        supervisor4Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supervisor4Label.setText("<html><center>Naveen Kuppuswamy<br />Artificial Intelligence Laboratory<br />Department of Informatics - University of Zurich<br /><code>naveenoid@ifi.uzh.ch</code></center></html>");
        supervisor4Panel.add(supervisor4Label, java.awt.BorderLayout.CENTER);
        supervisor4Panel.add(filler6, java.awt.BorderLayout.PAGE_END);

        supervisorsPanel.add(supervisor4Panel);

        copyrightLabel.setFont(copyrightLabel.getFont().deriveFont(copyrightLabel.getFont().getSize()-1f));
        copyrightLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        copyrightLabel.setText("<html><center>Copyright © 2014 Zurich University of Applied Sciences<br />All Rights Reserved.</center></html>");

        appVersionLabel.setFont(appVersionLabel.getFont().deriveFont(appVersionLabel.getFont().getSize()-1f));
        appVersionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        appVersionLabel.setText("Version");
        appVersionLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        appVersionLabel.setPreferredSize(new java.awt.Dimension(75, 14));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/zhaw/iamp/rct/ui/aboutWindow-cut.png"))); // NOI18N

        appNameLabel.setFont(appNameLabel.getFont().deriveFont(appNameLabel.getFont().getStyle() | java.awt.Font.BOLD, appNameLabel.getFont().getSize()+4));
        appNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        appNameLabel.setText("App Name");
        appNameLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        appNameLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(copyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(appNameLabel)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(appVersionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(supervisorsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                            .addComponent(supervisorsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(authorsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(auhtorsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(aboutLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(appNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aboutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(auhtorsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supervisorsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supervisorsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(copyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(appVersionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        dispose();
    }//GEN-LAST:event_formMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            dispose();
        }
    }//GEN-LAST:event_formKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aboutLabel;
    private javax.swing.JLabel appNameLabel;
    private javax.swing.JLabel appVersionLabel;
    private javax.swing.JLabel auhtorsLabel;
    private javax.swing.JLabel author1Label;
    private javax.swing.JPanel author1Panel;
    private javax.swing.JLabel author2Label;
    private javax.swing.JPanel author2Panel;
    private javax.swing.JPanel authorsPanel;
    private javax.swing.JLabel copyrightLabel;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel supervisior2Label;
    private javax.swing.JLabel supervisor1Label;
    private javax.swing.JPanel supervisor1Panel;
    private javax.swing.JPanel supervisor2Panel;
    private javax.swing.JLabel supervisor3Label;
    private javax.swing.JPanel supervisor3Panel;
    private javax.swing.JLabel supervisor4Label;
    private javax.swing.JPanel supervisor4Panel;
    private javax.swing.JLabel supervisorsLabel;
    private javax.swing.JPanel supervisorsPanel;
    // End of variables declaration//GEN-END:variables
}
