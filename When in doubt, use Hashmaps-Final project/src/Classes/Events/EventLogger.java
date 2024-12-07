package Classes.Events;

import Interfaces.ILogListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A class responsible for logging events and notifying registered listeners.
 */
public class EventLogger {
    private List<ILogListener> listeners = new ArrayList<>();

    /**
     * Adds a listener to the event logger.
     *
     * @param listener The listener to be added.
     */
    public void addListener(ILogListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the event logger.
     *
     * @param listener The listener to be removed.
     */
    public void removeListener(ILogListener listener) {
        listeners.remove(listener);
    }

    /**
     * Logs an event message and notifies registered listeners.
     *
     * @param log The event log message to be logged.
     */
    public void logEvent(String log) {
        // Print the log message to the console
        System.out.println(log);

        // Create a copy of the listeners to avoid ConcurrentModificationException
        List<ILogListener> copyListeners = new ArrayList<>(listeners);

        // Notify each listener about the logged event
        for (ILogListener listener : copyListeners) {
            listener.onEventLogged(log);
        }
    }
}