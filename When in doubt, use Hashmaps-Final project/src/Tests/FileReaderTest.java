package Tests;

import Classes.*;
import Enums.EntityStatus;
import Interfaces.IEntity;
import Interfaces.IFauna;
import Services.ReflectionFileReader;
import Services.WorldService;
import itumulator.world.Location;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The FileReaderTest class contains unit tests for the ReflectionFileReader class.
 * It tests the functionality of reading simulation setup files and creating entities in the world.
 */
class FileReaderTest {
    private final String readFileTestLocation = "data/input-filer/Tests/readFileTest.txt";
    private final String addEntityTestLocation = "data/input-filer/Tests/addEntityTest.txt";
    private final String addEntitiesTestLocation = "data/input-filer/Tests/addEntitiesTest.txt";
    private final String addDeadEntityTestLocation = "data/input-filer/Tests/addDeadEntityTest.txt";
    private final String addEntityAtLocationTestLocation = "data/input-filer/Tests/addEntityAtLocationTest.txt";
    private final String addRangeOfEntitiesTestLocation = "data/input-filer/Tests/addRangeOfEntitiesTest.txt";
    private final String addWolfPackTestLocation = "data/input-filer/Tests/addWolfPackTest.txt";
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
     * Tests reading basic configuration from a file.
     */
    @Test
    void readFileTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(readFileTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assertEquals(10, world.getSize());
    }

    /**
     * Tests adding a single entity to the world from a file.
     */
    @Test
    void addEntityTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(addEntityTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assertEquals(1, world.getEntities(IEntity.class).size());
        assertTrue(world.getEntities(IEntity.class).get(0) instanceof Fungi);
    }

    /**
     * Tests adding multiple entities to the world from a file.
     */
    @Test
    void addEntitiesTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(addEntitiesTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assertEquals(6, world.getEntities(IEntity.class).size());
        assertTrue(world.getEntities(IEntity.class).get(0) instanceof Rabbit);
    }

    /**
     * Tests adding dead entities to the world from a file.
     */
    @Test
    void addDeadEntityTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(addDeadEntityTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        boolean check = true;
        assertEquals(4, world.getEntities(IEntity.class).size());
        for(IFauna animal : world.getEntities(IFauna.class)) // check if all dead
            if(animal.getStatus() != EntityStatus.CARCASS) check = false;
        assertTrue(check);
    }

    /**
     * Tests adding an entity at a specific location to the world from a file.
     */
    @Test
    void addEntityAtLocationTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(addEntityAtLocationTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assertEquals(1, world.getEntities(IEntity.class).size());
        assertEquals(2, world.getEntities(IEntity.class).get(0).getCurrentLocation().getX());
        assertEquals(2, world.getEntities(IEntity.class).get(0).getCurrentLocation().getY());
    }

    /**
     * Tests creating a pack of wolves from a file.
     */
    @Test
    void addWolfPackTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(addWolfPackTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        boolean check = true;
        assertEquals(1, world.getEntities(Pack.class).size());
        assertEquals(8, world.getEntities(Wolf.class).size());
        for(Wolf wolf : world.getEntities(Wolf.class)) // check if all wolves are in pack
            if(wolf.getPack() != world.getEntities(Pack.class).get(0)) check = false;
        assertTrue(check);
    }

    /**
     * Tests adding a range of entities to the world from a file.
     */
    @Test
    void addRangeOfEntitiesTest(){
        ReflectionFileReader f = new ReflectionFileReader(world);
        try {
            f.ReadFile(addRangeOfEntitiesTestLocation);
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        assertTrue(world.getEntities(IEntity.class).size() < 11 && world.getEntities(IEntity.class).size() > 3);
        assertTrue(world.getEntities(IEntity.class).get(0) instanceof Grass);
    }
}