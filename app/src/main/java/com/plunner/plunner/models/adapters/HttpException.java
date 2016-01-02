package com.plunner.plunner.models.adapters;

import com.plunner.plunner.general.PlunnerException;

/**
 * Created by claudio on 22/12/15.
 */
public class HttpException extends PlunnerException {
    private String errorBody;

    public HttpException() {
        super();
    }

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    public HttpException(String detailMessage, retrofit.HttpException throwable) {
        super(detailMessage, throwable);
    }

    public HttpException(retrofit.HttpException throwable) {
        super(throwable);
    }

    public HttpException(String detailMessage, String errorBody) {
        super(detailMessage);
        this.errorBody = errorBody;
    }

    public HttpException(String detailMessage, retrofit.HttpException throwable, String errorBody) {
        super(detailMessage, throwable);
        this.errorBody = errorBody;
    }

    public HttpException(retrofit.HttpException throwable, String errorBody) {
        super(throwable);
        this.errorBody = errorBody;
    }

    public String getErrorBody() {
        return errorBody;
    }

    @Override
    public retrofit.HttpException getCause() {
        return (retrofit.HttpException) super.getCause();
    }
}