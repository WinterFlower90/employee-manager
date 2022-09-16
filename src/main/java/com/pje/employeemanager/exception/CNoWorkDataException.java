package com.pje.employeemanager.exception;

public class CNoWorkDataException extends RuntimeException {
    public CNoWorkDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNoWorkDataException(String msg) {
        super(msg);
    }

    public CNoWorkDataException() {
        super();
    }
}