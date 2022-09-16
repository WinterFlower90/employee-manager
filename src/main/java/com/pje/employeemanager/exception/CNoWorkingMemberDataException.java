package com.pje.employeemanager.exception;

public class CNoWorkingMemberDataException extends RuntimeException {
    public CNoWorkingMemberDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNoWorkingMemberDataException(String msg) {
        super(msg);
    }

    public CNoWorkingMemberDataException() {
        super();
    }
}