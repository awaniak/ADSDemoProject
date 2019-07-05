package com.example.ADSDemoProject.domain.conference.unit;

import com.example.ADSDemoProject.domain.conference.entity.Conference;
import com.example.ADSDemoProject.domain.conference.entity.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.entity.ConferenceType;
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

    Conference conference;
    Conference conferenceToCompare;

    private final String CONFERENCE_TITLE = "testTitle";
    private final String DESCRIPTION = "desc";
    private final ZonedDateTime DATE = ZonedDateTime.now();
    private final ConferencePriority PRIORITY = ConferencePriority.IMPORTANT;
    private final ConferenceType TYPE = ConferenceType.PLANNING;

    private final ZonedDateTime DATE_BEFORE = ZonedDateTime.now().minusDays(1);
    private final ConferencePriority LOW_PRIORITY = ConferencePriority.NOT_IMPORTANT;
    private final ConferenceType WORKSHOP_TYPE = ConferenceType.WORKSHOP;

    private final String EDITED_TITLE = "editedTitle";
    private final String EDITED_DESCRIPTION = "editeddesc";
    private final ZonedDateTime EDITED_DATE = ZonedDateTime.now().plusDays(5);
    private final ConferencePriority EDITED_PRIORITY = ConferencePriority.NOT_IMPORTANT;
    private final ConferenceType EDITED_TYPE = ConferenceType.RETROSPECTIVE;


    @Before
    public void setUp(){
        conference = new Conference(CONFERENCE_TITLE, DESCRIPTION, DATE, PRIORITY, TYPE);
        conferenceToCompare = new Conference(CONFERENCE_TITLE, DESCRIPTION, DATE_BEFORE, LOW_PRIORITY, WORKSHOP_TYPE);
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
        conferenceToEdit.setPriority(EDITED_PRIORITY);
        conferenceToEdit.setType(EDITED_TYPE);
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

        conference = conferenceRepository.save(new Conference(CONFERENCE_TITLE, DESCRIPTION, DATE, PRIORITY, TYPE));
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