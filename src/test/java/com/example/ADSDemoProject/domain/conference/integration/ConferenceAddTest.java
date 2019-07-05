package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.conference.repository.ConferencePriorityRepository;
import com.example.ADSDemoProject.conference.repository.ConferenceRepository;
import com.example.ADSDemoProject.conference.domain.Conference;
import com.example.ADSDemoProject.conference.domain.ConferencePriority;
import com.example.ADSDemoProject.conference.domain.ConferenceType;
import com.example.ADSDemoProject.conference.repository.ConferenceTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceAddTest {

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

    private ConferencePriority PRIORITY_IMPORTANT;
    private ConferencePriority PRIORITY_NOT_IMPORTANT;
    private ConferenceType TYPE_PLANNING;
    private ConferenceType TYPE_WORKSHOP;
    private ConferenceType TYPE_RETROSPECTIVE;

    @Before
    public void setUp(){
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

    }

    @Test
    public void should_add_conference() throws Exception {
        Conference conferenceToAdd = new Conference("hallo", "desc", ZonedDateTime.now(), PRIORITY_NOT_IMPORTANT, TYPE_WORKSHOP);
        long sizeBeforeAdd = conferenceRepository.count();
        MvcResult result = mvc.perform(
                post(url).content(objectMapper.writeValueAsString(conferenceToAdd)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andDo(print()).andReturn();
        Conference addedConference = objectMapper.readValue(result.getResponse().getContentAsString(), Conference.class);
        assertThat(sizeBeforeAdd + 1, is(conferenceRepository.count()));
        Assert.assertEquals(conferenceToAdd.getTitle(), addedConference.getTitle());
        Assert.assertEquals(conferenceToAdd.getDescription(), addedConference.getDescription());
        Assert.assertEquals(conferenceToAdd.getPriority(), addedConference.getPriority());
        Assert.assertEquals(conferenceToAdd.getType(), addedConference.getType());

        conferenceRepository.delete(addedConference);
    }

    @Test
    public void should_not_add_conference_with_same_date() throws Exception {
        Conference conferenceToAdd = new Conference("hallo", "desc", ZonedDateTime.now(), PRIORITY_NOT_IMPORTANT, TYPE_WORKSHOP);
        long sizeBeforeAdd = conferenceRepository.count();
        MvcResult result = mvc.perform(
                post(url).content(objectMapper.writeValueAsString(conferenceToAdd)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andDo(print()).andReturn();
        Conference addedConference = objectMapper.readValue(result.getResponse().getContentAsString(), Conference.class);
        mvc.perform(
                post(url).content(objectMapper.writeValueAsString(conferenceToAdd)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andDo(print()).andReturn();
        assertThat(sizeBeforeAdd + 1, is(conferenceRepository.count()));
        conferenceRepository.delete(addedConference);
    }
}
