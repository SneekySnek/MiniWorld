package Classes;

import Classes.Abstracts.Animal;
import Enums.Age;
import Enums.EntityStatus;
import Enums.Gender;
import Exceptions.LocationBlockedException;
import Exceptions.NoEntityException;
import Interfaces.*;
import itumulator.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Wolf class represents a wolf in a simulated world.
 * It extends the Animal class and implements the IPack and IPredator interfaces,
 * indicating its role as a pack animal and a predator in the ecosystem.
 */
public class Wolf extends Animal implements IPack, IPredator {
    private Pack pack;

    /**
     * Constructs a new Wolf instance.
     *
     * @param w The world service interface for the wolf.
     */
    public Wolf(IWorldService w) {
        super(w);
        this.DisplayInformationKey = "wolf";
        this.currentHealthLevel = 50;
        this.maxAgeThreshold = 12;
    }

    /**
     * Constructs a new Wolf instance with specified child and dead statuses.
     *
     * @param w        The world service interface for the wolf.
     * @param isChild  Indicates if the wolf is a child.
     * @param isDead   Indicates if the wolf is dead.
     */
    public Wolf(IWorldService w, Boolean isChild, Boolean isDead) {
        super(w, isChild, isDead);
        this.DisplayInformationKey = "wolf";
        this.currentHealthLevel = 50;
        this.maxAgeThreshold = 12;
    }

    /**
     * Defines the behavior of the wolf in the world.
     *
     * @param world The world in which the wolf acts.
     */
    @Override
    public void act(World world) {
        if (status != EntityStatus.ALIVE && pack != null) pack.removeWolf(this);
        if (status != EntityStatus.ALIVE || pack == null) return;
        if (world.isDay() && !pack.isHunting())
            move();
    }

    /**
     * Handles the wolf's movement behavior, including hunting and avoidance tactics.
     */
    @Override
    protected void move() {
        if(!pack.isHunting())
        {
            List<IPrey> Animals = worldService.getSurroundingEntities(currentLocation, 4, IPrey.class);
            try {
                if(Animals.size() == 1 && Animals.get(0) == this) throw new NoEntityException("no animals nearby");
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
    }

    /**
     * Manages the wolf's eating behavior, including consuming fauna, carcasses,
     * and engaging in fights with other wolves not in its pack.
     */
    @Override
    protected void eat() {
        try {
            List<Wolf> wolves = worldService.getSurroundingEntities(currentLocation, 1, Wolf.class);
            if(!wolves.isEmpty())  // Fight nearby wolves not in the pack
                for(Wolf wolf : wolves)
                    if(pack != null)
                        if(!pack.isInPack(wolf) && wolf.getStatus() == EntityStatus.ALIVE) {
                            pack.hunt(wolf);
                            return;
                        }
                        else if(wolf.getGender() != this.gender) {
                            Breed(); // Try to breed nearby female wolf
                            return;
                        }
            List<IFauna> animals = worldService.getSurroundingEntities(currentLocation, 1, IFauna.class);
            for(IFauna animal : animals) // Eat if there is a carcass
            {
                if(animal.getStatus() == EntityStatus.CARCASS || animal.getStatus() == EntityStatus.INFECTEDCARCASS) {
                    if(animal.getStatus() == EntityStatus.INFECTEDCARCASS || animal instanceof IPoisonous)
                        addHealth(-30); // Damage from infected carcass or poison
                    addEnergy(animal.getStatus() == EntityStatus.CARCASS ? 30 : 10);
                    animal.consume();
                }
                else if(animal instanceof IPrey)
                {
                    addEnergy(animal.getEnergy() / 2);
                    animal.kill();
                    if(animal instanceof IPoisonous) addHealth(-30); // Damage from infected carcass or poison
                }
                else if(animal instanceof IPredator && !(animal instanceof Wolf) && pack != null)
                {
                    pack.hunt(animal);
                    return;
                }
            }
        }
        catch(NoEntityException ee) { /* There are no creatures nearby */ }
    }

    /**
     * Handles the breeding behavior of the wolf, including creating new offspring
     * and adding them to the pack.
     */
    @Override // Override as custom breeding to add baby to pack
    public void Breed() {
        if (this.getEnergy() < 50 || gender == Gender.FEMALE) return;
        List<Wolf> Wolves = worldService.getSurroundingEntities(currentLocation, 1, Wolf.class);
        for (Wolf wolf : Wolves)
            if (wolf.getAgeStatus() == Age.ADULT && wolf.getGender() == Gender.FEMALE && wolf.getEnergy() > 50) {
                    Wolf baby = new Wolf(worldService, true, false);
                    worldService.AddToSurrounding(baby, currentLocation, 2);
                    pack.addWolf(baby);
                    wolf.addEnergy(-25);
                    addEnergy(-25);
                    return;
            }
    }

    /**
     * Returns the pack to which the wolf belongs.
     *
     * @return The pack of the wolf.
     */
    public Pack getPack(){ return pack; }

    /**
     * Sets the pack for the wolf.
     *
     * @param pack The pack to be assigned to the wolf.
     */
    public void setPack(Pack pack){ this.pack = pack; }

    /**
     * Clears the pack association of the wolf.
     */
    public void clearPack(){ this.pack = null; }
}
