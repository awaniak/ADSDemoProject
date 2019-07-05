package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.domain.conference.Conference;
import com.example.ADSDemoProject.domain.conference.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.ConferenceType;
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
    private ObjectMapper objectMapper;

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
    public void should_edit_conference() throws Exception {
        Conference conferenceToEdit = new Conference("editedTitle", "editedDesc", ZonedDateTime.now(), ConferencePriority.NOT_IMPORTANT, ConferenceType.RETROSPECTIVE);
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
        Conference conferenceToEdit = new Conference("editedTitle", "editedDesc", ZonedDateTime.now(), ConferencePriority.NOT_IMPORTANT, ConferenceType.RETROSPECTIVE);
        conferenceToEdit.setId(-1);
        mvc.perform(
                put(url).content(objectMapper.writeValueAsString(conferenceToEdit)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();
    }
}
