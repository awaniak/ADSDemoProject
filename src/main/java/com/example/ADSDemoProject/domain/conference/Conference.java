package com.example.ADSDemoProject.domain.conference;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
public class Conference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 3,max = 20)
    private String title;

    private String description;

    private ZonedDateTime conferenceDateTime;

    private ConferencePriority priority;

    private ConferenceType type;

    public Conference(@Size(min = 3, max = 20) String title, String description, ZonedDateTime conferenceDateTime, ConferencePriority priority, ConferenceType type) {
        this.title = title;
        this.description = description;
        this.conferenceDateTime = conferenceDateTime;
        this.priority = priority;
        this.type = type;
    }
}
