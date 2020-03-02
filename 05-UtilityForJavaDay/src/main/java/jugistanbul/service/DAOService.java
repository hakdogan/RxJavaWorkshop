package jugistanbul.service;

import jugistanbul.entity.Passengers;
import jugistanbul.entity.Speaker;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

public interface DAOService
{
    Speaker findSpeakerById(Integer id);
    Speaker updateSpeaker(Speaker speaker);
    List<Speaker> findAllSpeakers();
    List<Speaker> findAllChangeSpeakersByTime(LocalDateTime localDateTime);
    List<Passengers> findAllPassengers();
    Speaker addSpeaker(Speaker speaker);
    int deleteSpeaker(Integer id);
}
