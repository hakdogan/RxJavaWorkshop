package jugistanbul.observable.operator;

import io.reactivex.Observable;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

public class Speaker {

    private String firstName;
    private String lastName;
    private String talkTilte;
    private String room;
    private String eventName;

    public Speaker(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private Speaker(String firstName, String lastName, String talkTilte, String room, String eventName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.talkTilte = talkTilte;
        this.room = room;
        this.eventName = eventName;
    }

    public static Observable<Speaker> speakerObservable(final Event event){
        return Observable.just(new Speaker("Aiko", "Klostermann", "Artificial Intelligence? - more like Artificial Stupidity!",
                "Marmara B", event.getEventName()));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTalkTilte() {
        return talkTilte;
    }

    public void setTalkTilte(String talkTilte) {
        this.talkTilte = talkTilte;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}