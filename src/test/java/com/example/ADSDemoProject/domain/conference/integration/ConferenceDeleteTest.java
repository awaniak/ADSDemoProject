package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.conference.repository.ConferencePriorityRepository;
import com.example.ADSDemoProject.conference.repository.ConferenceRepository;
import com.example.ADSDemoProject.conference.domain.Conference;
import com.example.ADSDemoProject.conference.domain.ConferencePriority;
import com.example.ADSDemoProject.conference.domain.ConferenceType;
import com.example.ADSDemoProject.conference.repository.ConferenceTypeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceDeleteTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    ConferencePriorityRepository conferencePriorityRepository;

    @Autowired
    ConferenceTypeRepository conferenceTypeRepository;

    private final String url = "/conference";

    private List<Conference> mockedConferences;

    private ConferencePriority PRIORITY_IMPORTANT;
    private ConferencePriority PRIORITY_NOT_IMPORTANT;
    private ConferenceType TYPE_PLANNING;
    private ConferenceType TYPE_WORKSHOP;
    private ConferenceType TYPE_RETROSPECTIVE;

    @Before
    public void setUp() {

        PRIORITY_IMPORTANT = new ConferencePriority("IMPORTANT");
        PRIORITY_NOT_IMPORTANT = new ConferencePriority("NOT_IMPORTANT");

        PRIORITY_IMPORTANT = conferencePriorityRepository.save(PRIORITY_IMPORTANT);
        PRIORITY_NOT_IMPORTANT = conferencePriorityRepository.save(PRIORITY_NOT_IMPORTANT);

        TYPE_PLANNING = new ConferenceType("PLANNING");
        TYPE_RETROSPECTIVE = new ConferenceType("RETROSPECTIVE");
        TYPE_WORKSHOP = new ConferenceType("WORKSHOP");

        TYPE_PLANNING = conferenceTypeRepository.save(TYPE_PLANNING);
        TYPE_RETROSPECTIVE = conferenceTypeRepository.save(TYPE_RETROSPECTIVE);
        TYPE_WORKSHOP = conferenceTypeRepository.save(TYPE_RETROSPECTIVE);


        Conference conference1 = new Conference("title", "desc", ZonedDateTime.now(), PRIORITY_IMPORTANT, TYPE_PLANNING);
        Conference conference2 = new Conference("title", "desc", ZonedDateTime.now().plusDays(5), PRIORITY_IMPORTANT, TYPE_RETROSPECTIVE);
        Conference conference3 = new Conference("title", "desc", ZonedDateTime.now().plusDays(10), PRIORITY_NOT_IMPORTANT, TYPE_WORKSHOP);
        Conference conference4 = new Conference("title", "desc", ZonedDateTime.now().minusDays(10), PRIORITY_NOT_IMPORTANT, TYPE_PLANNING);
        conference1 = conferenceRepository.save(conference1);
        conference2 = conferenceRepository.save(conference2);
        conference3 = conferenceRepository.save(conference3);
        conference4 = conferenceRepository.save(conference4);

        mockedConferences = Arrays.asList(conference1, conference2, conference3, conference4);
    }

    @After
    public void tearDown() {
        mockedConferences.forEach(conference -> conferenceRepository.delete(conference));
    }

    @Test
    public void should_delete_conference() throws Exception {
        long sizeBeforeDelete = conferenceRepository.count();
        mvc.perform(
                delete(url + "/" + mockedConferences.get(0).getId()))
                .andExpect(status().is2xxSuccessful()).andDo(print()).andReturn();
        assertThat(sizeBeforeDelete - 1, is(conferenceRepository.count()));
    }

    @Test
    public void should_not_delete_not_existing_conference() throws Exception {
        long sizeBeforeDelete = conferenceRepository.count();
        mvc.perform(
                delete(url + "/-1"))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();
        assertThat(sizeBeforeDelete, is(conferenceRepository.count()));
    }
}
