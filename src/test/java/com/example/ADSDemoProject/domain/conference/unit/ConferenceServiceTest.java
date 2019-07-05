package com.example.ADSDemoProject.domain.conference.unit;

import com.example.ADSDemoProject.conference.repository.ConferencePriorityRepository;
import com.example.ADSDemoProject.conference.repository.ConferenceRepository;
import com.example.ADSDemoProject.conference.ConferenceService;
import com.example.ADSDemoProject.conference.domain.Conference;
import com.example.ADSDemoProject.conference.domain.ConferencePriority;
import com.example.ADSDemoProject.conference.domain.ConferenceType;
import com.example.ADSDemoProject.conference.repository.ConferenceTypeRepository;
import com.example.ADSDemoProject.utils.exception.InvalidRequestException;
import com.example.ADSDemoProject.utils.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class ConferenceServiceTest {

    @Mock
    private Conference conferenceMock;

    @MockBean
    private ConferenceRepository conferenceRepository;

    @InjectMocks
    private ConferenceService conferenceService;

    private ConferencePriority PRIORITY_IMPORTANT;
    private ConferencePriority PRIORITY_NOT_IMPORTANT;
    private ConferenceType TYPE_PLANNING;
    private ConferenceType TYPE_WORKSHOP;
    private ConferenceType TYPE_RETROSPECTIVE;


    @Before
    public void setUp() {
        PRIORITY_IMPORTANT = new ConferencePriority("IMPORTANT");
        PRIORITY_NOT_IMPORTANT = new ConferencePriority("NOT_IMPORTANT");

        TYPE_PLANNING = new ConferenceType("PLANNING");
        TYPE_RETROSPECTIVE = new ConferenceType("RETROSPECTIVE");
        TYPE_WORKSHOP = new ConferenceType("WORKSHOP");

        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void should_invoke_repository_save_when_save_valid_object() {
        Conference conferenceToSave = new Conference("test", "desc", ZonedDateTime.now(), PRIORITY_NOT_IMPORTANT, TYPE_PLANNING);
        when(conferenceRepository.save(Mockito.any())).thenReturn(conferenceToSave);
        conferenceService.save(conferenceToSave);
        verify(conferenceRepository, times(1)).save(Mockito.any());
    }

    @Test(expected = InvalidRequestException.class)
    public void should_throw_exception_when_save_conference_with_existing_date() {
        Conference conferenceToSave = new Conference("test", "desc", ZonedDateTime.now(), PRIORITY_NOT_IMPORTANT, TYPE_PLANNING);
        when(conferenceRepository.findByConferenceDateTime(Mockito.any())).thenReturn(Arrays.asList(conferenceMock));
        conferenceService.save(conferenceToSave);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_throw_exception_when_remove_not_existing_conference() {
        when(conferenceRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        conferenceService.deleteById(1);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_throw_exception_when_edit_not_existing_conference() {
        when(conferenceRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        conferenceService.edit(conferenceMock);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void should_throw_exception_when_not_found_conference() {
        when(conferenceRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        conferenceService.findById(-1);
    }

    @Test
    public void should_invoke_repository_find_all_when_find_all_with_no_sorting() {
        when(conferenceRepository.findAll()).thenReturn(new ArrayList<>());
        conferenceService.findAllConferenceWithCriteria(null);
        verify(conferenceRepository, times(1)).findAll();
    }

    @Test
    public void should_invoke_repository_find_all_with_sorting_when_find_all_with_sorting() {
        when(conferenceRepository.findAll()).thenReturn(new ArrayList<>());
        Sort sort = new Sort(Sort.Direction.ASC, "title");
        conferenceService.findAllConferenceWithCriteria(sort);
        verify(conferenceRepository, times(1)).findAll(sort);
    }
}