package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.conference.repository.ConferencePriorityRepository;
import com.example.ADSDemoProject.conference.repository.ConferenceRepository;
import com.example.ADSDemoProject.conference.domain.Conference;
import com.example.ADSDemoProject.conference.domain.ConferencePriority;
import com.example.ADSDemoProject.conference.domain.ConferenceType;
import com.example.ADSDemoProject.conference.repository.ConferenceTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceEditTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    ConferencePriorityRepository conferencePriorityRepository;

    @Autowired
    ConferenceTypeRepository conferenceTypeRepository;


    @Autowired
    private ObjectMapper objectMapper;

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
    public void should_edit_conference() throws Exception {
        Conference conferenceToEdit = new Conference("editedTitle", "editedDesc", ZonedDateTime.now(), PRIORITY_NOT_IMPORTANT, TYPE_RETROSPECTIVE);
        conferenceToEdit.setId(mockedConferences.get(0).getId());
        long sizeBeforeEdit = conferenceRepository.count();
        MvcResult result = mvc.perform(
                put(url).content(objectMapper.writeValueAsString(conferenceToEdit)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andDo(print()).andReturn();
        Conference editedConference = objectMapper.readValue(result.getResponse().getContentAsString(), Conference.class);
        assertThat(sizeBeforeEdit, is(conferenceRepository.count()));
        Assert.assertEquals(conferenceToEdit.getTitle(), editedConference.getTitle());
        Assert.assertEquals(conferenceToEdit.getDescription(), editedConference.getDescription());
        Assert.assertEquals(conferenceToEdit.getPriority(), editedConference.getPriority());
        Assert.assertEquals(conferenceToEdit.getType(), editedConference.getType());
    }

    @Test
    public void should_not_edit_not_existing_conference() throws Exception {
        Conference conferenceToEdit = new Conference("editedTitle", "editedDesc", ZonedDateTime.now(), PRIORITY_NOT_IMPORTANT, TYPE_RETROSPECTIVE);
        conferenceToEdit.setId(-1);
        mvc.perform(
                put(url).content(objectMapper.writeValueAsString(conferenceToEdit)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();
    }
}
