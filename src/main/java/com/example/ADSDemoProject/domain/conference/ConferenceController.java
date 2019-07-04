package com.example.ADSDemoProject.domain.conference;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("conference")
public class ConferenceController {

    private ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @GetMapping()
    public List<Conference> getAllConferences(Sort sort) {
        return conferenceService.findAllConferenceWithCriteria(sort);
    }

    @PostMapping()
    public Conference addConference(@RequestBody Conference conference) {
        return conferenceService.save(conference);
    }

}
