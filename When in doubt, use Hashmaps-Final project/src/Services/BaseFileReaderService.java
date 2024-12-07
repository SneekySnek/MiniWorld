package Services;

import Interfaces.IFileReader;
import Interfaces.IWorldService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A base abstract class for reading files and folders with specific file extensions.
 */
public abstract class BaseFileReaderService implements IFileReader {
    protected String[] Lines;
    protected final IWorldService world;

    /**
     * Initializes a new instance of the BaseFileReaderService class.
     *
     * @param worldService The world service used for reading files.
     */
    public BaseFileReaderService(IWorldService worldService) {
        this.world = worldService;
    }

    /**
     * Reads a text file from the specified path.
     *
     * @param path The path to the text file.
     * @throws IOException If an I/O error occurs or if the file does not exist or is not a text file.
     */
    public void ReadFile(String path) throws IOException {
        File file = new File(path);

        if (file == null || !(file.isFile()) || !(file.getName().endsWith(".txt")))
            throw new IOException("File does not exist or is not a text file.");
        Lines = ReadFileContents(path);
    }

    /**
     * Reads all text files from the specified folder.
     *
     * @param path The path to the folder.
     * @throws IOException If an I/O error occurs or if the folder does not exist or is not a directory.
     */
    public void ReadFolder(String path) throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files == null)
            throw new IOException("Folder does not exist or is not a directory.");

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                Lines = ReadFileContents(path);
            }
        }
    }

    /**
     * Reads the contents of a text file and returns them as an array of strings.
     *
     * @param path The path to the text file.
     * @return An array of strings representing the lines of the text file.
     */
    private static String[] ReadFileContents(String path) {
        try {
            return Files.readAllLines(Paths.get(path)).toArray(new String[0]);
        } catch (IOException e) {
            return new String[0]; // Return an empty array in case of an exception
        }
    }
}
