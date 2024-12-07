package Classes;

import Classes.Abstracts.Animal;
import Enums.Age;
import Enums.EntityStatus;
import Enums.Gender;
import Exceptions.LocationBlockedException;
import Exceptions.NoEntityException;
import Interfaces.*;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The Bear class represents a bear in a simulated world.
 * It extends the Animal class and implements the IPredator interface,
 * indicating its role as a predator. The class handles bear-specific behaviors
 * like movement, eating, breeding, and fighting.
 */
public class Bear extends Animal implements IPredator {
    private Location spawnLocation; // The bear's spawn location
    private static final int territoryRadius = 4; // The radius of the bear's territory

    /**
     * Constructs a new Bear instance.
     *
     * @param w The world service interface for the bear.
     */
    public Bear(IWorldService w) {
        super(w);
        this.DisplayInformationKey = "bear";
        this.currentHealthLevel = 100;
        this.maxAgeThreshold = 15;
    }

    /**
     * Constructs a new Bear instance with specified child and dead statuses.
     *
     * @param w        The world service interface for the bear.
     * @param isChild  Indicates if the bear is a child.
     * @param isDead   Indicates if the bear is dead.
     */
    public Bear(IWorldService w, Boolean isChild, Boolean isDead) {
        super(w, isChild, isDead);
        this.DisplayInformationKey = "bear";
        this.currentHealthLevel = 100;
        this.maxAgeThreshold = 15;
    }

    /**
     * Defines the behavior of the bear in the world.
     *
     * @param world The world in which the bear acts.
     */
    @Override
    public void act(World world) {
        if (status != EntityStatus.ALIVE) return;
        if (world.isDay())
            move();
    }

    /**
     * Handles the bear's movement behavior within its territory.
     */
    @Override
    public void move() {
        List<IFauna> Animals = worldService.getSurroundingEntities(spawnLocation, territoryRadius, IFauna.class);
        try {
            if(Animals.size() == 1 && Animals.get(0) == this) throw new NoEntityException("Only bear in territory");
            if(Animals.contains(this)) Animals.remove(this);
            IFauna nearestAnimal = worldService.findNearest(currentLocation, Animals);
            worldService.MoveTowards(this, nearestAnimal);
            addEnergy(-energyDrain);
        }
        catch(NoEntityException | LocationBlockedException e){
            super.move();
        }
        finally {
            eat();
        }
    }

    /**
     * Manages the bear's eating behavior, including consuming berries, carcasses,
     * prey, and engaging in fights with other predators.
     */
    @Override
    protected void eat() {
        List<BerryBush> bushes = worldService.getSurroundingEntities(currentLocation, 1, BerryBush.class);
        if(!bushes.isEmpty())
            if(bushes.get(0).hasBerries()) {
                addEnergy(15);
                bushes.get(0).consume();
            }

        List<IFauna> animals = worldService.getSurroundingEntities(currentLocation, 1, IFauna.class);
        if(animals.isEmpty()) return;
        for(IFauna animal : animals) // Eat if there is a carcass
        {
            if(animal.getStatus() == EntityStatus.CARCASS || animal.getStatus() == EntityStatus.INFECTEDCARCASS) {
                if(animal.getStatus() == EntityStatus.INFECTEDCARCASS || animal instanceof IPoisonous)
                    addHealth(-30); // Damage from infected carcass or poison
                addEnergy(animal.getStatus() == EntityStatus.CARCASS ? 30 : 10);
                animal.consume();
                break;
            }
        }

        List<IPredator> predators = worldService.getSurroundingEntities(currentLocation, 1, IPredator.class);
        List <IPrey> prey = worldService.getSurroundingEntities(currentLocation, 1, IPrey.class); // Collect non predators in animals
        if(predators.isEmpty() && !prey.isEmpty()) { // If there are no predators then eat the prey
            addEnergy(prey.get(0).getEnergy());
            animals.get(0).kill();
        }
        else if(!predators.isEmpty()) { // Prioritise fighting bears
            List<Bear> bears = predators.stream().filter(Bear.class::isInstance).map(Bear.class::cast).toList();
            if(!bears.isEmpty()) {
                Bear bear = bears.get(0); // If female try to breed
                if(gender == Gender.MALE && bear.getGender() == Gender.FEMALE) Breed();
                else fight(bear); return;
            } // Filter out bears
            predators = predators.stream().filter(predator -> !(predator instanceof Bear)).toList();
            if(!predators.isEmpty()) fight(predators.get(0));
        }
    }

    /**
     * Handles the breeding behavior of the bear, including creating new offspring
     * and engaging in fights if breeding is not successful.
     */
    @Override
    public void Breed() {
        try {
            super.Breed();
        }
       catch(NoEntityException e) { // Failed reproduction fight it
            List<Bear> bears = worldService.getSurroundingEntities(currentLocation, 1, Bear.class);
            fight(bears.get(0));
        }
    }

    /**
     * Engages the bear in a fight with another predator.
     *
     * @param Entity The predator the bear is fighting.
     */
    public void fight(IPredator Entity){
        if(Entity instanceof Bear && Entity.getAgeStatus() == Age.CHILD) return; // Don't kill kids
        Entity.addHealth(-1 * (new Random().nextInt(11) + 30));
        if(Entity.getStatus() == EntityStatus.CARCASS) // It ded
            addEnergy((Entity instanceof Bear) ? 200 : 100);
    }

    /**
     * Sets the location of the bear, initializing its spawn location if not already set.
     *
     * @param location The new location of the bear.
     */
    @Override
    public void setLocation(Location location) {
        if(spawnLocation == null) spawnLocation = location;
        currentLocation = location;
    }
}
