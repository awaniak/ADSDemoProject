package com.example.ADSDemoProject.domain.conference.unit;

import com.example.ADSDemoProject.domain.conference.ConferenceRepository;
import com.example.ADSDemoProject.domain.conference.ConferenceService;
import com.example.ADSDemoProject.domain.conference.entity.Conference;
import com.example.ADSDemoProject.domain.conference.entity.ConferencePriority;
import com.example.ADSDemoProject.domain.conference.entity.ConferenceType;
import com.example.ADSDemoProject.utils.exception.InvalidRequestException;
import com.example.ADSDemoProject.utils.exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_invoke_repository_save_when_save_valid_object() {
        Conference conferenceToSave = new Conference("test", "desc", ZonedDateTime.now(), ConferencePriority.NOT_IMPORTANT, ConferenceType.PLANNING);
        when(conferenceRepository.save(Mockito.any())).thenReturn(conferenceToSave);
        conferenceService.save(conferenceToSave);
        verify(conferenceRepository, times(1)).save(Mockito.any());
    }

    @Test(expected = InvalidRequestException.class)
    public void should_throw_exception_when_save_conference_with_existing_date() {
        Conference conferenceToSave = new Conference("test", "desc", ZonedDateTime.now(), ConferencePriority.NOT_IMPORTANT, ConferenceType.PLANNING);
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