package com.pje.employeemanager.exception;

public class CAlreadyHolidayStatusDataException extends RuntimeException {
    public CAlreadyHolidayStatusDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAlreadyHolidayStatusDataException(String msg) {
        super(msg);
    }

    public CAlreadyHolidayStatusDataException() {
        super();
    }
}

