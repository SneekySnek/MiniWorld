package Classes;

import Classes.Abstracts.Animal;
import Enums.Age;
import Enums.EntityStatus;
import Enums.Gender;
import Exceptions.LocationBlockedException;
import Exceptions.NoEntityException;
import Interfaces.IFauna;
import Interfaces.IPredator;
import Interfaces.IPrey;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * Constructs a new Rabbit instance.
 *
 * @param w The world service interface for the rabbit.
 */
public class Rabbit extends Animal implements IPrey {
    private Burrow currentBurrow;
    public Rabbit(IWorldService w) {
        super(w);
        this.DisplayInformationKey = "rabbit";
    }

    /**
     * Constructs a new Rabbit instance with specified child and dead statuses.
     *
     * @param w        The world service interface for the rabbit.
     * @param isChild  Indicates if the rabbit is a child.
     * @param isDead   Indicates if the rabbit is dead.
     */
    public Rabbit(IWorldService w, Boolean isChild, Boolean isDead) {
        super(w, isChild, isDead);
        this.DisplayInformationKey = "rabbit";
    }

    /**
     * Defines the behavior of the rabbit in the world.
     *
     * @param world The world in which the rabbit acts.
     */
    @Override
    public void act(World world) {
        if (status != EntityStatus.ALIVE) return;
        if (world.isDay()) { // Run if predators nearby
                List<IPredator> Predators = worldService.getSurroundingEntities(currentLocation, 3, IPredator.class);
                if (!Predators.isEmpty()) {
                    runFromPredators();
                    return;
                }
            move();
        }
        else sleep();
    }

    /**
     * Handles the rabbit's eating behavior.
     */
    @Override
    protected void eat()
    {
        List<Grass> grass = worldService.getSurroundingEntities(currentLocation, 1, Grass.class);
        if(grass.isEmpty()) return;
        grass.get(0).consume();
        addEnergy(30);
        try { Breed(); }
        catch(Exception e) { /* Failed to breed */ }
    }

    /**
     * Actions performed by the rabbit at the start of a new day.
     *
     * @param DayNumber The current day number in the world.
     */
    @Override
    public void onNewDay(int DayNumber)
    {
        super.onNewDay(DayNumber);
        if(!Objects.isNull(currentBurrow))
            currentBurrow.Exit(this);
    }

    /**
     * Manages the rabbit's sleeping behavior.
     */
    // To sleep search nearby burrows, if there are none then create one if there is enough energy otherwise just sleep
    @Override
    public void sleep() {
        try {
            enterBurrow();
        }
        catch(NoEntityException e) {
            currentBurrow = null;
            di = ageStatus == Age.CHILD ? new DisplayInformation(Color.lightGray, DisplayInformationKey + "-small-sleeping") : new DisplayInformation(Color.lightGray, DisplayInformationKey + "-sleeping");
        }
    }

    /**
     * Attempts to enter a burrow for the rabbit.
     *
     * @throws NoEntityException if no burrow is available.
     */
    private void enterBurrow() throws NoEntityException
    {
        if(currentBurrow != null) return;
        try {
            List<Burrow> burrows = worldService.getSurroundingEntities(currentLocation, 1, Burrow.class);
            if (burrows.isEmpty()) throw new NoEntityException("No burrow available");
            burrows.get(0).Enter(this);
        }
        catch(NoEntityException e) { // Try to create a burrow or find the nearest one within a radius. If there is none then sleep in open
            if (getEnergy() > 75 || worldService.IsSurroundingOccupied(currentLocation)) {
                currentBurrow = new Burrow(worldService);
                worldService.AddToSurrounding(currentBurrow, currentLocation, 1);
                currentBurrow.Enter(this);
            } else {
                List<Burrow> Burrows = worldService.getSurroundingEntities(currentLocation, 4, Burrow.class);
                try {
                    Burrow nearestBurrow = worldService.findNearest(currentLocation, Burrows);
                    worldService.MoveTowards(this, nearestBurrow);
                    addEnergy(-energyDrain);
                    List<Burrow> burrows = worldService.getSurroundingEntities(currentLocation, 1, Burrow.class);
                    if (burrows.isEmpty()) return;
                    burrows.get(0).Enter(this);
                } catch (NoEntityException | LocationBlockedException ee) {
                    throw new NoEntityException("No burrow available");
                }
            }
        }
    }

    /**
     * Handles the rabbit's behavior when running from predators.
     */
    public void runFromPredators() {
        try {
            IPredator nearestPredator = worldService.findNearest(currentLocation, 3, IPredator.class);
            worldService.MoveAwayFrom(this, nearestPredator);
        }
        catch(LocationBlockedException e){ /* give up*/ }
    }

    /**
     * Sets the current burrow for the rabbit.
     *
     * @param burrow The burrow to set as the current burrow.
     */
    public void setBurrow(Burrow burrow) { this.currentBurrow = burrow; }
}
