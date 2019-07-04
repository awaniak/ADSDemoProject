package com.example.ADSDemoProject.domain.conference;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conference")
public class ConferenceController {

    private ConferenceService conferenceService;

    public ConferenceController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @GetMapping
    public List<Conference> getAllConferences(Sort sort) {
        return conferenceService.findAllConferenceWithCriteria(sort);
    }

    @PostMapping
    public Conference addConference(@RequestBody Conference conference) {
        return conferenceService.save(conference);
    }

    @DeleteMapping("/{conferenceId}")
    public void deleteConference(@PathVariable long conferenceId) {
        conferenceService.deleteById(conferenceId);
    }

}
