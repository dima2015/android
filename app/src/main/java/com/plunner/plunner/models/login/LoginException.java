package com.plunner.plunner.models.login;

import com.plunner.plunner.general.PlunnerException;

import org.json.JSONArray;

/**
 * Created by claudio on 22/12/15.
 */
public class LoginException extends PlunnerException {
    private JSONArray jsonErrors = null;
    //TODO use the same way for models

    public LoginException() {
        super();
    }

    public LoginException(String detailMessage) {
        super(detailMessage);
    }

    public LoginException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public LoginException(Throwable throwable) {
        super(throwable);
    }

    public LoginException(String detailMessage, JSONArray jsonErrors) {
        super(detailMessage);
        this.jsonErrors = jsonErrors;
    }

    public JSONArray getJsonErrors() {
        return jsonErrors;
    }
}