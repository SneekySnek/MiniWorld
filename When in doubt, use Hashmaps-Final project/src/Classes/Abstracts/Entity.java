package Classes.Abstracts;

import Classes.Events.EventLogger;
import Classes.Events.NewDayEvent;
import Enums.EntityStatus;
import Interfaces.IEntity;
import Interfaces.IWorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * An abstract class representing an entity in the world.
 */
public abstract class Entity implements IEntity {
    protected int age = 0; // In ticks
    protected IWorldService worldService;
    protected DisplayInformation di;
    protected Location currentLocation;
    protected EventLogger logs = new EventLogger();

    /**
     * Constructor for creating an Entity.
     *
     * @param w The world service associated with this entity.
     */
    public Entity(IWorldService w) {
        this.worldService = w;
    }

    /**
     * Performs an action for the entity in the world.
     *
     * @param world The world in which the entity exists.
     */
    @Override
    public void act(World world) {
        this.age++;
    }

    /**
     * Gets the display information for the entity.
     *
     * @return The display information for the entity.
     */
    @Override
    public DisplayInformation getInformation() {
        return di;
    }

    /**
     * Gets the current location of the entity.
     *
     * @return The current location of the entity.
     */
    @Override
    public Location getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Sets the location of the entity.
     *
     * @param location The new location to set for the entity.
     */
    @Override
    public void setLocation(Location location) {
        currentLocation = location;
        logs.logEvent("Location set to " + location.getX() + ", " + location.getY());
    }

    /**
     * Gets the age of the entity in ticks.
     *
     * @return The age of the entity in ticks.
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the event logger associated with the entity.
     *
     * @return The event logger associated with the entity.
     */
    public EventLogger getLogger() {
        return logs;
    }
}