package ru.tyufyakov.project.testprojectfornexign.taskThree.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.CDRNoteRepo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service for generating Call Detail Record (CDR) reports and saving them as CSV files
 */

@Service
@RequiredArgsConstructor
public class CdrReportService {
    private static final Logger logger = LoggerFactory.getLogger(CdrReportService.class);
    private static final String REPORTS_DIR = "reports";
    private final CDRNoteRepo cdrNoteRepo;
    /**
     * Asynchronously generates a CDR report for a specified phone number and date range
     * saving the result as a CSV file in the "reports" directory
     *
     * @param msisdn the phone number (MSISDN) for which the report is generated
     * @param start the start date and time of the report period
     * @param end the end date and time of the report period
     * @param uuid a unique identifier for the report
     * @throws IOException if an error occurs while writing the report file
     */
    @Async
    public void generateCdrReport(long msisdn, ZonedDateTime start, ZonedDateTime end, String uuid) throws IOException {
        try {
            File dir = new File(REPORTS_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            List<CDRNote> calls = new ArrayList<>();
            calls.addAll(cdrNoteRepo.findAllByCallerAndStartCallTimeBetween(msisdn, start, end));
            calls.addAll(cdrNoteRepo.findAllByReceiverAndStartCallTimeBetween(msisdn, start, end));
            calls.sort(Comparator.comparing(CDRNote::getStartCallTime));

            String fileName = String.format("%d_%s.csv", msisdn, uuid);
            File file = new File(REPORTS_DIR, fileName);

            try (FileWriter writer = new FileWriter(file)) {
                for (CDRNote call : calls) {
                    String line = String.format("%s,%d,%d,%s,%s%n",
                            call.getIncomingOrOutgoing(),
                            call.getCaller(),
                            call.getReceiver(),
                            call.getStartCallTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                            call.getEndCallTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
                    writer.write(line);
                }
                logger.info("CDR report generated successfully: {}", file.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to generate CDR report for msisdn: {}, uuid: {}", msisdn, uuid, e);
        }
        catch (Exception e ){
            System.out.println(e.getMessage());
        }
    }

}
