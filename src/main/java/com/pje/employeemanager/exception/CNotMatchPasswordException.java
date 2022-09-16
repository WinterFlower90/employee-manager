package com.pje.employeemanager.exception;

public class CNotMatchPasswordException extends RuntimeException {
    public CNotMatchPasswordException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNotMatchPasswordException(String msg) {
        super(msg);
    }

    public CNotMatchPasswordException() {
        super();
    }
}