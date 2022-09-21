package com.pje.employeemanager.exception;

public class CNoHolidayCountRemainException extends RuntimeException {
    public CNoHolidayCountRemainException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNoHolidayCountRemainException(String msg) {
        super(msg);
    }

    public CNoHolidayCountRemainException() {
        super();
    }
}