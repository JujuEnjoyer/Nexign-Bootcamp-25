package ru.tyufyakov.project.testprojectfornexign.taskOne.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.Subscriber;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.SubscriberRepo;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Service responsible for generating random subscriber data and persisting it to the database
 */
@Service
@RequiredArgsConstructor
public class SubscriberGeneration {
    private final SubscriberRepo subscriberRepo;
    private static final String[] NAMES = {
            "Александр", "Михаил", "Максим", "Артем", "Дмитрий", "Иван", "Даниил", "Егор", "Андрей", "Илья",
            "Сергей", "Никита", "Кирилл", "Роман", "Владимир", "Анастасия", "Мария", "Дарья", "Анна", "Елизавета",
            "Полина", "Виктория", "Екатерина", "Софья", "Алиса", "Юлия", "Ольга", "Татьяна", "Наталья", "Ксения"
    };

    private static final String[] SURNAMES = {
            "Иванов", "Петров", "Сидоров", "Кузнецов", "Смирнов", "Попов", "Васильев", "Павлов", "Семенов", "Голубев",
            "Виноградов", "Богданов", "Воробьев", "Федоров", "Михайлов", "Беляев", "Тарасов", "Белов", "Комаров", "Орлов",
            "Козлов", "Николаев", "Морозов", "Зайцев", "Алексеев", "Лебедев", "Егоров", "Соколов", "Кудрявцев", "Степанов"
    };
    private Set<Subscriber> existingCombinations = new HashSet<>();

    private Random random = new Random();
    /**
     * Generates a random number of subscribers (between 10 and 20) and saves them to the database
     * Ensures uniqueness of subscriber combinations.
     */
    public void pushToDb() {
        int subscriberCount = random.nextInt(11) + 10; // От 10 до 20 абонентов
        for (int i = 0; i < subscriberCount; i++) {
            Subscriber subscriber = generateSubscriber();
            existingCombinations.add(subscriber);
        }
        subscriberRepo.saveAll(existingCombinations); // Сохраняем всех сразу
    }
    /**
     * Generates a unique subscriber with a random name, surname, and phone number
     *
     * @return a new {@link Subscriber} instance
     */
    public Subscriber generateSubscriber() {
        Subscriber subscriber;
        do {
            String name = NAMES[random.nextInt(NAMES.length)];
            String surname = SURNAMES[random.nextInt(SURNAMES.length)];
            long phoneNumber = generatePhoneNumber();
            subscriber = Subscriber.builder()
                    .name(name)
                    .surname(surname)
                    .phoneNumber(phoneNumber)
                    .build();
        } while (existingCombinations.contains(subscriber));
        return subscriber;
    }
    /**
     * Generates a random phone number starting with "79" followed by 9 random digits
     *
     * @return a unique phone number as a {@code long}
     */
    public long generatePhoneNumber() {
        long phoneNumber = 7 * (long) Math.pow(10, 10) + 9 * (long) Math.pow(10, 9) + random.nextInt(1000000000);
        return phoneNumber;
    }

}
