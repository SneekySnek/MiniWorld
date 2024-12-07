import Classes.*;
import Services.ReflectionFileReader;
import Services.StringBasedFileReader;
import Services.WorldService;
import itumulator.world.Location;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String InputLocation = "data/input-filer/txt files for making video/custom7.txt"; // Type "custom" for the dev file, or the file name
    public static void main(String[] args) {

        WorldService world = new WorldService();
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(InputLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        world.Simulate(300); // Simulate the program and print the total run time
    }
}