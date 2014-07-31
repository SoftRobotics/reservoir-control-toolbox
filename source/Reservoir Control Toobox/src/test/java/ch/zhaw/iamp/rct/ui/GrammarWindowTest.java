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

import ch.zhaw.iamp.rct.AppTest;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

public class GrammarWindowTest {

    private GrammarWindow window;

    @Before
    public void setUp() {
        window = new GrammarWindow();
    }

    @After
    public void cleanUp() throws IOException {
        AppTest.deleteTestConfigDirectory();
    }

    @Test
    public void testSetGraphCoordinatesLabel() {
        double xPosition = 12;
        double yPositon = 32.321234;

        window.setCoordinatesLabel(xPosition, yPositon);
        assertEquals("12.0/32.32", window.coordinates.getText());
    }

    @Test
    public void testResetGraphCoordinatesLabel() {
        assertEquals(GrammarWindow.DEFAULT_COORDINATES, window.coordinates.getText());
        window.coordinates.setText("123/1234");
        window.resetCoordinatesLabel();
        assertEquals(GrammarWindow.DEFAULT_COORDINATES, window.coordinates.getText());
    }

    @Ignore
    @Test
    public void testGetDirectoryOfInitalisationFileOnEmptyString() {
        assertEquals("", window.getDirectoryOfInitalisationFile());
    }

    @Test
    public void testGetDirectoryOfInitalisationFileOnFile() throws IOException {
        File file = new File("/tmp/grammarwindowtest.txt");
        file.createNewFile();

        window.initialisationFileTextField.setText("/tmp/grammarwindowtest.txt");
        assertEquals("/tmp", window.getDirectoryOfInitalisationFile());

        file.delete();
    }

}
