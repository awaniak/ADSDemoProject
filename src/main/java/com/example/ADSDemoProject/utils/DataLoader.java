package com.example.ADSDemoProject.utils;

import com.example.ADSDemoProject.domain.conference.entity.Conference;
import com.example.ADSDemoProject.domain.conference.entity.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.entity.ConferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    ConferenceRepository conferenceRepository;

    @Override
    public void run(String... args) throws Exception {
        Conference conference1 = new Conference("title", "desc", ZonedDateTime.now(), ConferencePriority.IMPORTANT, ConferenceType.PLANNING);
        Conference conference2 = new Conference("title", "desc", ZonedDateTime.now().plusDays(5), ConferencePriority.IMPORTANT, ConferenceType.RETROSPECTIVE);
        Conference conference3 = new Conference("title", "desc", ZonedDateTime.now().plusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.WORKSHOP);
        Conference conference4 = new Conference("title", "desc", ZonedDateTime.now().minusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.PLANNING);
        conferenceRepository.save(conference1);
        conferenceRepository.save(conference2);
        conferenceRepository.save(conference3);
        conferenceRepository.save(conference4);
    }
}
