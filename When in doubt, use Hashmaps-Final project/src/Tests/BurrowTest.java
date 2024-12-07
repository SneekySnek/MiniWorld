package Tests;

import Classes.Burrow;
import Classes.Rabbit;
import Exceptions.NoEntityException;
import Services.WorldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The BurrowTest class contains unit tests for the behaviors of burrows and their interactions with rabbits in a simulated world.
 * It tests scenarios like adding rabbits to a burrow, handling burrow overflow, and rabbits entering and exiting a burrow.
 */
class BurrowTest {
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
     * Tests the addition of a rabbit to a burrow.
     * Verifies that a rabbit can successfully enter a burrow.
     */
    @Test
    void rabbitAddedToBurrowTest(){
        world.Create(3);
        Burrow b1 = new Burrow(world);
        world.Add(b1);
        Rabbit r1 = new Rabbit(world);
        world.Add(r1);
        b1.Enter(r1);
        assertEquals(1, b1.getRabbits().size());
    }

    /**
     * Tests the scenario when a burrow is full and cannot accommodate more rabbits.
     * Verifies that an exception is thrown when trying to add a rabbit to a full burrow.
     */
    @Test
    void burrowOverflowTest(){
        world.Create(10);
        Burrow b1 = new Burrow(world);
        world.Add(b1);
        boolean check = false;
        for(int i = 0; i <= 8; i++) {
            Rabbit r = new Rabbit(world);
            world.Add(r);
            b1.Enter(r);
        }
        try {
            Rabbit r9 = new Rabbit(world);
            world.Add(r9);
            b1.Enter(r9);
        } catch (NoEntityException e) {
            check = true;
        }
        assertTrue(check);
    }

    /**
     * Tests if a rabbit successfully enters a burrow during the simulation.
     * Verifies that a rabbit enters a burrow by checking log messages.
     */
    @Test
    void burrowRabbitEnterTest(){
        world.Create(3);
        Burrow b1 = new Burrow(world);
        world.Add(b1);
        world.Add(new Rabbit(world));
        AtomicBoolean check = new AtomicBoolean(false);
        b1.getLogger().addListener(log -> {
            if(log.contains("enter")) check.set(true);
        });
        world.SimulateSilent(30);
        assertTrue(check.get());
    }

    /**
     * Tests the exit of a rabbit from a burrow.
     * Verifies that a rabbit can successfully exit a burrow and is removed from it.
     */
    @Test
    void burrowRabbitExitTest(){
        world.Create(3);
        Burrow b1 = new Burrow(world);
        world.Add(b1);
        Rabbit r1 = new Rabbit(world);
        world.Add(r1);
        b1.Enter(r1);
        b1.Exit(r1);
        assertEquals(0, b1.getRabbits().size());
    }
}