package com.pje.employeemanager.exception;

public class CAlreadyWorkInDataException extends RuntimeException {
    public CAlreadyWorkInDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAlreadyWorkInDataException(String msg) {
        super(msg);
    }

    public CAlreadyWorkInDataException() {
        super();
    }
}