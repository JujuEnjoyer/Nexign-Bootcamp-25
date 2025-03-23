package ru.tyufyakov.project.testprojectfornexign.taskTwo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.CDRNote;
import ru.tyufyakov.project.testprojectfornexign.taskOne.entity.Subscriber;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.CDRNoteRepo;
import ru.tyufyakov.project.testprojectfornexign.taskOne.repository.SubscriberRepo;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.model.DurationCall;
import ru.tyufyakov.project.testprojectfornexign.taskTwo.model.UdrReport;

import java.time.Duration;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating User Detail Record (UDR) reports based on call data
 */

@Service
@RequiredArgsConstructor
public class UdrService {

    private final SubscriberRepo subscriberRepo;
    private final CDRNoteRepo cdrNoteRepo;
    /**
     * Generates a UDR report for a specific phone number, either for a given month or for all time
     *
     * @param msisdn the phone number (MSISDN) for which the report is generated
     * @param month the month in "YYYY-MM" format (optional; if null, returns total for all time)
     * @return a {@link UdrReport} containing total incoming and outgoing call durations
     * @throws NullPointerException if the subscriber or CDR notes are not found
     * @throws IllegalArgumentException if the month format is invalid
     */
    public UdrReport udrReportForMonthOrAllPeriod(long msisdn, String month){
        try {
            UdrReport report = new UdrReport();
            if (subscriberRepo.existsByPhoneNumber(msisdn)) {
                if (month != null && !month.isEmpty()) {
                    List<ZonedDateTime> period = parseMonth(month);
                    ZonedDateTime start = period.get(0);
                    ZonedDateTime end = period.get(1);

                    List<CDRNote> outgoing = cdrNoteRepo.findAllByCallerAndStartCallTimeBetween(msisdn, start, end);
                    List<CDRNote> incoming = cdrNoteRepo.findAllByReceiverAndStartCallTimeBetween(msisdn, start, end);
                    report.setMsisdn(msisdn);
                    report.setIncomingCall(formatDuration(totalDuration(incoming)));
                    report.setOutgoingCall(formatDuration(totalDuration(outgoing)));
                    return report;
                } else {
                    List<CDRNote> callsIncoming = cdrNoteRepo.findAllByCaller(msisdn);
                    List<CDRNote> callsOutgoing = cdrNoteRepo.findAllByReceiver(msisdn);
                    report.setMsisdn(msisdn);
                    report.setIncomingCall(formatDuration(totalDuration(callsIncoming)));
                    report.setOutgoingCall(formatDuration(totalDuration(callsOutgoing)));
                    return report;
                }
            } else {
                throw new NullPointerException("No have Subscriber found for phone number: " + msisdn);
            }
        }
        catch (HttpClientErrorException.NotFound e){
            throw new NullPointerException("No have CDRNote found for phone number: " + msisdn);
        }
    }
    /**
     * Generates UDR reports for all subscribers, optionally filtered by month
     *
     * @param month the month in "YYYY-MM" format (optional; if null, defaults to current month)
     * @return a list of {@link UdrReport} objects for all subscribers
     */
    public List<UdrReport> udrReportForAll(String month){
        String effectiveMonth = (month == null || month.isEmpty())
                ? YearMonth.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM"))
                : month;

        List<Subscriber> allSubscribers = subscriberRepo.findAll();
        List<UdrReport> reports = new ArrayList<>();

        for(Subscriber subscriber : allSubscribers){
            reports.add(udrReportForMonthOrAllPeriod(subscriber.getPhoneNumber(), effectiveMonth));
        }
        return reports;
    }
    /**
     * Formats a duration in seconds into an HH:MM:SS string wrapped in a {@link DurationCall} object
     *
     * @param totalSeconds the total duration in seconds
     * @return a {@link DurationCall} object with the formatted duration
     */
    private DurationCall formatDuration(long totalSeconds) {
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
       DurationCall durationCall = new DurationCall();
       durationCall.setTotalTime(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        return durationCall;
    }
    /**
     * Calculates the total duration of a list of calls in seconds
     *
     * @param calls the list of {@link CDRNote} objects to calculate duration for
     * @return the total duration in seconds
     */
    private long totalDuration(List<CDRNote> calls){
        long totalDuration = 0;
        for (CDRNote call : calls){
            totalDuration += Duration.between(call.getStartCallTime(), call.getEndCallTime()).getSeconds();
        }
        return totalDuration;
    }
    /**
     * Parses a month string in "YYYY-MM" format into a start and end date range
     *
     * @param month the month string to parse
     * @return a list containing the start and end {@link ZonedDateTime} of the month
     * @throws IllegalArgumentException if the month format is invalid
     */
    private List<ZonedDateTime> parseMonth(String month) {
        try {
            String[] parts = month.split("-");
            int year = Integer.parseInt(parts[0]);
            int monthNum = Integer.parseInt(parts[1]);
            ZonedDateTime start = ZonedDateTime.of(year, monthNum, 1, 0, 0, 0, 0, ZoneOffset.UTC);
            ZonedDateTime end = start.plusMonths(1).minusSeconds(1);
            return List.of(start, end);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid month format: " + month + ". Expected 'YYYY-MM'");
        }
    }




}
