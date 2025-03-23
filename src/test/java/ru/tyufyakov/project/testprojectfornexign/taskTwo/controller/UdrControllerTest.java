package ru.tyufyakov.project.testprojectfornexign.taskTwo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.model.UdrReport;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.service.UdrService;
import ru.tyufyakov.project.testprojectfornexign.taskThree.service.CdrReportService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UdrControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UdrService udrService;

    @Mock
    private CdrReportService cdrReportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UdrController controller = new UdrController(udrService, cdrReportService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetUdrByMsisdn_Success() throws Exception {

        when(udrService.udrReportForMonthOrAllPeriod(anyLong(), anyString())).thenReturn(new UdrReport());

        mockMvc.perform(get("/api/79192526643").param("month", "2025-03"))
                .andExpect(status().isOk());

        verify(udrService, times(1)).udrReportForMonthOrAllPeriod(79192526643L, "2025-03");
    }

    @Test
    public void testGetUdrByMsisdn_InvalidMonth() throws Exception {
        when(udrService.udrReportForMonthOrAllPeriod(anyLong(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid month format"));

        mockMvc.perform(get("/api/79192526643").param("month", "invalid"))
                .andExpect(status().isBadRequest());
    }
}