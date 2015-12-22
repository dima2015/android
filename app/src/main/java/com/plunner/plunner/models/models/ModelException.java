package com.plunner.plunner.models.models;

/**
 * Created by claudio on 22/12/15.
 */
public class ModelException extends Exception {
    public ModelException() {
    }

    public ModelException(String detailMessage) {
        super(detailMessage);
    }

    public ModelException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ModelException(Throwable throwable) {
        super(throwable);
    }
}