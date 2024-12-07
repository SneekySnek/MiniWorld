package Interfaces;

/**
 * This interface represents a listener that reacts to the start of a new night.
 */
public interface INewNightListener {

    /**
     * Called when a new night starts.
     *
     * @param NightNumber The number of the new night.
     */
    void onNewNight(int NightNumber);
}