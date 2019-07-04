package com.example.ADSDemoProject.domain.conference.integration;


import com.example.ADSDemoProject.domain.conference.Conference;
import com.example.ADSDemoProject.domain.conference.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.ConferenceType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    final String url = "/conference";


    @Before
    public void setUp() {
        Conference conference1 = new Conference("title", "desc", ZonedDateTime.now(), ConferencePriority.IMPORTANT, ConferenceType.PLANNING);
        Conference conference2 = new Conference("title", "desc", ZonedDateTime.now().plusDays(5), ConferencePriority.IMPORTANT, ConferenceType.RETROSPECTIVE);
        Conference conference3 = new Conference("title", "desc", ZonedDateTime.now().plusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.WORKSHOP);
        Conference conference4 = new Conference("title", "desc", ZonedDateTime.now().minusDays(10), ConferencePriority.NOT_IMPORTANT, ConferenceType.PLANNING);
        conferenceRepository.save(conference1);
        conferenceRepository.save(conference2);
        conferenceRepository.save(conference3);
        conferenceRepository.save(conference4);
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
    public void should_return_all_conferences_sorted_by_date_desc() throws Exception {
        MvcResult result = mvc.perform(
                get(url)
                        .param("sort", "conferenceDateTime,desc"))

                .andExpect(status().isOk()).andDo(print()).andReturn();
        List<Conference> conferenceList = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Conference>>() {
        });

        Assert.assertEquals(conferenceRepository.findAll(
                new Sort(Sort.Direction.DESC, Arrays.asList("conferenceDateTime"))).stream().map(Conference::getId).collect(Collectors.toList()),
                conferenceList.stream().map(Conference::getId).collect(Collectors.toList()));
    }

    @Test
    public void should_return_all_conferences_sorted_by_type_asc() throws Exception {
        List<String> properties = Arrays.asList("conferenceDateTime", "type", "priority");
        List<Sort.Direction> directions = Arrays.asList(Sort.Direction.ASC, Sort.Direction.DESC);
        for (String property : properties) {
            for (Sort.Direction direction: directions) {
                should_return_all_conferences_sorted_by_given_properties_and_order(property, direction);
            }
        }
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
