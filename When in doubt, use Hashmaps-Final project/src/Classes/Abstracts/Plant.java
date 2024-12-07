package Classes.Abstracts;

import Enums.EntityStatus;
import Interfaces.IFlora;
import Interfaces.INewDayListener;
import Interfaces.IWorldService;
import Services.WorldService;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

/**
 * An abstract class representing a plant entity in the simulation.
 */
public abstract class Plant extends Entity implements IFlora, INewDayListener {
    /**
     * The growth rate of the plant in days.
     */
    protected int growthRate = 1; // In days

    /**
     * The status of the plant (e.g., alive, dead).
     */
    protected EntityStatus status = EntityStatus.ALIVE;

    /**
     * Constructs a new plant entity.
     *
     * @param w The world service where the plant belongs.
     */
    public Plant(IWorldService w) {
        super(w);
        w.getNewDayEvent().addListener(this);
    }

    /**
     * Consumes the plant, marking it as dead and removing it from the world.
     */
    public void consume() {
        this.status = EntityStatus.DEAD;
        worldService.Remove(this);
    }

    /**
     * Acts on the plant's growth and checks its status.
     *
     * @param world The world where the plant exists.
     */
    @Override
    public void act(World world) {
        this.age++;
        if (status == EntityStatus.DEAD)
            world.delete(this);
    }

    /**
     * Gets the status of the plant (e.g., alive, dead).
     *
     * @return The status of the plant.
     */
    @Override
    public EntityStatus getStatus() {
        return this.status;
    }

    /**
     * Sets the status of the plant.
     *
     * @param status The status to set.
     */
    @Override
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    /**
     * Gets the growth rate of the plant.
     *
     * @return The growth rate of the plant in days.
     */
    public int getGrowthRate() {
        return this.growthRate;
    }

    /**
     * Sets the growth rate of the plant.
     *
     * @param newGrowthRate The new growth rate to set.
     */
    @Override
    public void setGrowthRate(int newGrowthRate) {
        this.growthRate = newGrowthRate;
    }
}
