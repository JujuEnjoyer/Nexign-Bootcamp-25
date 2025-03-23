package ru.tyufyakov.project.testprojectfornexign.taskOne.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.Subscriber;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.CDRNoteRepo;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.SubscriberRepo;

import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CallGenerationTest {

    @Mock
    private SubscriberRepo subscriberRepo;

    @Mock
    private CDRNoteRepo noteRepo;

    @InjectMocks
    private CallGeneration callGeneration;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCallGeneration_NoSubscribers() {
        when(subscriberRepo.findAll()).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> callGeneration.callGeneration());
    }

    @Test
    public void testCallGeneration_Success() {
        Subscriber subscriber1 = new Subscriber(1L, "Name1", "Surname1", 79192526643L);
        Subscriber subscriber2 = new Subscriber(2L, "Name2", "Surname2", 79192526644L);
        when(subscriberRepo.findAll()).thenReturn(List.of(subscriber1, subscriber2));
        when(noteRepo.existsByCallerAndStartCallTimeLessThanEqualAndEndCallTimeGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(false);

        callGeneration.callGeneration();

        verify(noteRepo, atLeastOnce()).save(any(CDRNote.class));
    }

    @Test
    public void testGetRandomSubscriber() {
        List<Subscriber> subscribers = List.of(new Subscriber(), new Subscriber());
        Subscriber result = callGeneration.getRandomSubscriber(subscribers);
        assertNotNull(result);
        assertTrue(subscribers.contains(result));
    }

    @Test
    public void testIsBusySubscriber_NotBusy() {
        Subscriber subscriber = new Subscriber(1L, "Name", "Surname", 79192526643L);
        ZonedDateTime start = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime end = start.plusMinutes(10);
        when(noteRepo.existsByCallerAndStartCallTimeLessThanEqualAndEndCallTimeGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(false);
        boolean result = callGeneration.isBusySubscriber(subscriber, start, end);
        assertTrue(result);
    }

    @Test
    public void testIsBusySubscriber_Busy() {
        Subscriber subscriber = new Subscriber(1L, "Name", "Surname", 79192526643L);
        ZonedDateTime start = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        ZonedDateTime end = start.plusMinutes(10);
        when(noteRepo.existsByCallerAndStartCallTimeLessThanEqualAndEndCallTimeGreaterThanEqual(anyLong(), any(), any()))
                .thenReturn(true);
        boolean result = callGeneration.isBusySubscriber(subscriber, start, end);
        assertFalse(result);
    }
}