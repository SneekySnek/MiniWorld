package Tests;

import Classes.Grass;
import Classes.Rabbit;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The WorldServiceTest class contains unit tests for the WorldService class in a simulated world.
 * It tests various functionalities like creating worlds, adding entities, movement, and interaction with the environment.
 */
class WorldServiceTest {
    private WorldService world;

    @BeforeEach
    void setUp() {
        world = new WorldService();
    }

    @AfterEach
    void afterEach() {
        System.out.println("Testing ended");
        System.out.println();
    }

    /**
     * Tests creating a world of a specific size.
     * Verifies that the world is created with the specified size.
     */
    @Test
    void createWorldTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.SimulateSilent(30);
        assertEquals(10, world.getSize());
    }

    /**
     * Tests the simulation of the world for a specific number of ticks.
     * Verifies that the world advances by the specified number of ticks.
     */
    @Test
    void simulateWorldTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.SimulateSilent(30);
        assertEquals(30, world.getTicks());
    }

    /**
     * Tests adding an entity to the world.
     * Verifies that the entity is successfully added.
     */
    @Test
    void addEntityTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.SimulateSilent(3);
        assertEquals(1, world.getEntities(Grass.class).size());
    }

    /**
     * Tests adding an entity to a specific location in the world.
     * Verifies that the entity is added at the specified location.
     */
    @Test
    void addEntityAtLocationTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 2));
        world.SimulateSilent(3);
        assertEquals(1, world.getEntities(Grass.class).size());
        assertEquals(2, g1.getCurrentLocation().getX());
        assertEquals(2, g1.getCurrentLocation().getY());
    }

    /**
     * Tests adding an entity to the surroundings of a specific location.
     * Verifies that the entity is added to a location near the specified location.
     */
    @Test
    void addEntityToSurroundingTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        Grass g2 = new Grass(world);
        world.Add(g1, new Location(2, 2));
        world.AddToSurrounding(g2, new Location(2, 2), 2);
        world.SimulateSilent(3);
        assertEquals(2, world.getEntities(Grass.class).size());
        assertTrue(g2.getCurrentLocation().getX() != 2);
        assertTrue(g2.getCurrentLocation().getY() != 2);
    }

    /**
     * Tests checking if a specific location is occupied.
     * Verifies that the method correctly identifies occupied locations.
     */
    @Test
    void isOccupiedTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 2));
        world.SimulateSilent(3);
        assertTrue(world.IsOccupied(new Location(2, 2)));
        assertFalse(world.IsBlocked(new Location(2, 2)));
    }

    /**
     * Tests checking if a specific location is blocked.
     * Verifies that the method correctly identifies blocked locations.
     */
    @Test
    void isBlockedTest(){
        world.Create(3);
        Rabbit g1 = new Rabbit(world, false, true);
        world.Add(g1, new Location(2, 2));
        world.SimulateSilent(3);
        assertTrue(world.IsOccupied(new Location(2, 2)));
        assertTrue(world.IsBlocked(new Location(2, 2)));
    }

    /**
     * Tests checking if the surroundings of a location are occupied.
     * Verifies that the method correctly identifies if surrounding locations are occupied.
     */
    @Test
    void isSurroundingOccupiedTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 1));
        world.SimulateSilent(3);
        assertTrue(world.IsSurroundingOccupied(new Location(2, 2)));
    }

    /**
     * Tests finding the nearest entity of a certain type to a location.
     * Verifies that the method correctly identifies the nearest entity.
     */
    @Test
    void findNearestTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 4));
        Grass g2 = new Grass(world);
        world.Add(g2, new Location(2, 6));
        Grass g3 = new Grass(world);
        world.Add(g3, new Location(2, 7));
        world.SimulateSilent(3);
        assertEquals(g2, world.findNearest(g1.getCurrentLocation(), 4, Grass.class));
    }

    /**
     * Tests finding the nearest entity from a list of entities to a location.
     * Verifies that the method correctly identifies the nearest entity from the list.
     */
    @Test
    void findNearestFromListTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 4));
        Grass g2 = new Grass(world);
        world.Add(g2, new Location(2, 6));
        Grass g3 = new Grass(world);
        world.Add(g3, new Location(2, 7));
        ArrayList<Grass> grass = new ArrayList<>();
        grass.add(g2);
        grass.add(g3);
        world.SimulateSilent(3);
        assertEquals(g2, world.findNearest(g1.getCurrentLocation(), grass));
    }

    /**
     * Tests removing an entity from the world.
     * Verifies that the entity is successfully removed.
     */
    @Test
    void removeTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.Remove(g1);
        world.SimulateSilent(3);
        assertEquals(0, world.getEntities(Grass.class).size());
    }

    /**
     * Tests hiding an entity in the world.
     * Verifies that the entity is successfully hidden.
     */
    @Test
    void hideTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.Hide(g1);
        world.Add(g1);
        world.SimulateSilent(3);
        assertEquals(1, world.getEntities(Grass.class).size());
    }

    /**
     * Tests retrieving entities of a certain type from the world.
     * Verifies that the correct entities are retrieved.
     */
    @Test
    void getEntitiesTest(){
        world.Create(3);
        for(int i = 0; i < 5; i++)
            world.Add(new Grass(world));
        world.SimulateSilent(3);
        assertEquals(5, world.getEntities(Grass.class).size());
    }

    /**
     * Tests retrieving entities of a certain type at a specific location.
     * Verifies that the correct entities at the location are retrieved.
     */
    @Test
    void getEntitiesAtLocationTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 2));
        world.SimulateSilent(3);
        assertEquals(1, world.getEntities(new Location(2, 2), Grass.class).size());
        assertEquals(2, world.getEntities(new Location(2, 2), Grass.class).get(0).getCurrentLocation().getX());
        assertEquals(2, world.getEntities(new Location(2, 2), Grass.class).get(0).getCurrentLocation().getY());
    }

    /**
     * Tests retrieving entities of a certain type surrounding a location.
     * Verifies that the correct surrounding entities are retrieved.
     */
    @Test
    void getSurroundingEntitiesTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(1, 1));
        world.SimulateSilent(3);
        assertEquals(1, world.getSurroundingEntities(new Location(2, 2), 1, Grass.class).size());
        assertEquals(1, world.getSurroundingEntities(new Location(2, 2), 1, Grass.class).get(0).getCurrentLocation().getX());
        assertEquals(1, world.getSurroundingEntities(new Location(2, 2), 1, Grass.class).get(0).getCurrentLocation().getY());
    }

    /**
     * Tests moving an entity to a specific location.
     * Verifies that the entity is moved to the specified location.
     */
    @Test
    void moveTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 2));
        world.Move(g1, new Location(1, 1));
        world.SimulateSilent(3);
        assertEquals(1, world.getEntities(Grass.class).size());
        assertEquals(1, g1.getCurrentLocation().getX());
        assertEquals(1, g1.getCurrentLocation().getY());
    }

    /**
     * Tests moving an entity towards another entity.
     * Verifies that the entity moves closer to the target entity.
     */
    @Test
    void moveTowardsTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 4));
        Grass g2 = new Grass(world);
        world.Add(g2, new Location(2, 6));
        world.MoveTowards(g1, g2);
        world.SimulateSilent(3);
        assertEquals(2, g1.getCurrentLocation().getX());
        assertEquals(5, g1.getCurrentLocation().getY());
    }

    /**
     * Tests moving an entity away from another entity.
     * Verifies that the entity moves away from the target entity.
     */
    @Test
    void moveAwayFromTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 4));
        Grass g2 = new Grass(world);
        world.Add(g2, new Location(2, 6));
        world.MoveAwayFrom(g1, g2);
        world.SimulateSilent(3);
        assertEquals(2, g1.getCurrentLocation().getX());
        assertEquals(3, g1.getCurrentLocation().getY());
    }

    /**
     * Tests moving an entity to a nearby unoccupied location.
     * Verifies that the entity is moved to a nearby unoccupied location.
     */
    @Test
    void moveToNearbyTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 2));
        world.MoveToNearby(g1);
        world.SimulateSilent(3);
        assertTrue(g1.getCurrentLocation().getX() != 2);
        assertTrue(g1.getCurrentLocation().getY() != 2);
    }
}