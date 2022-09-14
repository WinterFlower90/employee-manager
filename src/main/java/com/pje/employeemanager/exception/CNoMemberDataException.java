package com.pje.basic.exception;

public class CNoMemberDataException extends RuntimeException {
    public CNoMemberDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CNoMemberDataException(String msg) {
        super(msg);
    }

    public CNoMemberDataException() {
        super();
    }
}