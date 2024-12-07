package Interfaces;

import Enums.Age;
import Enums.EntityStatus;
import Enums.Gender;

/**
 * Represents a fauna entity in the simulation.
 */
public interface IFauna extends IEntity {

    /**
     * Gets the age of the fauna entity.
     *
     * @return The age of the fauna entity in ticks.
     */
    int getAge();

    /**
     * Makes the fauna entity go to sleep.
     */
    void sleep();

    /**
     * Kills the fauna entity.
     */
    void kill();

    /**
     * Makes the fauna entity consume something.
     */
    void consume();

    /**
     * Infects the fauna entity.
     */
    void infect();

    /**
     * Adds energy to the fauna entity.
     *
     * @param amount The amount of energy to add.
     */
    void addEnergy(int amount);

    /**
     * Gets the energy level of the fauna entity.
     *
     * @return The energy level of the fauna entity.
     */
    int getEnergy();

    /**
     * Adds health to the fauna entity.
     *
     * @param amount The amount of health to add.
     */
    void addHealth(int amount);

    /**
     * Gets the status of the fauna entity.
     *
     * @return The status of the fauna entity.
     */
    EntityStatus getStatus();

    /**
     * Gets the age status of the fauna entity.
     *
     * @return The age status of the fauna entity.
     */
    Age getAgeStatus();

    /**
     * Gets the gender of the fauna entity.
     *
     * @return The gender of the fauna entity.
     */
    Gender getGender();

    /**
     * Gets the health of the fauna entity.
     *
     * @return The health of the fauna entity.
     */
    int getHealth();

    /**
     * Sets the gender of the fauna entity (mostly for testing).
     *
     * @param gender The gender to set for the fauna entity.
     */
    void setGender(Gender gender);

    /**
     * Sets the status of the fauna entity.
     *
     * @param status The status to set for the fauna entity.
     */
    void setStatus(EntityStatus status);
}
