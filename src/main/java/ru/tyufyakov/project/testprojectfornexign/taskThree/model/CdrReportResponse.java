package ru.tyufyakov.project.testprojectfornexign.taskThree.model;

import lombok.Data;

@Data
public class CdrReportResponse {
    private String uuid;
    private String message;

    public CdrReportResponse(String uuid, String message) {
        this.uuid = uuid;
        this.message = message;
    }
}