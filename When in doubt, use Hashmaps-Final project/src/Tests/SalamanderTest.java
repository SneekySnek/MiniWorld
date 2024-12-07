package Tests;

import Classes.*;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;


class SalamanderTest {
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
     * Tests a salamander's ability to eat worms in grass.
     * Verifies that the worm count in grass decreases after a salamander's interaction.
     */
    @Test
    void eatWormsTest(){
        world.Create(3);
        Grass g1 = new Grass(world);
        g1.addWorms(100);
        world.Add(g1, new Location(2, 1));
        Salamander s1 = new Salamander(world);
        world.Add(s1, new Location(2, 2));
        world.SimulateSilent(30);
        assertTrue(g1.getWorms() < 50);
    }

    /**
     * Tests a salamander's ability to create fire.
     * Verifies that a fire entity is created in the world when a salamander uses its fire ability.
     */
    @Test
    void fireTest(){
        world.Create(3);
        Salamander s1 = new Salamander(world);
        world.Add(s1);
        AtomicBoolean check = new AtomicBoolean(false);
        s1.getLogger().addListener(log -> {
            if(log.contains("fire"))
                if(!world.getEntities(Fire.class).isEmpty())
                    check.set(true);
        });
        world.SimulateSilent(100);
        assertTrue(check.get());
    }

    /**
     * Tests the poisonous effect of a salamander on a predator.
     * Verifies that a predator's health decreases when it consumes a poisonous salamander.
     */
    @Test
    void poisonTest(){
        world.Create(3);
        Salamander s1 = new Salamander(world);
        world.Add(s1, new Location(2, 2));
        Bear b1 = new Bear(world);
        world.Add(b1, new Location(2, 1));
        AtomicBoolean check = new AtomicBoolean(false);
        b1.getLogger().addListener(log -> {
            if(log.contains("has -30 health added"))
                check.set(true);
        });
        world.SimulateSilent(5);
        assertTrue(check.get());
    }
}