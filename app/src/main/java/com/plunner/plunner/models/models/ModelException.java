package com.plunner.plunner.models.models;

import com.plunner.plunner.general.PlunnerException;

/**
 * Created by claudio on 22/12/15.
 */
public class ModelException extends PlunnerException {
    public ModelException() {
        super();
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