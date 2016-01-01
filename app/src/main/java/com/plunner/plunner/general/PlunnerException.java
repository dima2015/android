package com.plunner.plunner.general;

/**
 * Created by claudio on 22/12/15.
 */
public class PlunnerException extends Exception {
    public PlunnerException() {
        super();
    }

    public PlunnerException(String detailMessage) {
        super(detailMessage);
    }

    public PlunnerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public PlunnerException(Throwable throwable) {
        super(throwable);
    }
}