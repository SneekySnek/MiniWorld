package Classes;

import Classes.Abstracts.Entity;
import Exceptions.NoEntityException;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.NonBlocking;

import java.awt.*;
import java.util.*;

/**
 * The Burrow class represents a burrow entity in a simulated world.
 * It extends the Entity class and implements the NonBlocking interface.
 * This class manages the interaction of rabbits with the burrow, including entering and exiting.
 */
public class Burrow extends Entity implements NonBlocking {
    private ArrayList<Rabbit> rabbits = new ArrayList<>();

    /**
     * Constructs a new Burrow instance.
     *
     * @param w The world service interface for the burrow.
     */
    public Burrow(IWorldService w) {
        super(w);
        this.di = new DisplayInformation(Color.orange, "hole-small");
        logs.logEvent("Burrow has been created");
    }

    /**
     * Allows a rabbit to enter the burrow if there is space available.
     * A maximum of 8 rabbits can occupy the burrow at a time.
     *
     * @param rabbit The rabbit attempting to enter the burrow.
     * @throws NoEntityException if the burrow is full.
     */
    public void Enter(Rabbit rabbit) {
        if (this.rabbits.size() > 8) throw new NoEntityException("Burrow full");
        this.rabbits.add(rabbit);
        rabbit.setBurrow(this);
        worldService.Hide(rabbit);
        logs.logEvent("Rabbit has entered");
    }

    /**
     * Allows a rabbit to exit the burrow.
     *
     * @param rabbit The rabbit that is exiting the burrow.
     */
    public void Exit(Rabbit rabbit) {
        rabbit.setBurrow(null);
        this.rabbits.remove(rabbit);
        worldService.AddToSurrounding(rabbit, currentLocation, 3);
        logs.logEvent("Rabbit has exited");
    }

    /**
     * Returns a list of rabbits currently in the burrow.
     *
     * @return An ArrayList of Rabbit objects in the burrow.
     */
    public ArrayList<Rabbit> getRabbits() {
        return rabbits;
    }
}
