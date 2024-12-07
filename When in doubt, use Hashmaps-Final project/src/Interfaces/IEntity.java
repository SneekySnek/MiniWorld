package Interfaces;

import Classes.Events.EventLogger;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * Represents an entity in the simulation.
 */
public interface IEntity extends Actor, DynamicDisplayInformationProvider {

    /**
     * Gets the current location of the entity.
     *
     * @return The current location of the entity.
     */
    Location getCurrentLocation();

    /**
     * Sets the location of the entity.
     *
     * @param location The location to set for the entity.
     */
    void setLocation(Location location);

    /**
     * Gets the age of the entity.
     *
     * @return The age of the entity in ticks.
     */
    int getAge();

    /**
     * Gets the event logger associated with the entity.
     *
     * @return The event logger for the entity.
     */
    EventLogger getLogger();
}
