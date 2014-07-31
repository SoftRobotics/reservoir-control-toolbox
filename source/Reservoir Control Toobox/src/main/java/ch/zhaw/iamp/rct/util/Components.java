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

import java.awt.Color;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Window;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * This class provides statically accessible methods for manipulating AWT/Swing
 * components.
 */
public class Components {

    public final static Color DEFAULT_BACKGROUND_COLOR = new TextField().getBackground();
    public final static Color ERROR_BACKGROUND_COLOR = new Color(255, 179, 179);
    final static String[] ICON_SIZES = new String[]{"16x16", "24x24", "32x32", "48x48", "64x64", "128x128", "140x140"};

    /**
     * Moves the cursor/caret to the end of the given text field.
     *
     * @param textField The text field to manipulate.
     */
    public static void moveCurserToEndOfTextField(JTextField textField) {
        if (textField == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        textField.setCaretPosition(textField.getText().length());
    }

    /**
     * Sets the icons to the given {@link JFrame}.
     *
     * @param frame The frame on which the icons should be set.
     */
    public static void setIcons(Window frame) {
        List<Image> icons = new LinkedList<>();

        for (String size : ICON_SIZES) {
            try {
                Image img = ImageIO.read(Components.class.getResourceAsStream("/ch/zhaw/iamp/rct/ui/" + size + ".png"));
                icons.add(img);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load the application icons: " + ex.getMessage());
            }
        }

        frame.setIconImages(icons);
    }
}
