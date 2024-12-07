package Classes.Abstracts;

import Classes.Bear;
import Classes.Fungi;
import Enums.Age;
import Enums.EntityStatus;
import Enums.Gender;
import Exceptions.LocationBlockedException;
import Exceptions.NoEntityException;
import Interfaces.*;
import itumulator.executable.DisplayInformation;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Random;
/**
 * An abstract class representing an animal entity in the simulation.
 */
public abstract class Animal extends Entity implements IFauna, INewDayListener, INewNightListener {
    protected String DisplayInformationKey = "animal";
    protected EntityStatus status = EntityStatus.ALIVE;
    protected Age ageStatus = Age.ADULT;
    protected int ageDays = 0; // In days
    protected int adultAgeThreshold = 3; // Age when animal is an adult
    protected int carcassAgeLength = 4; // Length of time in days that a carcass decomposes
    protected int maxAgeThreshold = 10; // Age when animal dies into a carcass
    protected int currentHealthLevel = 30;
    protected int energyDrain = 5;
    private int currentEnergyLevel = 100; // 100 is maximum, any surplus energy is used to restore health
    protected Gender gender = new Random().nextBoolean() ? Gender.MALE : Gender.FEMALE;

    /**
     * Constructs a new animal entity.
     *
     * @param w The world service where the animal belongs.
     */
    public Animal(IWorldService w) {
        super(w);
        logs.logEvent(this.DisplayInformationKey + " has been summoned");
        ageDays = adultAgeThreshold + 1;
        w.getNewDayEvent().addListener(this);
        w.getNewNightEvent().addListener(this);
        di = new DisplayInformation(Color.lightGray, DisplayInformationKey, true);
    }

    /**
     * Constructs a new animal entity with options to specify if it's a child and if it's dead.
     *
     * @param w       The world service where the animal belongs.
     * @param IsChild True if the animal is a child, false otherwise.
     * @param IsDead  True if the animal is dead, false otherwise.
     */
    public Animal(IWorldService w, boolean IsChild, boolean IsDead) {
        super(w);
        logs.logEvent(this.DisplayInformationKey + " has been summoned");
        w.getNewDayEvent().addListener(this);
        w.getNewNightEvent().addListener(this);
        if(IsChild) {
            ageStatus = Age.CHILD;
            ageDays = 0;
            di = new DisplayInformation(Color.lightGray, DisplayInformationKey + "-small", true);
        }
        else {
            ageDays = adultAgeThreshold + 1;
            di = new DisplayInformation(Color.lightGray, DisplayInformationKey, true);
        }
        if(IsDead) kill();
    }

    /**
     * Moves the animal to a nearby empty tile and manages energy and health levels.
     */
    protected void move()
    {
        try {
            this.currentLocation = worldService.MoveToNearby(this);
            addEnergy(-energyDrain);
            eat();
        }
        catch(LocationBlockedException e) { // Don't drain energy
            eat();
        }
        finally {
            if (currentHealthLevel < 0) kill();
        }
        logs.logEvent(this.DisplayInformationKey + " has moved");
    }

    /**
     * Initiates the breeding process for the animal.
     *
     * @throws NoEntityException If no offspring is produced during breeding.
     */
    public void Breed() throws NoEntityException {
        if (currentEnergyLevel < 50 || gender == Gender.FEMALE) return;
        List<IFauna> Animals = worldService.getSurroundingEntities(currentLocation, 1, IFauna.class);
        for (IFauna animal : Animals)
            if (this.getClass().isInstance(animal) && animal.getAgeStatus() == Age.ADULT && animal.getGender() == Gender.FEMALE && animal.getEnergy() > 50) {
                try {
                    Class<? extends Animal> currentClass = this.getClass();
                    // Use the constructor with 3 parameters
                    Constructor<? extends Animal> constructor = currentClass.getDeclaredConstructor(IWorldService.class, Boolean.class, Boolean.class);
                    constructor.setAccessible(true);
                    // Create a new instance of the same type with parameters
                    Animal baby = constructor.newInstance(worldService, true, false);
                    worldService.AddToSurrounding(baby, currentLocation, 2);
                     animal.addEnergy(-25);
                     addEnergy(-25);
                    logs.logEvent(this.DisplayInformationKey + " has bred");
                    return;
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new NoEntityException("No kid made");
                }
            }
        throw new NoEntityException("No kid made");
    }

    /**
     * Performs the eating behavior specific to the animal.
     */
    protected abstract void eat();

    /**
     * Adds energy to the animal, potentially healing it if energy surplus is available.
     *
     * @param amount The amount of energy to add (or subtract if negative).
     */
    public void addEnergy(int amount)
    {
        if(amount > 0) { // Heal if surplus energy
            if (currentEnergyLevel + amount > 100) addHealth(amount / 2);
            else currentEnergyLevel += amount;
        }
        else {
            if (currentEnergyLevel + amount <= 0) addHealth(amount); // starvation
            else currentEnergyLevel += amount;
        }
        if (currentHealthLevel < 0) kill();
        logs.logEvent(this.DisplayInformationKey + " has " + amount + " energy added");
    }

    /**
     * Adds health to the animal.
     *
     * @param amount The amount of health to add (or subtract if negative).
     */
    public void addHealth(int amount)
    {
        if(amount > 0) currentHealthLevel += amount;
        else {
            currentHealthLevel += amount;
            if(currentHealthLevel <= 0) kill();
        }
        logs.logEvent(this.DisplayInformationKey + " has " + amount + " health added");
    }

    /**
     * Handles the behavior of the animal on a new day.
     *
     * @param DayNumber The new day number.
     */
    @Override
    public void onNewDay(int DayNumber)
    {
        if(status == EntityStatus.DEAD) return;
        this.ageDays++;
        if(status == EntityStatus.ALIVE) {   // Grow twice every new day
            if(ageDays > maxAgeThreshold) {
                kill(); // Die if too old
                return;
            }
            if(ageDays > adultAgeThreshold && ageStatus == Age.CHILD)
                ageStatus = Age.ADULT;
            di = ageStatus == Age.CHILD ? new DisplayInformation(Color.lightGray, DisplayInformationKey + "-small", true) : new DisplayInformation(Color.lightGray, DisplayInformationKey, true);
        }
        else if(status == EntityStatus.CARCASS || status == EntityStatus.INFECTEDCARCASS) {
            if(ageDays > maxAgeThreshold + carcassAgeLength) // Carcass expires
            {
                worldService.Remove(this);
                if(status == EntityStatus.INFECTEDCARCASS)
                    worldService.Add(new Fungi(worldService, ageStatus == Age.CHILD), currentLocation);
                status = EntityStatus.DEAD;
                System.out.println(this.getClass().toString() + " has died");
            }
        }
    }

    /**
     * Handles the behavior of the animal on a new night.
     *
     * @param NightNumber The new night number.
     */
    @Override
    public void onNewNight(int NightNumber) {
        if(status == EntityStatus.DEAD) return;
        if(status == EntityStatus.ALIVE)
            sleep();
        else if(status == EntityStatus.INFECTEDCARCASS) {
            this.ageDays++; // Age faster if infected
            TryToInfect();
        }
    }

    /**
     * Causes the animal to sleep, changing its display information accordingly.
     */
    @Override
    public void sleep() {
        di = ageStatus == Age.CHILD ? new DisplayInformation(Color.lightGray, DisplayInformationKey + "-small-sleeping") : new DisplayInformation(Color.lightGray, DisplayInformationKey + "-sleeping");
        logs.logEvent(this.DisplayInformationKey + " has slept");
    }

    /**
     * Kills the animal, turning it into a carcass or infected carcass.
     */
    @Override
    public void kill() {
        if(!(status == EntityStatus.ALIVE)) return;
        di = ageStatus == Age.CHILD ? new DisplayInformation(Color.red, "carcass-small") : new DisplayInformation(Color.red, "carcass");
        status = EntityStatus.CARCASS;
        carcassAgeLength = ageStatus == Age.CHILD ? carcassAgeLength - 1 : carcassAgeLength;
        ageDays = maxAgeThreshold;
        logs.logEvent(this.DisplayInformationKey + " has died");
    }

    /**
     * Consumes the carcass or infected carcass.
     */
    @Override
    public void consume() {
        if(!(status == EntityStatus.CARCASS || status == EntityStatus.INFECTEDCARCASS)) return;
        worldService.Remove(this);
        if(status == EntityStatus.INFECTEDCARCASS)
            worldService.AddToSurrounding(new Fungi(worldService, ageStatus == Age.CHILD), currentLocation, 2);
        status = EntityStatus.DEAD;
        logs.logEvent(this.DisplayInformationKey + " carcass has died");
    }

    /**
     * Infects the carcass, turning it into an infected carcass.
     */
    @Override
    public void infect() {
        if(!(status == EntityStatus.CARCASS))
            return;
        status = EntityStatus.INFECTEDCARCASS;
        logs.logEvent(this.DisplayInformationKey + " has been infected");
    }

    /**
     * Tries to infect surrounding carcasses with the spore.
     */
    public void TryToInfect()
    {
        List<IFauna> list = worldService.getSurroundingEntities(currentLocation, 2, IFauna.class);
        for (IFauna Entity : list)
            if(Entity.getStatus() == EntityStatus.CARCASS)
                Entity.infect();
    }

    /**
     * Gets the current energy level of the animal.
     *
     * @return The current energy level.
     */
    public int getEnergy() { return this.currentEnergyLevel; }

    /**
     * Gets the current health level of the animal.
     *
     * @return The current health level.
     */
    public int getHealth() { return this.currentHealthLevel; }

    /**
     * Gets the status of the animal (e.g., alive, carcass, infected carcass).
     *
     * @return The status of the animal.
     */
    @Override
    public EntityStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status of the animal.
     *
     * @param status The status to set.
     */
    @Override
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    /**
     * Gets the age status of the animal (e.g., child, adult).
     *
     * @return The age status of the animal.
     */
    @Override
    public Age getAgeStatus() { return this.ageStatus; }

    /**
     * Gets the gender of the animal.
     *
     * @return The gender of the animal.
     */
    @Override
    public Gender getGender() { return this.gender; }

    /**
     * Sets the gender of the animal.
     *
     * @param gender The gender to set.
     */
    public void setGender(Gender gender) { this.gender = gender; } // Mostly for unit tests
}
