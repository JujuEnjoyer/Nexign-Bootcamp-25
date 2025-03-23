package ru.tyufyakov.project.testprojectfornexign.taskTwo.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.CDRNoteRepo;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.SubscriberRepo;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.model.UdrReport;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UdrServiceTest {

    @Mock
    private SubscriberRepo subscriberRepo;

    @Mock
    private CDRNoteRepo cdrNoteRepo;

    @InjectMocks
    private UdrService udrService;

    @Test
    public void testUdrReportForMonth_Success() {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusMinutes(10);
        CDRNote cdrNote = new CDRNote();
        cdrNote.setStartCallTime(start);
        cdrNote.setEndCallTime(end);
        cdrNote.setCaller(79192526643L);
        cdrNote.setReceiver(79192526644L);
        cdrNote.setIncomingOrOutgoing("01");
        when(subscriberRepo.existsByPhoneNumber(anyLong())).thenReturn(true);
        when(cdrNoteRepo.findAllByCallerAndStartCallTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of(cdrNote));
        when(cdrNoteRepo.findAllByReceiverAndStartCallTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of(cdrNote));
        UdrReport report = udrService.udrReportForMonthOrAllPeriod(79192526643L, "2025-03");
        assertNotNull(report);
        assertEquals(79192526643L, report.getMsisdn());
    }

    @Test
    public void testUdrReportForMonth_SubscriberNotFound() {
        when(subscriberRepo.existsByPhoneNumber(anyLong())).thenReturn(false);

        assertThrows(NullPointerException.class, () ->
                udrService.udrReportForMonthOrAllPeriod(79192526643L, "2025-03"));
    }
}