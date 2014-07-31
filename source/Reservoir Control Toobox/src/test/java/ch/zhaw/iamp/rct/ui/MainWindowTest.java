/*
 * Copyright (c) 2014, Zurich University of Applied Sciences, ZHAW
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.zhaw.iamp.rct.ui;

import ch.zhaw.iamp.rct.AppTest;
import ch.zhaw.iamp.rct.Controller;
import java.io.File;
import java.io.IOException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class MainWindowTest {

    private final static String CONFIG_DIRECTORY = "./";
    private final static String TEST_FILE = "testfile.txt";
    private static final File testFile = new File(TEST_FILE);
    private MainWindow mainWindow;
    private Controller controller;

    @Before
    public void setUp() throws IOException {
        AppTest.setAppConfigDirectory(CONFIG_DIRECTORY);

        mainWindow = new MainWindow();
        controller = new Controller();

        testFile.createNewFile();
    }

    @After
    public void cleanUp() throws IOException {
        testFile.delete();
        new File(mainWindow.STORAGE_FILENAME).delete();
        
        AppTest.deleteTestConfigDirectory();
    }

    @Test
    public void testConstructorOnCreatingStorageWithFile() {
        File storageFile = new File(mainWindow.STORAGE_FILENAME);
        assertTrue(storageFile.exists());
        assertNotNull(mainWindow.textFieldValues);
        assertNotNull(mainWindow.textFieldStorage);
    }

    @Test
    public void testSetController() {
        assertNull(mainWindow.controller);
        mainWindow.setController(controller);
        assertSame(controller, mainWindow.controller);
    }

    @Test
    public void testIsInputFileAccessableOnNull() {
        assertFalse(MainWindow.isInputFileAccessable(null));
    }

    @Test
    public void testIsInputFileAccessableOnEmptyString() {
        assertFalse(MainWindow.isInputFileAccessable(""));
    }

    @Test
    public void testIsInputFileAccessableOnNotExistingFile() {
        File file = new File("nonexistingfile.txt");
        assertFalse(MainWindow.isInputFileAccessable(file.getAbsolutePath()));
    }

    @Test
    public void testIsInputFileAccessableOnExistingFile() {
        assertTrue(MainWindow.isInputFileAccessable(testFile.getAbsolutePath()));
    }

    @Test
    public void testIsOutputFileAccessableOnNull() {
        assertFalse(MainWindow.isOutputFileAccessable(null));
    }

    @Test
    public void testIsOutputFileAccessableOnEmptyString() {
        assertFalse(MainWindow.isOutputFileAccessable(""));
    }

    @Test
    public void testIsIOutputFileAccessableOnNotExistingFile() {
        File file = new File("nonexistingfile.txt");
        assertTrue(MainWindow.isOutputFileAccessable(file.getAbsolutePath()));
    }

    @Test
    public void testIsOutputFileAccessableOnExistingFile() {
        assertTrue(MainWindow.isOutputFileAccessable(testFile.getAbsolutePath()));
    }

}
