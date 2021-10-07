package com.co2nsensus.co2mission.exception;

import java.util.Date;

public class CustomErrorResponse {

    private Date timestamp;
    private String code;
    private String message;

    public CustomErrorResponse(Date timestamp,String code, String message) {
        super();
        this.code = code;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}