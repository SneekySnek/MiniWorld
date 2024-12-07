package Interfaces;

import Enums.EntityStatus;
import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.NonBlocking;

/**
 * Represents a flora entity in the simulation.
 */
public interface IFlora extends NonBlocking, IEntity, DynamicDisplayInformationProvider {

    /**
     * Makes the flora entity consume something.
     */
    void consume();

    /**
     * Gets the status of the flora entity.
     *
     * @return The status of the flora entity.
     */
    EntityStatus getStatus();

    /**
     * Sets the status of the flora entity.
     *
     * @param status The status to set for the flora entity.
     */
    void setStatus(EntityStatus status);

    /**
     * Gets the growth rate of the flora entity.
     *
     * @return The growth rate of the flora entity.
     */
    int getGrowthRate();

    /**
     * Sets the growth rate of the flora entity.
     *
     * @param newGrowthRate The new growth rate to set for the flora entity.
     */
    void setGrowthRate(int newGrowthRate);
}
