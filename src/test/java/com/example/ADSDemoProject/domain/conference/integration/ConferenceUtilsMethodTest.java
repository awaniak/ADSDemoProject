package com.example.ADSDemoProject.domain.conference.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConferenceUtilsMethodTest {

    @Autowired
    private MockMvc mvc;

    private final String url = "/conference";

    @Test
    public void should_return_all_possible_conference_types() throws Exception{
        String types = "[\"WORKSHOP\",\"PLANNING\",\"DAILY\",\"RETROSPECTIVE\"]";
        MvcResult result = mvc.perform(
                get(url + "/types"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        Assert.assertEquals(result.getResponse().getContentAsString(), types);
    }

    @Test
    public void should_return_all_possible_conference_priorities() throws Exception{
        String priorities = "[\"IMPORTANT\",\"NOT_IMPORTANT\"]";
        MvcResult result = mvc.perform(
                get(url + "/priorities"))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        Assert.assertEquals(result.getResponse().getContentAsString(), priorities);
    }
}
