package Tests;

import Classes.Bear;
import Classes.BerryBush;
import Classes.Rabbit;
import Classes.Wolf;
import Enums.EntityStatus;
import Enums.Gender;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The BearTest class contains unit tests for various behaviors of bears in a simulated world.
 * It tests the interactions of bears with other entities, their movement, and lifecycle events.
 */
class BearTest {
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
     * Tests if a bear hunts a rabbit within its territory but not outside of it.
     */
    @Test
    void bearTerritoryTest(){ // Test if bear does not hunt rabbit away from territory but does eat the one in territory
        world.Create(20);
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(1, 1));
        Rabbit r1 = new Rabbit(world);
        world.Add(r1, new Location(1, 2));
        Rabbit r2 = new Rabbit(world);
        world.Add(r2, new Location(19, 19));
        world.SimulateSilent(20);
        assertSame(r1.getStatus(), EntityStatus.DEAD);
        assertSame(r2.getStatus(), EntityStatus.ALIVE);
    }

    /**
     * Tests if a bear eats fauna (like a rabbit) when in close proximity.
     */
    @Test
    void eatFaunaTest(){
        world.Create(3);
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(1, 1));
        Rabbit r1 = new Rabbit(world);
        world.Add(r1, new Location(1, 2));
        world.SimulateSilent(20);
        assertSame(r1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests if a bear engages in a fight with another predator (like a wolf).
     */
    @Test
    void fightPredatorTest(){
        world.Create(3);
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(1, 1));
        Wolf w1 = new Wolf(world);
        world.Add(w1, new Location(1, 2));
        world.SimulateSilent(3);
        assertTrue(w1.getHealth() < 50);
    }

    /**
     * Tests if a bear engages in a fight with another bear.
     */
    @Test
    void fightBearTest(){
        world.Create(5);
        Bear b1 = new Bear(world);
        b1.setGender(Gender.MALE);
        world.Add(b1, new Location(1, 1));
        Bear b2 = new Bear(world);
        b2.setGender(Gender.MALE);
        world.Add(b2, new Location(4, 4));
        world.SimulateSilent(10);
        assertTrue(b2.getHealth() < 100);
    }

    /**
     * Tests the breeding process of bears.
     * Verifies if bears breed successfully and produce offspring.
     */
    @Test
    void breedBearTest(){
        world.Create(5);
        Bear b1 = new Bear(world);
        b1.setGender(Gender.MALE);
        b1.addHealth(1000);
        b1.addEnergy(10000);
        world.Add(b1, new Location(1, 1));
        Bear b2 = new Bear(world);
        b2.addHealth(1000);
        b2.addEnergy(10000);
        b2.setGender(Gender.FEMALE);
        world.Add(b2, new Location(2, 3));
        world.SimulateSilent(10);
        assertTrue(world.getEntities(Bear.class).size() > 3);
    }

    /**
     * Tests if a bear consumes berries from a berry bush.
     */
    @Test
    void eatBushesTest(){
        world.Create(3);
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(1, 1));
        BerryBush b2 = new BerryBush(world);
        b2.addBerries(20);
        world.Add(b2, new Location(1, 2));
        world.SimulateSilent(20);
        assertSame(b2.hasBerries(), false);
    }

    /**
     * Tests the movement of a bear towards a target.
     */
    @Test
    void moveTowardsTest(){
        world.Create(5);
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(1, 1));
        world.Add(new Rabbit(world, false, true), new Location(4, 1));
        world.SimulateSilent(2);
        assertSame(b1.getCurrentLocation().getX(), 2);
        assertSame(b1.getCurrentLocation().getY(), 1);
    }
}