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

import java.io.File;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class StorageTest {

    private final String STOROGE_PATH = "testStorage.obj";
    private final String STOROGE_OBJECT = "my object to store";
    private String restored;
    private File storageFile;
    private Storage storage;

    @Before
    public void setUp() {
        storageFile = new File(STOROGE_PATH);
        storage = new Storage(STOROGE_PATH);
    }

    @After
    public void cleanUp() {
        storageFile.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnNull() {
        storage = new Storage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorOnEmptyString() {
        storage = new Storage("");
    }

    @Test
    public void testConstructorOnAssignment() {
        storage = new Storage(STOROGE_PATH);
        assertEquals(STOROGE_PATH, storage.storagePath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectToStoreOnNull() {
        storage.store(null);
    }

    @Test
    public void testObjectToStore() {
        assertFalse(storageFile.exists());
        storage.store(STOROGE_OBJECT);

        assertTrue(storageFile.exists());
    }

    @Test
    public void testObjectToRetore() {
        storage.store(STOROGE_OBJECT);
        restored = (String) storage.restore();
        assertEquals(STOROGE_OBJECT, restored);
    }

    @Test
    public void testObjectToRetoreWithTwoStorages() {
        storage.store(STOROGE_OBJECT);

        Storage restorer = new Storage(STOROGE_PATH);
        restored = (String) restorer.restore();
        assertEquals(STOROGE_OBJECT, restored);
    }

    @Test(expected = StorageException.class)
    public void testObjectToRetoreOnNonExistingFile() {
        assertFalse(storageFile.exists());
        restored = (String) storage.restore();
    }

    @Test
    public void testIsStorageExisting() {
        Storage readerStorage = new Storage(STOROGE_PATH);

        assertFalse(storageFile.exists());
        assertFalse(storage.isExisting());
        assertFalse(readerStorage.isExisting());

        storage.store(STOROGE_OBJECT);
        assertTrue(storage.isExisting());
        assertTrue(readerStorage.isExisting());
    }

}
