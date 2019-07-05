package com.example.ADSDemoProject.conference.repository;

import com.example.ADSDemoProject.conference.domain.ConferenceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceTypeRepository extends JpaRepository<ConferenceType, Long> {

}
