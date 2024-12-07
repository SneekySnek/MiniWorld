package Interfaces;

import Classes.Events.NewDayEvent;
import Classes.Events.NewNightEvent;
import Exceptions.LocationBlockedException;
import Exceptions.NoEntityException;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.List;

/**
 * Represents an interface for managing the simulated world and its entities.
 */
public interface IWorldService {

    /**
     * Creates the world with the specified size.
     *
     * @param size The size of the world.
     */
    void Create(int size);

    /**
     * Simulates the world for the specified amount of time.
     *
     * @param amount The amount of simulation time to run.
     */
    void Simulate(int amount);

    /**
     * Simulates the world silently (without output) for the specified amount of time.
     *
     * @param amount The amount of simulation time to run silently.
     */
    void SimulateSilent(int amount);

    /**
     * Adds an entity to the world.
     *
     * @param entity The entity to add.
     */
    void Add(IEntity entity);

    /**
     * Adds an entity to the world at a specific location.
     *
     * @param entity   The entity to add.
     * @param location The location where the entity should be added.
     */
    void Add(IEntity entity, Location location);

    /**
     * Adds an entity to the surrounding area of a location within a specified radius.
     *
     * @param entity   The entity to add to the surrounding area.
     * @param location The central location.
     * @param radius   The radius around the location to add the entity.
     */
    void AddToSurrounding(IEntity entity, Location location, int radius);

    /**
     * Moves an entity to a nearby tile and returns the new location.
     *
     * @param entity The entity to move.
     * @return The new location of the entity.
     * @throws LocationBlockedException if the target location is blocked.
     */
    Location MoveToNearby(IEntity entity) throws LocationBlockedException;

    /**
     * Moves an entity to a specific location.
     *
     * @param entity   The entity to move.
     * @param location The target location.
     * @throws LocationBlockedException if the target location is blocked.
     */
    void Move(IEntity entity, Location location) throws LocationBlockedException;

    /**
     * Moves an entity towards another entity.
     *
     * @param sourceEntity      The source entity to move.
     * @param destinationEntity The destination entity to move towards.
     * @throws LocationBlockedException if the target location is blocked.
     */
    void MoveTowards(IEntity sourceEntity, IEntity destinationEntity) throws LocationBlockedException;

    /**
     * Moves an entity away from another entity.
     *
     * @param sourceEntity The source entity to move.
     * @param targetEntity The target entity to move away from.
     * @throws LocationBlockedException if the target location is blocked.
     */
    void MoveAwayFrom(IEntity sourceEntity, IEntity targetEntity) throws LocationBlockedException;

    /**
     * Removes an entity from the world.
     *
     * @param entity The entity to remove.
     */
    void Remove(IEntity entity);

    /**
     * Hides an entity, making it invisible in the world.
     *
     * @param entity The entity to hide.
     */
    void Hide(IEntity entity);

    /**
     * Gets a list of entities of the specified type.
     *
     * @param type The class type of entities to retrieve.
     * @param <T>  The type of entities.
     * @return A list of entities of the specified type.
     * @throws NoEntityException if no entities of the specified type are found.
     */
    <T extends IEntity> List<T> getEntities(Class<T> type) throws NoEntityException;

    /**
     * Gets a list of entities of the specified type at a specific location.
     *
     * @param location The location to check for entities.
     * @param type     The class type of entities to retrieve.
     * @param <T>      The type of entities.
     * @return A list of entities of the specified type at the given location.
     * @throws NoEntityException if no entities of the specified type are found at the location.
     */
    <T extends IEntity> List<T> getEntities(Location location, Class<T> type) throws NoEntityException;

    /**
     * Gets a list of surrounding entities of the specified type within a radius of a location.
     *
     * @param location The central location.
     * @param radius   The radius around the location to search for entities.
     * @param type     The class type of entities to retrieve.
     * @param <T>      The type of entities.
     * @return A list of surrounding entities of the specified type.
     * @throws NoEntityException if no entities of the specified type are found in the surrounding area.
     */
    <T extends IEntity> List<T> getSurroundingEntities(Location location, int radius, Class<T> type) throws NoEntityException;

    /**
     * Finds the nearest entity of the specified type within a radius of a location.
     *
     * @param location The central location.
     * @param radius   The radius around the location to search for entities.
     * @param type     The class type of the entity to find.
     * @param <T>      The type of the entity.
     * @return The nearest entity of the specified type.
     * @throws NoEntityException if no entities of the specified type are found in the specified radius.
     */
    <T extends IEntity> T findNearest(Location location, int radius, Class<T> type) throws NoEntityException;

    /**
     * Finds the nearest entity of the specified type among a list of entities.
     *
     * @param location  The central location.
     * @param entities  The list of entities to search within.
     * @param <T>       The type of the entity.
     * @return The nearest entity of the specified type.
     * @throws NoEntityException if no entities of the specified type are found among the provided entities.
     */
    <T extends IEntity> T findNearest(Location location, List<T> entities) throws NoEntityException;

    /**
     * Checks if a location is occupied by an entity.
     *
     * @param tile The location to check for occupancy.
     * @return true if the location is occupied, false otherwise.
     */
    boolean IsOccupied(Location tile);

    /**
     * Checks if a location is blocked.
     *
     * @param tile The location to check for blocking.
     * @return true if the location is blocked, false otherwise.
     */
    boolean IsBlocked(Location tile);

    /**
     * Checks if the surrounding area of a location is occupied by entities.
     *
     * @param tile The central location.
     * @return true if the surrounding area is occupied, false otherwise.
     */
    boolean IsSurroundingOccupied(Location tile);

    /**
     * Gets the event for a new day in the simulation.
     *
     * @return The event for a new day.
     */
    NewDayEvent getNewDayEvent();

    /**
     * Gets the event for a new night in the simulation.
     *
     * @return The event for a new night.
     */
    NewNightEvent getNewNightEvent();

    /**
     * Gets the current day in the simulation.
     *
     * @return The current day.
     */
    int getDay();

    /**
     * Gets the current hour in the simulation.
     *
     * @return The current hour.
     */
    int getHour();

    /**
     * Gets the current tick count in the simulation.
     *
     * @return The current tick count.
     */
    int getTicks();

    /**
     * Gets the size of the world.
     *
     * @return The size of the world.
     */
    int getSize();
}
