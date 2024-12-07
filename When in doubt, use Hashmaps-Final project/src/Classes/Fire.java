package Classes;

import Classes.Abstracts.Entity;
import Enums.EntityStatus;
import Interfaces.*;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.awt.*;
import java.util.List;

/**
 * The Fire class represents a fire entity in a simulated world.
 * It extends the Entity class and implements the NonBlocking and INewDayListener interfaces.
 * The class is responsible for simulating fire behavior in the world, affecting flora and fauna.
 */
public class Fire extends Entity implements NonBlocking, INewDayListener {
    private boolean isActive = true;

    /**
     * Constructs a new Fire instance.
     *
     * @param w The world service interface for the fire.
     */
    public Fire(IWorldService w) {
        super(w);
        this.di = new DisplayInformation(Color.green, "fire", true);
    }

    /**
     * Defines the behavior of the fire in the world, including spreading and
     * damaging nearby fauna and flora.
     *
     * @param world The world in which the fire acts.
     */
    @Override
    public void act(World world) {
        if(!isActive) return;
        List<IFauna> Animals = worldService.getSurroundingEntities(currentLocation, 1, IFauna.class);
        if(!Animals.isEmpty())
            for(IFauna animal : Animals)
                if(!(animal instanceof Salamander))
                    animal.addHealth(-5); // Burn surrounding animals
        Animals = worldService.getEntities(currentLocation, IFauna.class);
        if(!Animals.isEmpty())
            for(IFauna animal : Animals)
                if(!(animal instanceof Salamander))
                    animal.addHealth(-20); // Burn entities on fire even more
        List<IFlora> Plants = worldService.getSurroundingEntities(currentLocation, 1, IFlora.class);
        if(!Plants.isEmpty())
            for(IFlora plant : Plants) { // Burn and spread through surrounding plants
                plant.setStatus(EntityStatus.DEAD);
                Location loc = plant.getCurrentLocation();
                worldService.Remove(plant);
                worldService.Add(new Fire(worldService), loc);
            }
    }

    /**
     * Executed on the occurrence of a new day event, used to handle the lifecycle of the fire.
     *
     * @param DayNumber The current day number in the world.
     */
    @Override
    public void onNewDay(int DayNumber) {
        if(!isActive) return;
        worldService.Remove(this);
        isActive = false;
    }

    /**
     * Checks whether the fire is currently active.
     *
     * @return A boolean indicating if the fire is active.
     */
    public boolean isActive() { return isActive; }
}
