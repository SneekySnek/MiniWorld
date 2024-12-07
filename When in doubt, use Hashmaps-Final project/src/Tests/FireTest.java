package Tests;

import Classes.Fire;
import Classes.Grass;
import Classes.Pack;
import Classes.Rabbit;
import Enums.EntityStatus;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The FireTest class contains unit tests for the behaviors of fire in a simulated world.
 * It tests the lifecycle of fire, including its extinguishing, its effect on other entities, and its ability to spread.
 */
class FireTest {
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
     * Tests the extinguishing of fire over time.
     * Verifies that fire becomes inactive after a specific duration.
     */
    @Test
    void fireExtinguishesTest(){
        world.Create(3);
        Fire f1 = new Fire(world);
        world.Add(f1);
        world.SimulateSilent(40);
        assertFalse(f1.isActive());
    }

    /**
     * Tests the impact of fire on other entities like animals.
     * Verifies that animals lose health when in contact with fire.
     */
    @Test
    void fireBurnsTest(){
        world.Create(3);
        Fire f1 = new Fire(world);
        world.Add(f1, new Location(2, 2));
        Rabbit r1 = new Rabbit(world);
        world.Add(r1, new Location(2, 2));
        world.SimulateSilent(2);
        assertTrue(r1.getHealth() < 25);
    }

    /**
     * Tests the spreading of fire to nearby flammable entities like grass.
     * Verifies that nearby flammable entities catch fire and change status accordingly.
     */
    @Test
    void fireSpreadsTest(){
        world.Create(3);
        Fire f1 = new Fire(world);
        world.Add(f1, new Location(2, 2));
        Grass g1 = new Grass(world);
        world.Add(g1, new Location(2, 1));
        world.SimulateSilent(10);
        assertSame(g1.getStatus(), EntityStatus.DEAD);
        assertEquals(2, world.getEntities(Fire.class).size());
    }
}