package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.domain.conference.Conference;
import com.example.ADSDemoProject.domain.conference.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.ConferenceType;
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

    private final String url = "/conference";

    private List<Conference> mockedConferences;


    @Before
    public void setUp() {
        Conference conference1 = new Conference("title", "desc", ZonedDateTime.now(), ConferencePriority.IMPORTANT, ConferenceType.PLANNING);
        Conference conference2 = new Conference("title", "desc", ZonedDateTime.now().plusDays(5), ConferencePriority.IMPORTANT, ConferenceType.RETROSPECTIVE);
        Conference conference3 = new Conference("title", "desc", ZonedDateTime.now().plusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.WORKSHOP);
        Conference conference4 = new Conference("title", "desc", ZonedDateTime.now().minusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.PLANNING);
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
