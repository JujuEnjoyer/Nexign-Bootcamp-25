package ru.tyufyakov.project.testprojectfornexign.taskTwo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UdrReport {
    private long msisdn;
    private DurationCall incomingCall;
    private DurationCall outgoingCall;
}
