package jugistanbul.observable.operator;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

public class Event {
    private String eventName;
    public Event(final String eventName){
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
