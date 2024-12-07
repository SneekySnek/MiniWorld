package Tests;

import Classes.Bear;
import Classes.Burrow;
import Classes.Grass;
import Classes.Rabbit;
import Enums.EntityStatus;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The RabbitTest class contains unit tests for the behaviors of rabbits in a simulated world.
 * It tests rabbit interactions with burrows, their eating habits, and their response to threats.
 */
class RabbitTest {
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
     * Tests a rabbit's ability to enter and sleep in a burrow.
     * Verifies that a rabbit successfully enters a burrow and is found there.
     */
    @Test
    void rabbitBurrowSleepTest(){
        world.Create(3);
        Rabbit r1 = new Rabbit(world);
        world.Add(r1);
        Burrow b1 = new Burrow(world);
        world.Add(b1);
        world.SimulateSilent(19);
        assertEquals(r1, b1.getRabbits().get(0));
    }

    /**
     * Tests a rabbit's ability to exit a burrow.
     * Verifies that the rabbit exits the burrow after a specific duration.
     */
    @Test
    void rabbitBurrowExitTest(){
        world.Create(3);
        Rabbit r1 = new Rabbit(world);
        world.Add(r1);
        Burrow b1 = new Burrow(world);
        world.Add(b1);
        world.SimulateSilent(25);
        assertEquals(0, b1.getRabbits().size());
    }

    /**
     * Tests a rabbit's ability to create a new burrow.
     * Verifies that a rabbit with sufficient energy creates a burrow.
     */
    @Test
    void rabbitCreateBurrowTest(){
        world.Create(3);
        Rabbit r1 = new Rabbit(world);
        r1.addEnergy(1000);
        world.Add(r1);
        world.SimulateSilent(30);
        assertEquals(1, world.getEntities(Rabbit.class).size());
    }

    /**
     * Tests a rabbit sleeping without creating a burrow.
     * Verifies that a rabbit with insufficient energy sleeps without creating a burrow.
     */
    @Test
    void rabbitSleepNoBurrowTest(){
        world.Create(3);
        Rabbit r1 = new Rabbit(world);
        r1.addEnergy(-40); // Make sure not enough energy to not create a burrow
        world.Add(r1);
        world.SimulateSilent(19);
        assertTrue(r1.getInformation().getImageKey().contains("sleep"));
    }

    /**
     * Tests a rabbit's eating behavior.
     * Verifies that grass is consumed by a rabbit.
     */
    @Test
    void rabbitEatTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 1));
        world.Add(new Rabbit(world), new Location(2, 2));
        world.SimulateSilent(30);
        assertSame(g1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests a rabbit's response to a predator's presence.
     * Verifies that a rabbit runs away from a nearby predator.
     */
    @Test
    void rabbitRunAwayTest(){
        world.Create(10);
        Rabbit r1 = new Rabbit(world);
        world.Add(r1, new Location(2, 2));
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(2, 6));
        world.SimulateSilent(1);
        assertEquals(2, r1.getCurrentLocation().getX());
        assertEquals(1, r1.getCurrentLocation().getY());
    }
}