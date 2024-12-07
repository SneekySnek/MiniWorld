package Tests;

import Classes.*;
import Enums.EntityStatus;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The PackTest class contains unit tests for the behaviors of wolf packs in a simulated world.
 * It tests the addition of wolves to a pack and the pack's hunting behavior.
 */
class PackTest {
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
     * Tests the addition of wolves to a pack.
     * Verifies that wolves are successfully added to the pack and recognized as pack members.
     */
    @Test
    void addWolvesTest(){
        world.Create(3);
        Pack p1 = new Pack(world);
        Wolf w1 = new Wolf(world);
        world.Add(w1);
        p1.addWolf(w1);
        world.SimulateSilent(3);
        assertTrue(p1.isInPack(w1));
        assertSame(w1.getPack(), p1);
    }

    /**
     * Tests the hunting behavior of a wolf pack.
     * Verifies that the pack engages in hunting when a suitable prey is nearby.
     */
    @Test
    void huntTest(){
        world.Create(5);
        Pack p1 = new Pack(world);
        for(int i = 0; i < 10; i++) {
            Wolf w = new Wolf(world);
            world.Add(w);
            p1.addWolf(w);
        }
        Bear b1 = new Bear(world);
        b1.addHealth(10000);
        world.Add(b1);
        world.SimulateSilent(10);
        assertTrue(p1.isHunting());
    }
}