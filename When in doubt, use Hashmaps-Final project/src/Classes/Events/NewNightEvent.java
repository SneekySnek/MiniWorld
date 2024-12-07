package Classes.Events;

import Interfaces.INewDayListener;
import Interfaces.INewNightListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an event that is triggered when a new night begins.
 */
public class NewNightEvent {
    private List<INewNightListener> listeners = new ArrayList<>();

    /**
     * Adds a listener to the NewNightEvent.
     *
     * @param listener The listener to be added.
     */
    public void addListener(INewNightListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the NewNightEvent.
     *
     * @param listener The listener to be removed.
     */
    public void removeListener(INewNightListener listener) {
        listeners.remove(listener);
    }

    /**
     * Triggers the NewNightEvent, notifying registered listeners of a new night.
     *
     * @param nightNumber The number representing the current night.
     */
    public void triggerEvent(int nightNumber) {
        // Create a copy of the listeners to avoid ConcurrentModificationException
        List<INewNightListener> copyListeners = new ArrayList<>(listeners);

        // Notify each listener about the new night
        for (INewNightListener listener : copyListeners) {
            listener.onNewNight(nightNumber);
        }
    }
}
