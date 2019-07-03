package com.example.ADSDemoProject.domain.conference;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConferenceRepository conferenceRepository;


    @Before
    public void setUp() throws Exception {
        Conference conference1 = new Conference("title", "desc", ZonedDateTime.now(), ConferencePriority.IMPORTANT, ConferenceType.PLANNING);
        Conference conference2 = new Conference("title", "desc", ZonedDateTime.now(), ConferencePriority.IMPORTANT, ConferenceType.RETROSPECTIVE);
        Conference conference3 = new Conference("title", "desc", ZonedDateTime.now().plusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.WORKSHOP);
        Conference conference4 = new Conference("title", "desc", ZonedDateTime.now().minusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.PLANNING);

    }
}
