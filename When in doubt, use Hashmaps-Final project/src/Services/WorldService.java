package Services;

import Classes.Abstracts.Animal;
import Classes.Events.NewDayEvent;
import Classes.Events.NewNightEvent;
import Exceptions.LocationBlockedException;
import Exceptions.NoEntityException;
import Interfaces.IEntity;
import Interfaces.IPredator;
import Interfaces.IWorldService;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.*;

/**
 * The WorldService class is responsible for managing the simulation of a world.
 * It provides functionality to add, move, and remove entities within the world,
 * as well as to simulate the passage of time and events like day and night.
 */
public class WorldService implements IWorldService {

    private final int display_size = 800;
    private int delay = 500;
    private int Size = 15;
    private int Hour = 0;
    private int Day = 0; // 20 Hours in a day
    private int Night = 0;
    private NewDayEvent newDayEvent = new NewDayEvent();
    private NewNightEvent newNightEvent = new NewNightEvent();
    private int Tick = 0;
    private World world;
    private Program program;
    private Map<Object, Location> entityList;

    /**
     * Initializes the world with the given size.
     *
     * @param size The size of the world to be created.
     */
    public void Create(int size)
    {
        Size = size;
        program = new Program(Size, display_size, delay);
        world = program.getWorld();
    }

    /**
     * Runs the simulation for the specified amount of ticks and prints the total runtime.
     *
     * @param Amount The number of ticks to simulate.
     */
    public void Simulate(int Amount){
        Hour = Tick = Day = 0;
        System.out.println();
        System.out.println("% Simulation start ---------------------------------------------------------------------- %");

        System.out.println();

        System.out.println("Log:");

        long start = System.currentTimeMillis();
        program.show(); // Show the GUI

        for(int i = 0; i < Amount; i++)
            run();

        System.out.println();
        System.out.println("% Simulation stop ----------------------------------------------------------------------- %");
        long stop = System.currentTimeMillis();
        System.out.println();

        System.out.println("Total runtime: " + (double)((int)((((double)stop-(double)start)/1000)*100))/100 + "s");
        System.out.println();
    }

    /**
     * Runs a single tick of the simulation.
     */
    private void run()
    {
        Tick++;
        program.simulate();
        if(Tick % 20 == 0) {
            Day++;
            newDayEvent.triggerEvent(Day);
        }
        Hour = world.getCurrentTime();
        if(Tick % 20 == 10) {
            Night++;
            newNightEvent.triggerEvent(Night);
        }
    }

    /**
     * Simulates the world without displaying the GUI for faster execution.
     *
     * @param Amount The number of ticks to simulate.
     */
    public void SimulateSilent(int Amount){
        System.out.println();
        System.out.println("% Simulation start ---------------------------------------------------------------------- %");

        System.out.println();

        System.out.println("Log:");

        long start = System.currentTimeMillis();

        for(int i = 0; i < Amount; i++)
            run();

        System.out.println();
        System.out.println("% Simulation stop ----------------------------------------------------------------------- %");
        long stop = System.currentTimeMillis();
        System.out.println();

        System.out.println("Total runtime: " + (double)((int)((((double)stop-(double)start)/1000)*100))/100 + "s");
        System.out.println();
    }

    /**
     * Adds an entity to the world in a random unoccupied location.
     * If the randomly chosen location is occupied, the method attempts to add the entity to a surrounding location.
     *
     * @param entity The entity to be added to the world.
     */
    @Override
    public void Add(IEntity entity) {
        Location location = new Location(new Random().nextInt(Size), new Random().nextInt(Size));
        while(!world.isTileEmpty(location))
            location = new Location(new Random().nextInt(Size), new Random().nextInt(Size));
        if(!IsOccupied(location)) {
            entity.setLocation(location);
            world.setTile(location, entity);
        }
        else
            AddToSurrounding(entity, location,1);
    }

    /**
     * Adds an entity to the world at a specified location.
     * If the specified location is occupied, the method attempts to add the entity to a surrounding location within a radius of 2.
     *
     * @param entity   The entity to be added to the world.
     * @param location The location where the entity should be added.
     */
    @Override
    public void Add(IEntity entity, Location location) {
        if(!world.isTileEmpty(location)) {
            this.AddToSurrounding(entity, location, 2);
            return;
        }
        world.setTile(location, entity);
        entity.setLocation(location);
    }

    /**
     * Adds an entity to a surrounding location within a specified radius of a given location.
     * The method searches for the first unoccupied location within the radius and adds the entity there.
     * If no unoccupied location is found, a LocationBlockedException is thrown.
     *
     * @param entity   The entity to be added to the world.
     * @param location The central location from which to search for an unoccupied spot.
     * @param radius   The radius within which to search for an unoccupied location.
     * @throws LocationBlockedException If no unoccupied location is found within the radius.
     */
    @Override
    public void AddToSurrounding(IEntity entity, Location location, int radius)
    {
        world.setCurrentLocation(location);
        Set<Location> m = world.getSurroundingTiles(location, radius);
        if (!m.isEmpty()) {
            for(Location l : m){
                if(!IsOccupied(l)){
                    entity.setLocation(l);
                    world.setTile(l, entity);
                    return;
                }
            }
        }
        throw new LocationBlockedException("Could not add entity");
    }

    /**
     * Moves the given entity to a nearby location.
     *
     * @param entity The entity to be moved.
     * @return The new location of the entity.
     */
    public Location MoveToNearby(IEntity entity)
    {
        world.setCurrentLocation(entity.getCurrentLocation());
        List<Location> locations = new ArrayList<>(world.getEmptySurroundingTiles().stream().toList());
        Collections.shuffle(locations);
        if(!locations.isEmpty())
            for(Location loc : locations) {
                if(!IsBlocked(loc)) {
                    this.Move(entity, loc);
                    return loc;
                }
            }
        throw new LocationBlockedException("Could not move nearby");
    }

    /**
     * Moves the given entity to the specified location.
     *
     * @param entity The entity to be moved.
     * @param location The destination location.
     * @throws LocationBlockedException if the destination is blocked.
     */
    public void Move(IEntity entity, Location location) throws LocationBlockedException
    {
        try {
            if(IsBlocked(location))
                throw new LocationBlockedException("Could not move there");
            world.setCurrentLocation(location);
            world.move(entity, location);
            entity.setLocation(location);
        }
        catch(Exception e) {
            throw new LocationBlockedException("Could not move there");
        }
    }

    /**
     * Moves the source entity towards the destination entity.
     *
     * @param sourceEntity The entity to be moved.
     * @param destinationEntity The entity towards which the source entity is moved.
     * @throws LocationBlockedException if the path is blocked.
     */
    public void MoveTowards(IEntity sourceEntity, IEntity destinationEntity) throws LocationBlockedException
    {
        int dx = destinationEntity.getCurrentLocation().getX() - sourceEntity.getCurrentLocation().getX();
        int dy = destinationEntity.getCurrentLocation().getY() - sourceEntity.getCurrentLocation().getY();
        // Normalize direction
        if (dx != 0) dx /= Math.abs(dx);
        if (dy != 0) dy /= Math.abs(dy);
        // Calculate new location
        Location newLocation = new Location(sourceEntity.getCurrentLocation().getX() + dx, sourceEntity.getCurrentLocation().getY() + dy);
        this.Move(sourceEntity, newLocation);
    }

    /**
     * Moves the source entity away from the target entity.
     *
     * @param sourceEntity The entity to be moved.
     * @param targetEntity The entity from which the source entity is moved away.
     * @throws LocationBlockedException if the path is blocked.
     */
    public void MoveAwayFrom(IEntity sourceEntity, IEntity targetEntity) throws LocationBlockedException
    {
        int dx = sourceEntity.getCurrentLocation().getX() - targetEntity.getCurrentLocation().getX();
        int dy = sourceEntity.getCurrentLocation().getY() - targetEntity.getCurrentLocation().getY();
        // Normalize direction
        if (dx != 0) dx /= Math.abs(dx);
        if (dy != 0) dy /= Math.abs(dy);
        // Calculate new location
        Location newLocation = new Location(sourceEntity.getCurrentLocation().getX() + dx, sourceEntity.getCurrentLocation().getY() + dy);
        this.Move(sourceEntity, newLocation);
    }

    /**
     * Removes an entity from the world.
     * This method will remove the entity from the world's grid and clean up any associated resources.
     *
     * @param entity The entity to be removed from the world.
     */
    @Override
    public void Remove(IEntity entity) {
        world.remove(entity);
        world.delete(entity);
    }

    /**
     * Hides an entity from the world.
     * This method removes the entity from the world's grid but does not clean up its resources.
     *
     * @param entity The entity to be hidden in the world.
     */
    @Override
    public void Hide(IEntity entity) {
        world.delete(entity);
    }

    /**
     * Retrieves a list of entities of a specified type in the world.
     * This method searches the world for all entities of the given class type.
     *
     * @param type The class type of entities to retrieve.
     * @return A list of entities of the specified type.
     */
    @Override
    public <T extends IEntity> List<T> getEntities(Class<T> type) {
        ArrayList<T> list = new ArrayList<T>();
        for (Map.Entry<Object, Location> entry : world.getEntities().entrySet())
                if(type.isInstance(entry.getKey()))
                    list.add((T)entry.getKey());
        return list;
    }

    /**
     * Retrieves a list of entities of a specified type at a specific location.
     * This method searches the world at the given location for all entities of the given class type.
     *
     * @param location The location at which to search for entities.
     * @param type     The class type of entities to retrieve.
     * @return A list of entities of the specified type at the given location.
     */
    @Override
    public <T extends IEntity> List<T> getEntities(Location location, Class<T> type) {
        ArrayList<T> list = new ArrayList<T>();
        for (Map.Entry<Object, Location> entry : world.getEntities().entrySet())
            if (entry.getValue().getX() == location.getX() && entry.getValue().getY() == location.getY())
                if(type.isInstance(entry.getKey()))
                    list.add((T)entry.getKey());
        return list;
    }

    /**
     * Retrieves a list of entities of a specified type within a specified radius of a given location.
     * This method searches the surrounding area of the location for entities of the given class type.
     *
     * @param location The center location for the search.
     * @param radius   The radius within which to search for entities.
     * @param type     The class type of entities to retrieve.
     * @return A list of entities of the specified type within the radius of the location.
     */
    @Override
    public <T extends IEntity> List<T> getSurroundingEntities(Location location, int radius, Class<T> type) {
        world.setCurrentLocation(location);
        Set<Location> m = world.getSurroundingTiles(location, radius);
        List<T> list = new ArrayList<T>();
        for(Location l : m)
            if(IsOccupied(l))
                if(type.isInstance(world.getTile(l)))
                    list.add((T)world.getTile(l));
        return list;
    }

    /**
     * Finds the nearest entity of a specified type within a certain radius of a location.
     * This method returns the nearest entity of the specified type within the specified radius.
     *
     * @param <T>      The type of entity to find.
     * @param location The central location from which to measure the radius.
     * @param radius   The radius within which to find the entity.
     * @param type     The class object of the entity type to find.
     * @return The nearest entity of the specified type within the radius.
     * @throws NoEntityException If no entity of the specified type is found within the radius.
     */
    @Override
    public <T extends IEntity> T findNearest(Location location, int radius, Class<T> type) throws NoEntityException
    {
        return this.findNearest(location, this.getSurroundingEntities(location, radius, type));
    }

    /**
     * Finds the nearest entity from a list of entities relative to a location.
     * This method returns the nearest entity from the list based on its distance from the specified location.
     *
     * @param <T>       The type of entity to find.
     * @param location  The location from which to measure distance to entities.
     * @param entities  The list of entities among which to find the nearest.
     * @return The nearest entity from the list to the specified location.
     * @throws NoEntityException If the list of entities is empty.
     */
    @Override
    public <T extends IEntity> T findNearest(Location location, List<T> entities) throws NoEntityException
    {
        if (entities.isEmpty()) throw new NoEntityException("No entity nearby");
        T nearestEntity = entities.get(0);
        for (T entity : entities)
        {
            int dx = location.getX() - entity.getCurrentLocation().getX();
            int dy = location.getY() - entity.getCurrentLocation().getY();

            int ndx = location.getX() - nearestEntity.getCurrentLocation().getX();
            int ndy = location.getY() - nearestEntity.getCurrentLocation().getY();
            if (Math.sqrt(dx * dx + dy * dy) < Math.sqrt(ndx * ndx + ndy * ndy))
                nearestEntity = entity;
        }
        return nearestEntity;
    }

    /**
     * Checks if the given location is occupied.
     *
     * @param Tile The location to check.
     * @return True if the location is occupied, otherwise false.
     */
    public boolean IsOccupied(Location Tile) { // If it is blocked or contains a non-blocking
        return (IsBlocked(Tile) || (world.getTile(Tile) instanceof NonBlocking));
    }

    /**
     * Checks if the given location is blocked.
     *
     * @param Tile The location to check.
     * @return True if the location is blocked, otherwise false.
     */
    public boolean IsBlocked(Location Tile) {
        return !world.isTileEmpty(Tile);
    }

    /**
     * Checks if the surrounding area of the given location is occupied.
     *
     * @param Tile The location to check.
     * @return True if the surrounding area is occupied, otherwise false.
     */
    public boolean IsSurroundingOccupied(Location Tile) {
        world.setCurrentLocation(Tile);
        Set<Location> m = world.getEmptySurroundingTiles();
        if (!m.isEmpty()) {
            boolean occupied = false;
            for(Location l : m)
                if(IsOccupied(l))   occupied = true;
            return occupied;
        }
        return true;
    }

    /**
     * Validates if the given location is within the bounds of the world.
     *
     * @param Tile The location to validate.
     * @return True if the location is valid, otherwise false.
     */
    private Boolean ValidateLocation(Location Tile)
    {
        return (Tile.getX() > Size || Tile.getY() > Size || Tile.getX() < 0 || Tile.getY() < 0);
    }

    public World getWorld() {
        return world;
    }

    // setters and getters
    public void setDelay(int delayAmount){
        this.delay = delayAmount;
    }
    public NewDayEvent getNewDayEvent() { return this.newDayEvent; }
    public NewNightEvent getNewNightEvent() { return this.newNightEvent; }

    @Override
    public int getDay() {
        return Day;
    }

    @Override
    public int getHour() {
        return Hour;
    }

    @Override
    public int getTicks() {
        return Tick;
    }

    @Override
    public int getSize() { return world.getSize(); }
}