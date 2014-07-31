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
package ch.zhaw.iamp.rct.util;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import static java.util.logging.Level.SEVERE;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

/**
 * Provides simple dialogs via static access.
 */
public class Dialogs {

    private final static Dimension MINIMUM_SIZE = new Dimension(600, 400);
    private final static Dimension PREFERRED_SIZE = new Dimension(700, 450);

    /**
     * Shows a not-too-ugly file select dialog. Since Swing in combination with
     * GTK+ only provides really ugly and/or not correctly function file
     * dialogs, this one enables the Nimbus Look and Feel.
     *
     * @param parent The parent component of this dialog.
     * @param startPath The path to open.
     * @param showAllFiles Whether or not all files should be shown. If false,
     * only {@code csv} files will be shown.
     * @return The selected file.
     */
    public static File selectFileFromDialog(Component parent, String startPath, boolean showAllFiles) {
        if (startPath == null || parent == null) {
            throw new IllegalArgumentException("The arguments may not be null.");
        }

        LookAndFeel originalLookAndFeel = UIManager.getLookAndFeel();
        setLookAndFeelToNimbus();
        File startFile = new File(startPath);
        JFileChooser dialog = new JFileChooser(startFile);

        if (startFile.isFile()) {
            dialog.setSelectedFile(startFile);
        }

        if (!showAllFiles) {
            dialog.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file != null && (file.isDirectory() || file.getAbsolutePath().matches(".*\\.csv"));
                }

                @Override
                public String getDescription() {
                    return "*.csv";
                }
            });
        }

        dialog.setMultiSelectionEnabled(false);
        dialog.setMinimumSize(MINIMUM_SIZE);
        dialog.setPreferredSize(PREFERRED_SIZE);
        dialog.showOpenDialog(parent);
        File file = dialog.getSelectedFile();

        setLookAndFeel(originalLookAndFeel);

        return file;
    }

    private static void setLookAndFeelToNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Dialogs.class.getName()).log(SEVERE, "Could not change the look and feel for file chooser dialog: {0}", ex);
        }
    }

    private static void setLookAndFeel(LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Dialogs.class.getName()).log(SEVERE, "Could not restore the look and feel after file chooser dialog: {0}", ex);
        }
    }
    
    public static void showErrorPane(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
