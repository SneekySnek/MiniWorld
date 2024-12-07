package Classes;

import Classes.Abstracts.Entity;
import Enums.EntityStatus;
import Exceptions.LocationBlockedException;
import Interfaces.IFauna;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Pack class represents a group of wolves that can hunt and act together.
 */
public class Pack extends Entity implements NonBlocking {
    private final List<Wolf> wolves = new ArrayList<>();
    private Boolean isHunting = false;
    private IFauna hunted;

    /**
     * Constructs a new Pack instance.
     *
     * @param w The world service where the Pack belongs.
     */
    public Pack(IWorldService w) {
        super(w);
    }

    /**
     * Performs actions for the Pack during simulation.
     *
     * @param world The world where the simulation is taking place.
     */
    @Override
    public void act(World world) {
        if (isHunting && world.isDay()) { // Move wolves towards IEntity, get amount of wolves next to IEntity and damage it
            for (Wolf wolf : wolves)
                try {
                    worldService.MoveTowards(wolf, hunted);
                } catch (LocationBlockedException e) {
                    /* It's okay; there can be many wolves */
                }
            List<Wolf> nearWolves = worldService.getSurroundingEntities(hunted.getCurrentLocation(), 1, Wolf.class);
            if (!wolves.isEmpty())
                hunted.addHealth(nearWolves.size() * -1 * (new Random().nextInt(11) + 15));
            if (hunted.getStatus() == EntityStatus.CARCASS || hunted.getStatus() == EntityStatus.DEAD) { // It's dead
                for (Wolf wolf : nearWolves)
                    wolf.addEnergy(75);
                isHunting = false;
                hunted = null;
            }
        }
    }

    /**
     * Adds a Wolf to the Pack.
     *
     * @param wolf The Wolf to add to the Pack.
     */
    public void addWolf(Wolf wolf) {
        if (wolves.isEmpty()) // Create a cave at the first wolf location
        {
            worldService.AddToSurrounding(this, wolf.getCurrentLocation(), 2);
            di = new DisplayInformation(Color.orange, "hole");
        }
        wolves.add(wolf);
        wolf.setPack(this);
    }

    /**
     * Removes a Wolf from the Pack.
     *
     * @param wolf The Wolf to remove from the Pack.
     */
    public void removeWolf(Wolf wolf) {
        wolves.remove(wolf);
        wolf.clearPack();
    }

    /**
     * Checks if a Wolf is part of the Pack.
     *
     * @param wolf The Wolf to check.
     * @return true if the Wolf is in the Pack, false otherwise.
     */
    public Boolean isInPack(Wolf wolf) {
        return wolves.contains(wolf);
    }

    /**
     * Checks if the Pack is currently hunting.
     *
     * @return true if the Pack is hunting, false otherwise.
     */
    public Boolean isHunting() {
        return isHunting;
    }

    /**
     * Initiates a hunt by the Pack on a target IFauna entity.
     *
     * @param entity The IFauna entity to hunt.
     */
    public void hunt(IFauna entity) {
        isHunting = true;
        hunted = entity;
    }
}
