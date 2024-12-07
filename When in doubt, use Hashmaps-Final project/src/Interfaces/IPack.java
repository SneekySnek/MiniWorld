package Interfaces;

import Classes.Pack;

/**
 * Represents an interface for entities that can belong to a pack.
 */
public interface IPack {

    /**
     * Gets the pack that this entity belongs to.
     *
     * @return The pack that this entity belongs to.
     */
    Pack getPack();

    /**
     * Sets the pack that this entity belongs to.
     *
     * @param pack The pack to set for this entity.
     */
    void setPack(Pack pack);

    /**
     * Clears the pack association for this entity.
     */
    void clearPack();
}
