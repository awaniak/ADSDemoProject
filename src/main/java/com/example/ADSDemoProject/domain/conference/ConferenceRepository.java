package com.example.ADSDemoProject.domain.conference;

import com.example.ADSDemoProject.domain.conference.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;


public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByConferenceDateTime(ZonedDateTime dateTime);
}
