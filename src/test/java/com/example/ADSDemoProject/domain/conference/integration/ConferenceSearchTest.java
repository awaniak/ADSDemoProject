package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.conference.ConferenceRepository;
import com.example.ADSDemoProject.conference.domain.Conference;
import com.example.ADSDemoProject.conference.domain.ConferencePriority;
import com.example.ADSDemoProject.conference.domain.ConferenceType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceSearchTest {

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
    public void should_return_all_conferences() throws Exception {
        MvcResult result = mvc.perform(
                get(url))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        List<Conference> conferenceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Conference>>() {
        });
        Assert.assertFalse(conferenceList.isEmpty());
        Assert.assertEquals(conferenceList.size(), conferenceRepository.count());
    }

    @Test
    public void should_return_all_conferences_sorted_by_properties_and_directions() throws Exception {
        List<String> properties = Arrays.asList("conferenceDateTime", "type", "priority");
        List<Sort.Direction> directions = Arrays.asList(Sort.Direction.ASC, Sort.Direction.DESC);
        for (String property : properties) {
            for (Sort.Direction direction: directions) {
                should_return_all_conferences_sorted_by_given_properties_and_order(property, direction);
            }
        }
    }

    @Test
    public void should_return_one_conference() throws Exception {
        Conference conferenceToReturn = mockedConferences.get(0);
        MvcResult result = mvc.perform(
                get(url + "/" + conferenceToReturn.getId()))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        Conference conference = objectMapper.readValue(result.getResponse().getContentAsString(), Conference.class);
        Assert.assertEquals(conference.getTitle(), conferenceToReturn.getTitle());
        Assert.assertEquals(conference.getId(), conferenceToReturn.getId());
        Assert.assertEquals(conference.getPriority(), conferenceToReturn.getPriority());
        Assert.assertEquals(conference.getType(), conferenceToReturn.getType());
    }

    @Test
    public void should_not_return_not_existing_conference() throws Exception {
        mvc.perform(
                get(url + "/-1"))
                .andExpect(status().isNotFound()).andDo(print());
    }

    private void should_return_all_conferences_sorted_by_given_properties_and_order(String property, Sort.Direction direction) throws Exception{
        MvcResult result = mvc.perform(
                get(url)
                        .param("sort", property + "," + direction.toString()))

                .andExpect(status().isOk()).andDo(print()).andReturn();
        List<Conference> conferenceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Conference>>() {
        });

        Assert.assertEquals(conferenceRepository.findAll(
                new Sort(direction, Arrays.asList(property))).stream().map(Conference::getId).collect(Collectors.toList()),
                conferenceList.stream().map(Conference::getId).collect(Collectors.toList()));
    }
}
