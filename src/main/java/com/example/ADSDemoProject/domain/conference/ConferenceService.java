package com.example.ADSDemoProject.domain.conference;


import com.example.ADSDemoProject.utils.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConferenceService {

    private ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public Conference save(Conference conference) {
        return conferenceRepository.save(conference);
    }

    public Conference findById(long id) {
        return conferenceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public void deleteById(long id){
        Conference conferenceToDelete = conferenceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        conferenceRepository.delete(conferenceToDelete);
    }

    public Conference edit(Conference conference){
        Conference conferenceToEdit = conferenceRepository.findById(conference.getId()).orElseThrow(ResourceNotFoundException::new);
        return conferenceRepository.save(conferenceToEdit);
    }

    public List<Conference> findAllConferenceWithCriteria(List<Sort.Order> orders) {
        return conferenceRepository.findAll(Sort.by(orders));
    }

}
