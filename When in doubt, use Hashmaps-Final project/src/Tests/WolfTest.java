package Tests;

import Classes.*;
import Enums.EntityStatus;
import Enums.Gender;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The WolfTest class contains unit tests for the behaviors of wolves in a simulated world.
 * It tests the interactions of wolves with packs, their hunting, eating behaviors, and breeding.
 */
class WolfTest {
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
     * Tests the process of a wolf joining a pack.
     * Verifies that a wolf is successfully added to the pack and recognized as a pack member.
     */
    @Test
    void joinPackTest(){
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
     * Tests a wolf's hunting and consumption of live fauna (like rabbits).
     * Verifies that the wolf consumes live prey, leading to the prey's death.
     */
    @Test
    void eatFaunaTest(){
        world.Create(3);
        Wolf w1 = new Wolf(world);
        world.Add(w1, new Location(1, 1));
        Pack p1 = new Pack(world);
        p1.addWolf(w1);
        Rabbit r1 = new Rabbit(world);
        world.Add(r1, new Location(1, 2));
        world.SimulateSilent(8);
        assertSame(r1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests a wolf's consumption of carcasses.
     * Verifies that the wolf can consume dead animals (carcasses).
     */
    @Test
    void eatCarcassTest(){
        world.Create(3);
        Wolf w1 = new Wolf(world);
        world.Add(w1, new Location(1, 1));
        Pack p1 = new Pack(world);
        p1.addWolf(w1);
        Rabbit r1 = new Rabbit(world, false, true);
        world.Add(r1, new Location(1, 2));
        world.SimulateSilent(8);
        assertSame(r1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests the initiation of a hunt by a wolf pack.
     * Verifies that a pack starts hunting when suitable prey is nearby.
     */
    @Test
    void startHuntWithPackTest(){
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

    /**
     * Tests the breeding behavior of wolves within a pack.
     * Verifies that wolves breed successfully and produce offspring.
     */
    @Test
    void wolfBreedTest(){ // custom breed behaviour for wolf
        world.Create(3);
        Wolf w1 = new Wolf(world);
        w1.setGender(Gender.MALE);
        w1.addEnergy(10000); // Make sure enough energy to breed
        world.Add(w1, new Location(2, 2));
        Wolf w2 = new Wolf(world);
        w2.addEnergy(10000);
        w2.setGender(Gender.FEMALE);
        world.Add(w2, new Location(2, 1));
        Pack p1 = new Pack(world);
        p1.addWolf(w1);
        p1.addWolf(w2);
        world.SimulateSilent(10);
        assertTrue(world.getEntities(Wolf.class).size() >= 3);
    }
}