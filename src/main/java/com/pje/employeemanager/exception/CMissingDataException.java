package com.pje.basic.exception;

public class CMissingDataException extends RuntimeException {
    public CMissingDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public CMissingDataException(String msg) {
        super(msg);
    }

    public CMissingDataException() {
        super();
    }
}

