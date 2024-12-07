package Services;

import Classes.*;
import Classes.Abstracts.Animal;
import Exceptions.NoEntityException;
import Interfaces.IEntity;
import Interfaces.IFauna;
import Interfaces.IWorldService;
import itumulator.world.Location;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A file reader that uses reflection to create and populate entities in the world.
 */
public class ReflectionFileReader extends BaseFileReaderService {
    public ReflectionFileReader(IWorldService worldService) {
        super(worldService);
    }
    private static final Logger logger = Logger.getLogger(StringBasedFileReader.class.getName());

    /**
     * Reads a file and creates entities in the world based on its contents using reflection.
     *
     * @param path The path to the file to be read.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    @Override
    public void ReadFile(String path) throws IOException {
        super.ReadFile(path);
        world.Create(Integer.parseInt(Lines[0]));
        for (int i = 1; i < Lines.length; i++) {
            if (Lines[i].contains("cordyceps")) return; // Filter out cordyceps as we did not implement it (optional requirement)
            boolean isDead = false;
            int amount = 0;
            Location location = null;
            String className = "";
            String[] parts = Lines[i].split(" ");

            if (Objects.equals(parts[0], "carcass")) isDead = true;
            if (isDead) className = Character.isDigit(parts[1].charAt(0)) ? parts[0] : parts[1];
            else className = parts[0];

            // Add amount
            for (String part : parts)
                if (Character.isDigit(part.charAt(0))) { // Check if the part starts with an integer
                    if (part.contains("-")) {
                        String[] range = part.split("-");
                        int min = Integer.parseInt(range[0]);
                        int max = Integer.parseInt(range[1]);
                        amount = new Random().nextInt(max - min + 1) + min;
                    } else amount = Integer.parseInt(part);
                    break;
                }

            // Add coordinate
            for (String part : parts)
                if (part.startsWith("<"))// Check if the part starts with an integer
                    location = new Location(Integer.parseInt(String.valueOf(part.charAt(1))), Integer.parseInt(String.valueOf(part.charAt(3))));

            if (className.equals("wolf")) {
                Pack pack = new Pack(world);
                for (int ii = 0; ii < amount; ii++) {
                    Wolf wolf = new Wolf(world, false, isDead);
                    world.Add(wolf);
                    pack.addWolf(wolf);
                }
            } else if (className.equals("carcass")) {
                if (location != null) world.Add(new Rabbit(world, true, true), location);
                else world.Add(new Rabbit(world, true, true));
            } else {
                for (int ii = 0; ii < amount; ii++) {
                    try {
                        Class<? extends IEntity> currentClass = (Class<? extends IEntity>) Class.forName("Classes." + Character.toUpperCase(className.charAt(0)) + className.substring(1));
                        Constructor<? extends IEntity> constructor = currentClass.getDeclaredConstructor(IWorldService.class);
                        constructor.setAccessible(true);
                        IEntity entity;
                        if (IFauna.class.isAssignableFrom(currentClass)) {
                            constructor = currentClass.getDeclaredConstructor(IWorldService.class, Boolean.class, Boolean.class);
                            constructor.setAccessible(true);
                            entity = constructor.newInstance(world, false, isDead);
                        } else entity = constructor.newInstance(world);
                        if (location != null) world.Add(entity, location);
                        else world.Add(entity);
                    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                        throw new NoEntityException("Entity does not exist");
                    }
                }
            }
        }
    }
}
