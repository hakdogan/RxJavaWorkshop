package jugistanbul.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@Entity
public class Passengers
{

    @Id
    private String id;
    private String speaker;
    private boolean sunnyWeather;
    private Long flightCostLimit;
    private Long hotelCostLimit;
    private String certainDays;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public boolean isSunnyWeather() {
        return sunnyWeather;
    }

    public void setSunnyWeather(boolean sunnyWeather) {
        this.sunnyWeather = sunnyWeather;
    }

    public Long getFlightCostLimit() {
        return flightCostLimit;
    }

    public void setFlightCostLimit(Long flightCostLimit) {
        this.flightCostLimit = flightCostLimit;
    }

    public Long getHotelCostLimit() {
        return hotelCostLimit;
    }

    public void setHotelCostLimit(Long hotelCostLimit) {
        this.hotelCostLimit = hotelCostLimit;
    }

    public String getCertainDays() {
        return certainDays;
    }

    public void setCertainDays(String certainDays) {
        this.certainDays = certainDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passengers that = (Passengers) o;
        return sunnyWeather == that.sunnyWeather &&
                id.equals(that.id) &&
                speaker.equals(that.speaker) &&
                flightCostLimit.equals(that.flightCostLimit) &&
                hotelCostLimit.equals(that.hotelCostLimit) &&
                certainDays.equals(that.certainDays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, speaker, sunnyWeather, flightCostLimit, hotelCostLimit, certainDays);
    }
}
