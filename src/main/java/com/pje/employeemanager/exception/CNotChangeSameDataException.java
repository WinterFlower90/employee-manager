package com.pje.employeemanager.exception;

public class CNotChangeSameDataException extends RuntimeException {
    public CNotChangeSameDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNotChangeSameDataException(String msg) {
        super(msg);
    }

    public CNotChangeSameDataException() {
        super();
    }
}