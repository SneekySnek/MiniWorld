package Interfaces;

/**
 *   This is an abstraction for the fungi kingdom, and our best take on how they will
 *   take over the world!
 */
public interface Infection {

    /**
     * Sets the infection status to true.
     */
    void setInfected();

    /**
     * Checks the infection status.
     *
     * @return True if infected, false otherwise.
     */
    boolean getInfected();
}