package ru.tyufyakov.project.testprojectfornexign.taskThree.controller.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.tyufyakov.project.testprojectfornexign.taskThree.controller.CdrController;
import ru.tyufyakov.project.testprojectfornexign.taskThree.service.CdrReportService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CdrControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CdrReportService cdrReportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        CdrController controller = new CdrController(cdrReportService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGenerateCdrReport_Success() throws Exception {
        doNothing().when(cdrReportService).generateCdrReport(anyLong(), any(), any(), anyString());

        mockMvc.perform(post("/api/report/79192526643/generate-cdr")
                        .param("startDate", "2025-03-01T19:55:33+00:00")
                        .param("endDate", "2025-03-31T12:23:59+00:00"))
                .andExpect(status().isOk());

        verify(cdrReportService, times(1)).generateCdrReport(anyLong(), any(), any(), anyString());
    }

    @Test
    public void testGenerateCdrReport_InvalidDate() throws Exception {
        mockMvc.perform(post("/api/report/79192526643/generate-cdr")
                        .param("startDate", "invalid-date")
                        .param("endDate", "2025-03-31T12:23:59+00:00"))
                .andExpect(status().isBadRequest());
    }
}