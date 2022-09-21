package result;

import model.Event;

/**
 * Events Result Object of an Event Request
 */
public class EventsResult extends Result{
    Event[] data;
    boolean success;

    /**
     * Constructor of Events Result Object
     * @param data Event Array
     * @param success Boolean Success Status
     */
    public EventsResult(Event[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Returns the Array of Events from the Events Request
     * @return Event Array
     */
    public Event[] getData() {
        return data;
    }

    /**
     * Sets the Array of Events from the Events Request
     * @param data Event Array
     */
    public void setData(Event[] data) {
        this.data = data;
    }

    /**
     * Returns the Success Status of the Event Result
     * @return Boolean Success Status
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the Success Status of the Event Result
     * @param success Boolean Success Status
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
