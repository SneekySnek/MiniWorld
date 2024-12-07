package Tests;

import Classes.*;
import Classes.Rabbit;
import Enums.Age;
import Enums.EntityStatus;
import Enums.Gender;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The AnimalTest class contains unit tests for various behaviors of animals in a simulated world.
 * It tests the lifecycle events like aging, decomposing, infection, breeding, and movement of animals.
 */
class AnimalTest {
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
     * Tests the aging process of an animal.
     * Verifies if an animal becomes a carcass after a specific duration.
     */
    @Test
    void animalAgingTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world);
        a1.addEnergy(10000); // Make it not starve to death
        world.Add(a1);
        world.SimulateSilent(180);
        assertTrue(a1.getStatus() == EntityStatus.CARCASS);
    }

    /**
     * Tests the transition of an animal from baby to adult.
     * Verifies if a young animal becomes an adult after a specific duration.
     */
    @Test
    void animalBabyToAdultTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world, true, false);
        a1.addEnergy(10000); // Make it not starve to death
        world.Add(a1);
        world.SimulateSilent(100);
        assertTrue(a1.getAgeStatus() == Age.ADULT);
    }

    /**
     * Tests the decomposition of a carcass.
     * Verifies if a dead animal changes status to decomposed after a specific duration.
     */
    @Test
    void animalCarcassDecomposeTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world, false, true);
        world.Add(a1);
        world.SimulateSilent(100);
        assertSame(a1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests the infection of a carcass by fungi.
     * Verifies if a carcass near fungi becomes infected after a specific duration.
     */
    @Test
    void animalInfectedCarcassTest(){
        world.Create(3);
        world.Add(new Fungi(world), new Location(2, 1));
        Rabbit a1 = new Rabbit(world, false, true);
        world.Add(a1, new Location(2, 2));
        world.SimulateSilent(40);
        assertSame(a1.getStatus(), EntityStatus.INFECTEDCARCASS);
    }

    /**
     * Tests the infection spreading from an infected carcass to another carcass.
     * Verifies if a carcass becomes infected when placed near an infected carcass.
     */
    @Test
    void animalInfectCarcassTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world, false, true);
        a1.setStatus(EntityStatus.INFECTEDCARCASS);
        world.Add(a1, new Location(2, 2));
        Rabbit a2 = new Rabbit(world, false, true);
        world.Add(a2, new Location(2, 1));
        world.SimulateSilent(40);
        assertSame(a2.getStatus(), EntityStatus.INFECTEDCARCASS);
    }

    /**
     * Tests the breeding process of animals.
     * Verifies if animals breed successfully and produce offspring.
     */
    @Test
    void animalBreedingTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world);
        a1.addEnergy(10000); // Make sure enough energy to breed
        a1.setGender(Gender.MALE);
        world.Add(a1, new Location(2, 2));
        Rabbit a2 = new Rabbit(world);
        a2.addEnergy(10000);
        a2.setGender(Gender.FEMALE);
        world.Add(a2, new Location(2, 1));
        world.Add(new Grass(world));
        world.Add(new Grass(world));
        world.Add(new Grass(world));
        world.Add(new Grass(world));
        world.SimulateSilent(100);
        assertTrue(world.getEntities(Rabbit.class).size() > 3);
    }

    /**
     * Tests the death of an animal due to external factors like fire.
     * Verifies if an animal becomes a carcass when exposed to fire.
     */
    @Test
    void animalKilledTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world);
        world.Add(a1, new Location(2, 2));
        world.Add(new Fire(world), new Location(2, 1));
        world.Add(new Fire(world), new Location(1, 1));
        world.Add(new Fire(world), new Location(2, 0));
        world.SimulateSilent(80);
        assertSame(a1.getStatus(), EntityStatus.CARCASS);
    }

    /**
     * Tests the consumption of an animal by a predator.
     * Verifies if an animal becomes dead when consumed by a predator.
     */
    @Test
    void animalConsumeTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world);
        world.Add(a1, new Location(2, 2));
        world.Add(new Bear(world), new Location(2, 1));
        world.SimulateSilent(30);
        assertSame(a1.getStatus(), EntityStatus.DEAD);
    }

    /**
     * Tests the movement ability of an animal.
     * Verifies if an animal changes its location when simulating movement.
     */
    @Test
    void animalMoveTest(){
        world.Create(3);
        Rabbit a1 = new Rabbit(world);
        world.Add(a1, new Location(2, 2));
        world.SimulateSilent(1);
        assertTrue(a1.getCurrentLocation().getX() != 2 && a1.getCurrentLocation().getY() != 2);
    }
}