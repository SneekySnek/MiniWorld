package Tests;

import Classes.Grass;
import Enums.EntityStatus;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The GrassTest class contains unit tests for the behaviors of grass in a simulated world.
 * It tests the spread of grass, presence and consumption of worms, and the lifecycle of grass.
 */
class GrassTest {
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
     * Tests the ability of grass to spread over time.
     * Verifies that new grass entities appear after a specific duration.
     */
    @Test
    void grassSpreadTest(){
        world.Create(10);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.SimulateSilent(30);
        assertTrue(world.getEntities(Grass.class).size() > 1);
    }

    /**
     * Tests the presence of worms in grass entities.
     * Verifies that worms appear in grass over time.
     */
    @Test
    void grassWormsTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.SimulateSilent(30);
        assertTrue(world.getEntities(Grass.class).size() > 1);
    }

    /**
     * Tests the consumption of grass and its transition to a dead state.
     * Verifies that grass becomes dead after being consumed.
     */
    @Test
    void grassConsumeTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1);
        g1.consume();
        world.SimulateSilent(5);
        assertSame(g1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests the consumption of worms in grass.
     * Verifies that worms can be successfully consumed from grass.
     */
    @Test
    void grassConsumeWormsTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        world.Add(g1);
        world.SimulateSilent(100);
        assertTrue(g1.ConsumeWorms() > 1);
    }

    /**
     * Tests the spread of worms from one grass entity to another.
     * Verifies that worms in one grass entity can spread to nearby grass entities.
     */
    @Test
    void grassWormsSpreadTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        Grass g2 = new Grass(world);
        world.Add(g1, new Location(2, 1));
        world.Add(g2, new Location(2, 2));
        world.SimulateSilent(30);
        assertTrue(g2.getWorms() > 1);
    }
}