package Classes;

import Classes.Abstracts.Animal;
import Enums.Age;
import Enums.EntityStatus;
import Exceptions.LocationBlockedException;
import Interfaces.IPoisonous;
import Interfaces.IPredator;
import Interfaces.IPrey;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.World;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * The Salamander class represents a salamander in a simulated world.
 * It extends the Animal class and implements the IPoisonous and IPrey interfaces,
 * indicating its role as both a poisonous creature and a prey in the ecosystem.
 */
public class Salamander extends Animal implements IPoisonous, IPrey {
    /**
     * Constructs a new Salamander instance.
     *
     * @param w The world service interface for the salamander.
     */
    public Salamander(IWorldService w) {
        super(w);
        this.DisplayInformationKey = "salamander";
        this.energyDrain = 2;
        this.maxAgeThreshold = 30; // Live long, use less energy
        this.adultAgeThreshold = 10;
    }

    /**
     * Constructs a new Salamander instance with specified child and dead statuses.
     *
     * @param w        The world service interface for the salamander.
     * @param isChild  Indicates if the salamander is a child.
     * @param isDead   Indicates if the salamander is dead.
     */
    public Salamander(IWorldService w, Boolean isChild, Boolean isDead) {
        super(w, isChild, isDead);
        this.DisplayInformationKey = "salamander";
        this.energyDrain = 2;
        this.maxAgeThreshold = 30; // Live long, use less energy
        this.adultAgeThreshold = 10;
    }

    /**
     * Defines the behavior of the salamander in the world.
     *
     * @param world The world in which the salamander acts.
     */
    @Override
    public void act(World world) {
        if (status != EntityStatus.ALIVE) return;
        if (world.isDay()) {
            move();
            this.di = new DisplayInformation(Color.lightGray, DisplayInformationKey + (ageStatus == Age.CHILD ? "-small" : ""), true);
        }
        if(new Random().nextInt(5) == 0 && world.isDay()) // 1/5 chance to fire
            fire();
    }

    /**
     * Represents the salamander's ability to create fire.
     * This method updates the salamander's display information to indicate fire.
     */
    public void fire()
    {
        this.di = new DisplayInformation(Color.lightGray, DisplayInformationKey + (ageStatus == Age.CHILD ? "-small" : "") + "-fire", true);
        try {
            worldService.AddToSurrounding(new Fire(worldService), currentLocation, 1);
            logs.logEvent("Salamander added fire");
        } catch(LocationBlockedException e) {/* no place to keep fire */}
    }

    /**
     * Handles the salamander's eating behavior.
     * Salamanders consume worms found in grass and gain energy from them.
     */
    @Override
    protected void eat() {
            List<Grass> Grass = worldService.getSurroundingEntities(currentLocation, 1, Grass.class);
            if(Grass.isEmpty()) return;
            for(Grass grass : Grass)
                if(grass.hasWorms())
                    addEnergy(grass.ConsumeWorms() * 5);
        if(new Random().nextInt(5) == 0) { // 1 in 5 chance to breed
            try {
                Breed();
            } catch (Exception e) { /* Failed to breed */ }
        }
    }
}
