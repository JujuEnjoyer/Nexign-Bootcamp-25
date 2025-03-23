package ru.tyufyakov.project.testprojectfornexign.taskOne.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.tyufyakov.project.testprojectfornexign.taskOne.service.CallGeneration;
import ru.tyufyakov.project.testprojectfornexign.taskOne.service.SubscriberGeneration;

/**
 * * Component responsible for initializing the database with subscribers and call data on application startup
 */

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SubscriberGeneration subscriberGeneration;
    private final CallGeneration callGeneration;
    /**
     * Executes the initialization process by generating subscribers and calls
     *
     * @throws Exception if an error occurs during data generation or persistence
     */
    @Override
    public void run(String... args) throws Exception {
        subscriberGeneration.pushToDb();

        callGeneration.callGeneration();
    }
}