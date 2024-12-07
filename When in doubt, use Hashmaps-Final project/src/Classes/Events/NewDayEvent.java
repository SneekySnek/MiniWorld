package Classes.Events;

import Interfaces.INewDayListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that is triggered when a new day begins.
 */
public class NewDayEvent {
    private List<INewDayListener> listeners = new ArrayList<>();

    /**
     * Adds a listener to the NewDayEvent.
     *
     * @param listener The listener to be added.
     */
    public void addListener(INewDayListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the NewDayEvent.
     *
     * @param listener The listener to be removed.
     */
    public void removeListener(INewDayListener listener) {
        listeners.remove(listener);
    }

    /**
     * Triggers the NewDayEvent, notifying registered listeners of a new day.
     *
     * @param dayNumber The number representing the current day.
     */
    public void triggerEvent(int dayNumber) {
        // Create a copy of the listeners to avoid ConcurrentModificationException
        List<INewDayListener> copyListeners = new ArrayList<>(listeners);

        // Notify each listener about the new day
        for (INewDayListener listener : copyListeners) {
            listener.onNewDay(dayNumber);
        }
    }
}
