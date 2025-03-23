package ru.tyufyakov.project.testprojectfornexign.taskTwo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.model.UdrReport;
import ru.tyufyakov.project.testprojectfornexign.taskThree.service.CdrReportService;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.service.UdrService;
import java.util.List;

/**
 * REST controller for handling User Detail Record (UDR) report requests.
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UdrController {

    private final UdrService udrService;
    private final CdrReportService cdrReportService;
    /**
     * Retrieves a UDR report for a specific phone number, optionally filtered by month
     *
     * @param msisdn the phone number (MSISDN) for which the report is requested
     * @param month the month in "YYYY-MM" format (optional; if null, returns total for all time)
     * @return a {@link ResponseEntity} containing the {@link UdrReport} or an error response
     */
    @GetMapping("/{msisdn}")
    public ResponseEntity<UdrReport> getUdrByMsisdn(
           @PathVariable long msisdn,
           @RequestParam(required = false) String month) {
       try {
           UdrReport report = udrService.udrReportForMonthOrAllPeriod(msisdn, month);
           return ResponseEntity.ok(report);
       } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().body(null);
       }
    }
    /**
     * Retrieves UDR reports for all subscribers, optionally filtered by month
     *
     * @param month the month in "YYYY-MM" format (optional; if null, defaults to current month)
     * @return a {@link ResponseEntity} containing a list of {@link UdrReport} objects or an error response
     */
    @GetMapping("/all")
    public ResponseEntity<List<UdrReport>> getAllUdr(
            @RequestParam(required = false) String month) {
        try {
            List<UdrReport> reports = udrService.udrReportForAll(month);
            return ResponseEntity.ok(reports);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}





