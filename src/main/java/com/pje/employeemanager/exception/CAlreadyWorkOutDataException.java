package com.pje.employeemanager.exception;

public class CAlreadyWorkOutDataException extends RuntimeException {
    public CAlreadyWorkOutDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CAlreadyWorkOutDataException(String msg) {
        super(msg);
    }

    public CAlreadyWorkOutDataException() {
        super();
    }
}