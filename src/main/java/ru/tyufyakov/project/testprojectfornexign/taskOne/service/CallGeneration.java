package ru.tyufyakov.project.testprojectfornexign.taskOne.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.Subscriber;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.CDRNoteRepo;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.SubscriberRepo;

import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

/**
 * Service responsible for generating random call data (CDR notes) and persisting it to the database
 */
@Service
@RequiredArgsConstructor
public class CallGeneration {

    private final SubscriberRepo subscriberRepo;
    private final CDRNoteRepo noteRepo;
    private final Random random = new Random();

    /**
     * Generates a random number of call records (between 500 and 999) for existing subscribers
     * and saves them to the database. Calls are generated starting from January 1, 2025, with random
     * durations and time shifts.
     *
     * @throws IllegalStateException if no subscribers are found in the database
     */

    public void callGeneration() {
        List<Subscriber> subscribers = subscriberRepo.findAll();
        if (subscribers.isEmpty()) {
            throw new IllegalStateException("No subscribers found in the database");
        }

        int callCount = random.nextInt(500) + 500; // От 500 до 999 звонков
        ZonedDateTime currentTime = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC); // Начало 2025 года

        for (int i = 0; i < callCount; i++) {
            Subscriber caller = getRandomSubscriber(subscribers);
            Subscriber receiver = getRandomSubscriber(subscribers);
            while (caller.getPhoneNumber() == receiver.getPhoneNumber()) {
                receiver = getRandomSubscriber(subscribers);
            }

            // Длительность звонка от 1 до 40 минут
            int duration = random.nextInt(2340) + 60;
            ZonedDateTime endCallTime = currentTime.plusSeconds(duration);
            String typeCall = random.nextBoolean() ? "01" : "02";

            if (isBusySubscriber(caller, currentTime, endCallTime) && isBusySubscriber(receiver, currentTime, endCallTime)) {
                CDRNote cdrNote = new CDRNote();
                cdrNote.setIncomingOrOutgoing(typeCall);
                cdrNote.setCaller(caller.getPhoneNumber());
                cdrNote.setReceiver(receiver.getPhoneNumber());
                cdrNote.setStartCallTime(currentTime);
                cdrNote.setEndCallTime(endCallTime);
                noteRepo.save(cdrNote);
            }

            int timeShift = random.nextInt(86400) + 60; // От 1 минуты до 1 часа
            currentTime = endCallTime.plusSeconds(timeShift);
        }
    }
    /**
     * Retrieves a random subscriber from the provided list.
     *
     * @param subscribers list of subscribers to choose from
     * @return a randomly selected {@link Subscriber}
     */
    public Subscriber getRandomSubscriber(List<Subscriber> subscribers) {
        return subscribers.get(random.nextInt(subscribers.size()));
    }
    /**
     * Checks if a subscriber is available (not busy) during the specified time period.
     *
     * @param subscriber the subscriber to check
     * @param startCallTime the start time of the call
     * @param endCallTime the end time of the call
     * @return {@code true} if the subscriber is not busy, {@code false} otherwise
     */
    public boolean isBusySubscriber(Subscriber subscriber, ZonedDateTime startCallTime, ZonedDateTime endCallTime) {
        return !noteRepo.existsByCallerAndStartCallTimeLessThanEqualAndEndCallTimeGreaterThanEqual(
                subscriber.getPhoneNumber(), endCallTime, startCallTime
        );
    }
}