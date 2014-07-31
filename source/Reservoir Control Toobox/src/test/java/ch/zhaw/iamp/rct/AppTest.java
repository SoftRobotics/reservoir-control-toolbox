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

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author René Bernhardsgrütter <rene.bernhardsgruetter@gmail.com>
 */
public class AppTest {

    private static final String TEST_CONFIG_DIRECTORY = "./test-config/";
    private File configDirectory;
    private App app;

    @Before
    public void setUp() throws IOException {
        deleteTestConfigDirectory();

        configDirectory = new File(TEST_CONFIG_DIRECTORY);
        app = new App();
        app.CONFIG_DIRECTORY = TEST_CONFIG_DIRECTORY;
    }

    @After
    public void cleanUp() throws IOException {
        deleteTestConfigDirectory();
    }

    public static void deleteTestConfigDirectory() throws IOException {
        File directory = new File(TEST_CONFIG_DIRECTORY);
        FileUtils.deleteDirectory(directory);
    }

    @Test
    public void testMainPraparations() {
        app.main(null);
        assertNotNull(app.cliOptions);
        assertNotNull(app.controller);
    }

    @Test
    public void testConfigureCommandLineInterface() {
        app.configureCommandLineInterface();

        assertNotNull(app.cliOptions);
        assertTrue(app.cliOptions.hasOption(app.GRAMMAR_OPTIONS[0]));
        assertTrue(app.cliOptions.hasOption(app.DEFINITION_OPTIONS[0]));
        assertTrue(app.cliOptions.hasOption(app.INIT_OPTIONS[0]));
        assertTrue(app.cliOptions.hasOption(app.OUTPUT_OPTIONS[0]));
    }

    @Test
    public void testCreatingConfigDirectory() {
        assertFalse(configDirectory.exists());

        app.main(null);

        assertTrue(configDirectory.exists());
    }

    /**
     * This method allows to set the config directory path in the static
     * {@link App} class. This should be used for testing purposes only.
     *
     * @param directory The new directory.
     */
    public static void setAppConfigDirectory(String directory) {
        App.CONFIG_DIRECTORY = directory;
    }
}
