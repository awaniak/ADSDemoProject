package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.domain.conference.Conference;
import com.example.ADSDemoProject.domain.conference.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.ConferenceType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceAddTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String url = "/conference";

    @Test
    public void should_add_conference() throws Exception {
        Conference conferenceToAdd = new Conference("hallo", "desc", ZonedDateTime.now(), ConferencePriority.NOT_IMPORTANT, ConferenceType.WORKSHOP);
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
        Conference conferenceToAdd = new Conference("hallo", "desc", ZonedDateTime.now(), ConferencePriority.NOT_IMPORTANT, ConferenceType.WORKSHOP);
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
