package com.plunner.plunner.models.adapters;

import com.plunner.plunner.general.PlunnerException;

/**
 * Created by claudio on 22/12/15.
 */
public class NoHttpException extends PlunnerException {
    private Boolean networkError = false;

    public NoHttpException() {
        super();
    }

    public NoHttpException(String detailMessage) {
        super(detailMessage);
    }

    public NoHttpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoHttpException(Throwable throwable) {
        super(throwable);
    }

    public Boolean getNetworkError() {
        return networkError;
    }

    public void setNetworkError(Boolean networkError) {
        this.networkError = networkError;
    }
}