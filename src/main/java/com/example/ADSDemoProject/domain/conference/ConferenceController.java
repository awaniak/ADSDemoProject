package com.example.ADSDemoProject.domain.conference;

import com.example.ADSDemoProject.domain.conference.entity.Conference;
import com.example.ADSDemoProject.domain.conference.entity.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.entity.ConferenceType;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/{conferenceId}")
    public Conference getConference(@PathVariable long conferenceId) {
        return conferenceService.findById(conferenceId);
    }

    @PostMapping
    public Conference addConference(@RequestBody Conference conference) {
        return conferenceService.save(conference);
    }

    @DeleteMapping("/{conferenceId}")
    public void deleteConference(@PathVariable long conferenceId) {
        conferenceService.deleteById(conferenceId);
    }

    @PutMapping
    public Conference editConference(@RequestBody Conference conference) {
        return conferenceService.edit(conference);
    }

    @GetMapping("/types")
    public ConferenceType[] getConferenceTypes() {
        return conferenceService.getConferenceTypes();
    }

    @GetMapping("/priorities")
    public ConferencePriority[] getConferencePriorities() {
        return conferenceService.getConferencePriorities();
    }

}
