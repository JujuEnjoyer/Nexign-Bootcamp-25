package ru.tyufyakov.project.testprojectfornexign.taskOne.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.Subscriber;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.SubscriberRepo;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscriberGenerationTest {

    @Mock
    private SubscriberRepo subscriberRepo;

    @InjectMocks
    private SubscriberGeneration subscriberGeneration;

    @Test
    public void testPushToDb() {
        when(subscriberRepo.saveAll(anySet())).thenReturn(Collections.emptyList());
        subscriberGeneration.pushToDb();
        verify(subscriberRepo, times(1)).saveAll(anySet());
    }

    @Test
    public void testGenerateSubscriber() {
        Subscriber subscriber = subscriberGeneration.generateSubscriber();

        assertNotNull(subscriber);
        assertNotNull(subscriber.getName());
        assertNotNull(subscriber.getSurname());
        assertTrue(subscriber.getPhoneNumber() > 0);
    }
}