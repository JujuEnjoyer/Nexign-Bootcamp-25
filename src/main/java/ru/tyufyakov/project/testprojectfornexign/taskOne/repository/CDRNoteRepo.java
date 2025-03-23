package ru.tyufyakov.project.testprojectfornexign.taskOne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface CDRNoteRepo extends JpaRepository<CDRNote, Long> {
    boolean existsByCallerAndStartCallTimeLessThanEqualAndEndCallTimeGreaterThanEqual(long subscriberNumber, ZonedDateTime endTime, ZonedDateTime startTime);
    List<CDRNote> findAllByCaller(long phonNumber);
    List<CDRNote> findAllByReceiver(long phonNumber);
    List<CDRNote> findAllByCallerAndStartCallTimeBetween(
            long phoneNumber, ZonedDateTime startDateTime, ZonedDateTime endDateTime);

    List<CDRNote> findAllByReceiverAndStartCallTimeBetween(
            long phoneNumber, ZonedDateTime startDateTime, ZonedDateTime endDateTime);

    List<CDRNote> findAllByStartCallTimeBetween(ZonedDateTime startDateTime, ZonedDateTime endDateTime);

}
