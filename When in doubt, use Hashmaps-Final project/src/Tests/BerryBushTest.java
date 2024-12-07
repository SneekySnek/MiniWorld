package Tests;

import Classes.BerryBush;
import Enums.EntityStatus;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The BerryBushTest class contains unit tests for the behaviors of berry bushes in a simulated world.
 * It tests the growth of berries on the bush and the consumption of these berries.
 */
class BerryBushTest {
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
     * Tests the growth of berries on a berry bush over time.
     * Verifies if berries grow on the bush after a specific duration.
     */
    @Test
    void growBerriesTest(){
        world.Create(3);
        BerryBush b1 = new BerryBush(world);
        world.Add(b1);
        world.SimulateSilent(200);
        assertSame(b1.hasBerries(), true);
    }

    /**
     * Tests the consumption of berries on a berry bush.
     * Verifies if the bush no longer has berries after they are consumed.
     */
    @Test
    void consumeBerriesTest(){
        world.Create(3);
        BerryBush b1 = new BerryBush(world);
        b1.addBerries(20);
        world.Add(b1);
        world.SimulateSilent(3);
        b1.consume();
        assertSame(b1.hasBerries(), false);
    }
}