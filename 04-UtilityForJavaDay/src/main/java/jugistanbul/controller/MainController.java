package jugistanbul.controller;

import jugistanbul.entity.Speaker;
import jugistanbul.entity.SpeakerDTO;
import jugistanbul.service.DAOService;
import jugistanbul.smtp.SendEmail;
import org.apache.commons.lang3.tuple.Pair;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@RestController
public class MainController
{

    private final DAOService daoService;
    private final SendEmail sendEmail;
    private final ModelMapper mapper;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);
    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public MainController(final DAOService daoService, final SendEmail sendEmail,
                          final ModelMapper mapper){
        this.daoService = daoService;
        this.sendEmail = sendEmail;
        this.mapper = mapper;
    }

    @GetMapping(path = "/allSpeakers")
    public List<Speaker> allSpeakers(){
        return daoService.findAllSpeakers();
    }

    @PostMapping(path = "/addSpeaker")
    public Speaker addSpeaker(@RequestBody SpeakerDTO speakerDTO){
        final Speaker speaker = mapper.map(speakerDTO, Speaker.class);
        return daoService.addSpeaker(speaker);
    }

    @DeleteMapping(path = "/deleteSpeaker/{id}")
    public Integer deleteSpeaker(@PathVariable("id") Integer id){
        return daoService.deleteSpeaker(id);
    }

    @PostMapping(path = "/mail")
    public List<Speaker> sendEmailToNonApprovedSpeakers(){

        final List<Speaker> speakers = daoService.findAllSpeakers();

        final List<Pair<Speaker, Future<String>>> tasks = speakers
                .stream().map(speaker -> Pair.of(speaker, sendEmailAsync(speaker)))
                .collect(toList());

        final List<Speaker> failures = tasks.stream()
                .flatMap(pair -> {
                    try {
                        Future<String> future = pair.getRight();
                        future.get(1, TimeUnit.SECONDS);
                        return Stream.empty();
                    } catch (Exception e) {
                        Speaker speaker = pair.getLeft();
                        logger.warn("Failed to send {}", speaker.getMail(), e);
                        return Stream.of(speaker);
                    }
                })
                .collect(toList());

        return failures;
    }

    public Future<String> sendEmailAsync(final Speaker speaker){
        return pool.submit(() -> sendEmail.sendEmail(speaker));
    }
}
