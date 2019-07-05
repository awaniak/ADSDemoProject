package com.example.ADSDemoProject.domain.conference.unit;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


@DataJpaTest
@RunWith(SpringRunner.class)
public class ConferenceRepositoryTest {

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    ConferencePriorityRepository conferencePriorityRepository;

    @Autowired
    ConferenceTypeRepository conferenceTypeRepository;

    Conference conference;
    Conference conferenceToCompare;

    private String CONFERENCE_TITLE = "testTitle";
    private String DESCRIPTION = "desc";
    private ZonedDateTime DATE = ZonedDateTime.now();

    private ConferencePriority PRIORITY_IMPORTANT;
    private ConferencePriority PRIORITY_NOT_IMPORTANT;
    private ConferenceType TYPE_PLANNING;
    private ConferenceType TYPE_WORKSHOP;
    private ConferenceType TYPE_RETROSPECTIVE;

    private ZonedDateTime DATE_BEFORE = ZonedDateTime.now().minusDays(1);
    private String EDITED_TITLE = "editedTitle";
    private String EDITED_DESCRIPTION = "editeddesc";
    private ZonedDateTime EDITED_DATE = ZonedDateTime.now().plusDays(5);


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


        conference = new Conference(CONFERENCE_TITLE, DESCRIPTION, DATE, PRIORITY_IMPORTANT, TYPE_PLANNING);
        conferenceToCompare = new Conference(CONFERENCE_TITLE, DESCRIPTION, DATE_BEFORE, PRIORITY_NOT_IMPORTANT, TYPE_WORKSHOP);
        conferenceRepository.save(conference);
        conferenceRepository.save(conferenceToCompare);



    }

    @After
    public void tearDown() throws Exception {
        conferenceRepository.delete(conference);
        conferenceRepository.delete(conferenceToCompare);
    }

    @Test
    public void should_create_and_fetch_conference_success() {
        Optional<Conference> optional = conferenceRepository.findById(conference.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), is(conference));
    }

    @Test
    public void should_not_find_not_existing_conference() {
        Optional<Conference> optional = conferenceRepository.findById(-1L);
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void should_find_all_conferences() {
        List<Conference> conferences = conferenceRepository.findAll();
        assertThat(conferences.size(), is(2));
    }

    @Test
    public void should_edit_exist_conference() {
        Optional<Conference> optional = conferenceRepository.findById(conference.getId());
        Conference conferenceToEdit = optional.get();
        conferenceToEdit.setTitle(EDITED_TITLE);
        conferenceToEdit.setConferenceDateTime(EDITED_DATE);
        conferenceToEdit.setDescription(EDITED_DESCRIPTION);
        conferenceToEdit.setPriority(PRIORITY_NOT_IMPORTANT);
        conferenceToEdit.setType(TYPE_RETROSPECTIVE);
        Conference editedConference = conferenceRepository.save(conferenceToEdit);

        Optional<Conference> optionalEditedConference = conferenceRepository.findById(editedConference.getId());

        assertThat(optionalEditedConference.isPresent(), is(true));
        assertThat(optionalEditedConference.get(), is(editedConference));
        assertThat(optionalEditedConference.get().getTitle(), is(editedConference.getTitle()));
        assertThat(optionalEditedConference.get().getId(), is(editedConference.getId()));

        conference = conferenceRepository.save(conference);

    }

    @Test
    public void should_remove_conference() {
        Conference conferenceToRemove = conferenceRepository.findById(conference.getId()).get();
        conferenceRepository.delete(conferenceToRemove);
        Optional<Conference> optionalConference = conferenceRepository.findById(conference.getId());
        assertThat(optionalConference.isPresent(), is(false));

        conference = conferenceRepository.save(new Conference(CONFERENCE_TITLE, DESCRIPTION, DATE, PRIORITY_IMPORTANT, TYPE_PLANNING));
    }

    @Test
    public void should_fetch_all_sorted_by_type_asc() {
        List<Sort.Order> orders = Arrays.asList(new Sort.Order(Sort.Direction.ASC, ConferenceProperties.type.toString()));
        List<Conference> conferences = conferenceRepository.findAll(Sort.by(orders));
        assertThat(conferences.size(), is(2));
        assertThat(conferences.get(0), is(conferenceToCompare));
        assertThat(conferences.get(1), is(conference));
    }

    @Test
    public void should_fetch_all_sorted_by_type_desc() {
        List<Sort.Order> orders = Arrays.asList(new Sort.Order(Sort.Direction.DESC, ConferenceProperties.type.toString()));
        List<Conference> conferences = conferenceRepository.findAll(Sort.by(orders));
        assertThat(conferences.size(), is(2));
        assertThat(conferences.get(0), is(conference));
        assertThat(conferences.get(1), is(conferenceToCompare));
    }

    private enum ConferenceProperties {
        title, description, conferenceDateTime, priority, type
    }

}