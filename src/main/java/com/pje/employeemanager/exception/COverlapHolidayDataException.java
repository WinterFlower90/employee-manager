package com.pje.employeemanager.exception;

public class COverlapHolidayDataException extends RuntimeException {
    public COverlapHolidayDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public COverlapHolidayDataException(String msg) {
        super(msg);
    }

    public COverlapHolidayDataException() {
        super();
    }
}

