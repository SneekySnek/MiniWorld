package Interfaces;

/**
 * This interface represents a log listener that can react to logged events.
 */
public interface ILogListener {

    /**
     * Called when an event is logged.
     *
     * @param log The log message of the event.
     */
    void onEventLogged(String log);
}