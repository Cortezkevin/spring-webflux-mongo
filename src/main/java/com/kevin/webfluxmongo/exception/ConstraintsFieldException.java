package com.kevin.webfluxmongo.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ConstraintsFieldException extends Exception {

    private Map<String, Object> fieldsErrors;
    private HttpStatus status;

    public ConstraintsFieldException(Map<String, Object> fieldsErrors, HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.fieldsErrors = fieldsErrors;
    }

    public Map<String, Object> getFieldsErrors() {
        return fieldsErrors;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
