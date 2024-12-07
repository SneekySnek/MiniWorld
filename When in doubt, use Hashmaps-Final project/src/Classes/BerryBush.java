package Classes;

import Classes.Abstracts.Plant;
import Enums.EntityStatus;
import Interfaces.IFlora;
import Interfaces.INewDayListener;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.Random;

/**
 * The BerryBush class represents a berry bush in a simulated world.
 * It extends the Plant class and includes functionalities specific to a berry bush,
 * such as growing berries and changing its appearance based on the number of berries.
 */
public class BerryBush extends Plant {
    private int berries = 0;
    private int berriesThreshold = 3;

    /**
     * Constructs a new BerryBush instance.
     *
     * @param world The world service interface for the berry bush.
     */
    public BerryBush(IWorldService world) {
        super(world);
        this.di = new DisplayInformation(Color.lightGray, "bush", true);
    }

    /**
     * Handles the growth of berries on a new day.
     * Berries grow if the bush is alive, older than 1 day, and has less than 10 berries.
     *
     * @param DayNumber The current day number in the world.
     */
    @Override
    public void onNewDay(int DayNumber) {
        if(status == EntityStatus.ALIVE && DayNumber > 1 && this.berries < 10)
            berries++;
    }

    /**
     * Consumes the berries on the bush and resets the berry count.
     * The display information is also updated to reflect the absence of berries.
     */
    @Override
    public void consume() {
        this.di = new DisplayInformation(Color.lightGray, "bush", true);
        this.berries = 0;
    }

    /**
     * Updates the bush's appearance based on the number of berries.
     * If the bush has more than the threshold of berries, it shows berries in its display.
     *
     * @param world The world in which the berry bush is acting.
     */
    public void act(World world) {
        if(berries < berriesThreshold)
            di = new DisplayInformation(Color.lightGray, "bush", true);
        else if(berries == berriesThreshold && !this.di.getImageKey().equals("bush-berries"))
            di = new DisplayInformation(Color.lightGray, "bush-berries", true);
    }

    /**
     * Checks if the bush has more than the threshold of berries.
     *
     * @return True if the bush has more than the threshold of berries, false otherwise.
     */
    public boolean hasBerries() { return berries > berriesThreshold; }

    /**
     * Adds a specified amount of berries to the bush.
     * This method is intended for testing purposes.
     *
     * @param amount The number of berries to add to the bush.
     */
    public void addBerries(int amount) { this.berries += amount; }
}
