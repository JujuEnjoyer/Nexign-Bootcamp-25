package ru.tyufyakov.project.testprojectfornexign.taskOne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.Subscriber;
@Repository
public interface SubscriberRepo extends JpaRepository<Subscriber, Long> {
    boolean existsByPhoneNumber(long phoneNumber);

}
