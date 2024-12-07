package Tests;

import Classes.Fire;
import Classes.Fungi;
import Classes.Grass;
import Classes.Rabbit;
import Enums.EntityStatus;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The FungiTest class contains unit tests for the behaviors of fungi in a simulated world.
 * It tests various aspects of fungi lifecycle, such as growth, infection process, and death.
 */
class FungiTest {
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
     * Tests the appearance of fungi based on their size.
     * Verifies that small fungi have a distinct appearance compared to regular-sized fungi.
     */
    @Test
    void fungiSmallSizeTest(){
        world.Create(3);
        Fungi f1 = new Fungi(world, true);
        world.Add(f1);
        Fungi f2 = new Fungi(world);
        world.Add(f2);
        assertTrue(f1.getInformation().getImageKey().contains("small"));
        assertFalse(f2.getInformation().getImageKey().contains("small"));
    }

    /**
     * Tests the ability of fungi to infect carcasses.
     * Verifies that nearby carcasses become infected over time.
     */
    @Test
    void fungiInfectsTest(){
        world.Create(3);
        Fungi f1 = new Fungi(world);
        world.Add(f1);
        Rabbit r1 = new Rabbit(world, false, true);
        world.Add(r1);
        world.SimulateSilent(10);
        assertSame(r1.getStatus(), EntityStatus.INFECTEDCARCASS);
    }

    /**
     * Tests the lifecycle of fungi, specifically their death after a certain period.
     * Verifies that fungi die after a specific duration, indicated by a log message.
     */
    @Test
    void fungiDiesTest(){
        world.Create(3);
        Fungi f1 = new Fungi(world);
        world.Add(f1);

        Fungi f2 = new Fungi(world, true);
        world.Add(f2);
        AtomicBoolean check = new AtomicBoolean(false);
        AtomicBoolean check2 = new AtomicBoolean(false);
        f1.getLogger().addListener(log -> {
            if(log.contains("died")) check.set(true);
        });
        f2.getLogger().addListener(log -> {
            if(log.contains("died")) check2.set(true);
        });
        world.SimulateSilent(110);
        assertTrue(check.get());
        assertTrue(check2.get());
    }

    /**
     * Tests if infected carcasses spawn new fungi.
     * Verifies that new fungi appear near infected carcasses after a certain duration.
     */
    @Test
    void carcassSpawnFungiTest(){
        world.Create(3);
        Rabbit r1 = new Rabbit(world, false, true);
        r1.setStatus(EntityStatus.INFECTEDCARCASS);
        world.Add(r1);
        Rabbit r2 = new Rabbit(world, true, true);
        r2.setStatus(EntityStatus.INFECTEDCARCASS);
        world.Add(r2);
        world.SimulateSilent(100);
        assertEquals(2, world.getEntities(Fungi.class).size());
        boolean check = false;
        for(Fungi fungi : world.getEntities(Fungi.class)) // Check if one of the fungi is small
            if(fungi.getInformation().getImageKey().contains("small")) check = true;
        assertTrue(check);
    }
}