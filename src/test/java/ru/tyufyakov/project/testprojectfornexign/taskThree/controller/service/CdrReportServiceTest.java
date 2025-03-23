package ru.tyufyakov.project.testprojectfornexign.taskThree.controller.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.CDRNoteRepo;
import ru.tyufyakov.project.testprojectfornexign.taskThree.service.CdrReportService;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

public class CdrReportServiceTest {

    @Mock
    private CDRNoteRepo cdrNoteRepo;

    @InjectMocks
    private CdrReportService cdrReportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateCdrReport_Success() throws Exception {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusDays(1);
        CDRNote call = new CDRNote();
        when(cdrNoteRepo.findAllByCallerAndStartCallTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of(call));
        when(cdrNoteRepo.findAllByReceiverAndStartCallTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of(call));

        cdrReportService.generateCdrReport(79192526643L, start, end, "test-uuid");

        verify(cdrNoteRepo, times(1)).findAllByCallerAndStartCallTimeBetween(79192526643L, start, end);
        verify(cdrNoteRepo, times(1)).findAllByReceiverAndStartCallTimeBetween(79192526643L, start, end);
    }

    @Test
    public void testGenerateCdrReport_NoCalls() throws Exception {
        ZonedDateTime start = ZonedDateTime.now();
        ZonedDateTime end = start.plusDays(1);
        when(cdrNoteRepo.findAllByCallerAndStartCallTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of());
        when(cdrNoteRepo.findAllByReceiverAndStartCallTimeBetween(anyLong(), any(), any()))
                .thenReturn(List.of());

        cdrReportService.generateCdrReport(79192526643L, start, end, "test-uuid");

        verify(cdrNoteRepo, times(1)).findAllByCallerAndStartCallTimeBetween(79192526643L, start, end);
        verify(cdrNoteRepo, times(1)).findAllByReceiverAndStartCallTimeBetween(79192526643L, start, end);
    }
}