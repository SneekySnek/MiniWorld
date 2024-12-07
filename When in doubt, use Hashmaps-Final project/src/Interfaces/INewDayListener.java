package Interfaces;

/**
 * This interface represents a listener that reacts to the start of a new day.
 */
public interface INewDayListener {

    /**
     * Called when a new day starts.
     *
     * @param DayNumber The number of the new day.
     */
    void onNewDay(int DayNumber);
}