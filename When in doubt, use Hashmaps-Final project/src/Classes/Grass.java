package Classes;

import Classes.Abstracts.Plant;
import Enums.EntityStatus;
import Exceptions.LocationBlockedException;
import Interfaces.IFauna;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * The actor Grass is self-duplicating and will not stop, until the whole map is covered by grass.
 */
public class Grass extends Plant {
    private int worms = 0;
    public Grass(IWorldService world) {
        super(world);
        this.di = new DisplayInformation(Color.green, "grass", true);
    }

    /**
     * This method inherits OnNewDay and will try to grow 2 new grass every new day
     * @param DayNumber The new day
     */
    @Override
    public void onNewDay(int DayNumber)
    {
        if(this.status == EntityStatus.DEAD) return;
        if(worms > 0 || new Random().nextInt(5) == 0)
            worms += 5;
        try {
            if (DayNumber % growthRate == 0) {   // Grow twice every new day
                worldService.AddToSurrounding(new Grass(worldService), currentLocation, 1);
                worldService.AddToSurrounding(new Grass(worldService), currentLocation, 1);
            }
            List<Grass> Grass = worldService.getSurroundingEntities(currentLocation, 1, Grass.class);
            if(!Grass.isEmpty())
                for(Grass grass : Grass)
                    grass.addWorms(1);
        }
        catch(Exception e){ /* New grass can not be added */}
    }
    /**
     * Consumes all the worms in the grass. This method will reset the worm count to zero.
     *
     * @return The number of worms that were in the grass before consumption.
     */
    public int ConsumeWorms()
    {
        int w = worms;
        worms = 0;
        return w;
    }

    /**
     * Checks if the grass has more than one worm.
     *
     * @return True if the grass has more than one worm, false otherwise.
     */
    public Boolean hasWorms() { return worms > 1; };

    /**
     * Gets the current number of worms in the grass.
     *
     * @return The number of worms in the grass.
     */
    public int getWorms() { return worms; }

    /**
     * Adds a specified number of worms to the grass.
     *
     * @param amount The number of worms to add.
     */
    public void addWorms(int amount) { worms += amount; };
}
