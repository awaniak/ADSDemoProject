package com.example.ADSDemoProject.conference.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ConferencePriority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String priority;

    public ConferencePriority(String priority) {
        this.priority = priority;
    }
}
