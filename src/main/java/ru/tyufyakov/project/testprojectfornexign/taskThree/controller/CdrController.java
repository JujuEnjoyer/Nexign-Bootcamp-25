package ru.tyufyakov.project.testprojectfornexign.taskThree.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tyufyakov.project.testprojectfornexign.taskThree.model.CdrReportResponse;
import ru.tyufyakov.project.testprojectfornexign.taskThree.service.CdrReportService;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

/**
 * REST controller for handling Call Detail Record (CDR) report generation requests
 */

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class CdrController {
    /**
     * Initiates the generation of a CDR report for a specified phone number within a given date range
     *
     * @param msisdn the phone number (MSISDN) for which the report is generated
     * @param startDate the start date and time of the report period in ISO 8601 format (e.g., "2025-03-01T19:55:33+00:00")
     * @param endDate the end date and time of the report period in ISO 8601 format (e.g., "2025-03-31T12:23:59+00:00")
     * @return a {@link ResponseEntity} containing a {@link CdrReportResponse} with the report UUID and status message
     * @throws DateTimeParseException if the provided date strings are not in the correct ISO 8601 format
     * @throws IllegalArgumentException if the start date is after the end date
     */
    private final CdrReportService cdrReportService;
    @PostMapping("/{msisdn}/generate-cdr")
    public ResponseEntity<CdrReportResponse> generateCdrReport(
            @PathVariable long msisdn,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            ZonedDateTime start = ZonedDateTime.parse(startDate, formatter);
            ZonedDateTime end = ZonedDateTime.parse(endDate, formatter);

            if (start.isAfter(end)) {
                return ResponseEntity.badRequest()
                        .body(new CdrReportResponse(null, "Start date must be before end date"));
            }

            String uuid = UUID.randomUUID().toString();
            cdrReportService.generateCdrReport(msisdn, start, end, uuid);

            return ResponseEntity.ok(new CdrReportResponse(uuid, "CDR report generation started"));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body(new CdrReportResponse(null, "Invalid date format: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new CdrReportResponse(null, "Error initiating CDR report generation: " + e.getMessage()));
        }
    }
}
/*
SELECT * FROM CDRNOTE
    WHERE (CALLER = 79260541274 OR RECEIVER = 79260541274)
    AND END_CALL_TIME   >= '2025-03-01 20:23:59+00'
    AND START_CALL_TIME <= '2025-03-31 19:55:33+00'
 */
