package com.example.ADSDemoProject.conference.domain.repository;

import com.example.ADSDemoProject.conference.domain.ConferencePriority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferencePriorityRepository extends JpaRepository<ConferencePriority, Long> {
}
