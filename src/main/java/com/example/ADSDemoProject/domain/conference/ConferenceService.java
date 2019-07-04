package com.example.ADSDemoProject.domain.conference;


import com.example.ADSDemoProject.utils.exception.InvalidRequestException;
import com.example.ADSDemoProject.utils.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
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
        if (!conferenceRepository.findByConferenceDateTime(conference.getConferenceDateTime()).isEmpty()) {
            throw new InvalidRequestException("Conference with given time already exists");
        }
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
        conferenceRepository.findById(conference.getId()).orElseThrow(ResourceNotFoundException::new);
        return conferenceRepository.save(conference);
    }

    public List<Conference> findAllConferenceWithCriteria(Sort sort) {
        if (sort == null){
            return conferenceRepository.findAll();
        }else {
            return conferenceRepository.findAll(sort);
        }
    }

}
