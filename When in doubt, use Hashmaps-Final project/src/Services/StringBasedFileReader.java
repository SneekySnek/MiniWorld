package Services;

import Classes.*;
import Interfaces.IWorldService;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;

/**
 * A file reader that reads entity data from a string-based format and creates entities in the world.
 */
public class StringBasedFileReader extends BaseFileReaderService {
    public StringBasedFileReader(IWorldService worldService) {
        super(worldService);
    }
    private static final Logger logger = Logger.getLogger(StringBasedFileReader.class.getName());

    /**
     * Reads a file in a string-based format and creates entities in the world based on its contents.
     *
     * @param path The path to the file to be read.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    @Override
    public void ReadFile(String path) throws IOException {
        super.ReadFile(path);
        world.Create(Integer.parseInt(Lines[0]));
        for (int i = 1; i < Lines.length; i++) {
            String[] parts = Lines[i].split(" ");
            String className = parts[0];
            String amountString = parts[1];
            int amount = 0;

            if (amountString.contains("-")) {
                String[] range = amountString.split("-");
                int min = Integer.parseInt(range[0]);
                int max = Integer.parseInt(range[1]);
                amount = new Random().nextInt(max - min + 1) + min;
            } else
                amount = Integer.parseInt(amountString);

            for (int ii = 0; ii < amount; ii++) {
                switch (className) {
                    case "grass":
                        world.Add(new Grass(world));
                        System.out.println("Grass added " + amount);
                        break;
                    case "rabbit":
                        world.Add(new Rabbit(world));
                        System.out.println("Rabbit added " + amount);
                        break;
                    case "burrow":
                        world.Add(new Burrow(world));
                        System.out.println("Burrow added " + amount);
                        break;
                    case "BerryBush":
                        world.Add(new BerryBush(world));
                        System.out.println("BerryBush added " + amount);
                        break;
                    case "carcass": // Just add a rabbit carcass
                        world.Add(new Rabbit(world, false, true));
                        System.out.println("Carcass added " + amount);
                        break;
                    case "fungi":
                        world.Add(new Fungi(world, false));
                        System.out.println("Fungi added " + amount);
                        break;
                    case "wolf":
                        // world.Add(new Wolf(world));
                        System.out.println("Wolf added " + amount);
                        break;
                    case "bear":
                        world.Add(new Bear(world));
                        System.out.println("Bear added " + amount);
                        break;
                    case "salamander":
                        world.Add(new Salamander(world));
                        System.out.println("Salamander added " + amount);
                        break;
                }
            }
        }
    }
}
