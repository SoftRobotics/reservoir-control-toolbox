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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This Storage can be used to store {@link Object}s as simple files on the file
 * system and read them again.
 */
public class Storage {

    String storagePath;

    /**
     * Creates a new {@link  Storage} object that reads and writes the file at
     * the given {@code storagePath}.
     *
     * @param storagePath The path of the target file of this {@link Storage}.
     * @throws IllegalArgumentException If the argument is null or empty.
     */
    public Storage(String storagePath) {
        if (storagePath == null || storagePath.isEmpty()) {
            throw new IllegalArgumentException("The argument may not be null nor may it be empty.");
        }

        this.storagePath = storagePath;
    }

    /**
     * Serializes and stores the given argument.
     *
     * @param objectToStore The object to store.
     * @throws IllegalArgumentException If the argument is null.
     * @throws StorageException If the argument could not be serialized or
     * written.
     */
    public void store(Object objectToStore) {
        if (objectToStore == null) {
            throw new IllegalArgumentException("The argument may not be null.");
        }

        try {
            createDirectoriesIfNew();
            serialize(objectToStore);
        } catch (IOException ex) {
            throw new StorageException("Could not serialize and store the given object: " + ex.getMessage());
        }
    }

    private void createDirectoriesIfNew() {
        File directory = new File(storagePath).getAbsoluteFile().getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private void serialize(Object objectToStore) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(storagePath);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(objectToStore);
        }
    }

    /**
     * Restores the object from the storage path.
     *
     * @return The restored object.
     * @throws StorageException If anything goes wrong while restoring.
     */
    public Object restore() {
        try {
            return deserialize();
        } catch (IOException | ClassNotFoundException ex) {
            throw new StorageException("Could not restore the requested object: " + ex.getMessage());
        }
    }

    private Object deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(storagePath);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object restoredObject = objectInputStream.readObject();
        return restoredObject;
    }

    /**
     * Checks if a storage file at the configured storage path can be found or
     * not. This does not test whether the file contains a serialized Java
     * object, nor if it is of the correct type!
     *
     * @return True, if a storage file is existing, otherwise false.
     */
    public boolean isExisting() {
        File storage = new File(storagePath);
        return storage.exists();
    }

}
