package Interfaces;

import java.io.IOException;

/**
 * This interface represents a file reader that can read a folder.
 */
public interface IFileReader {

    /**
     * Reads all the files in the specified folder.
     *
     * @param path The path of the folder to read.
     * @throws IOException If an I/O error occurs.
     */
    void ReadFolder(String path) throws IOException;
}