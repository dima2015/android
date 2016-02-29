package com.plunner.plunner.models.adapters.utils;

import android.util.Log;

import com.plunner.plunner.models.adapters.HttpException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by claudio on 29/02/16.
 */
public class ErrorDetails {
    private HttpException httpException;
    private Map<String, String> errors = null;

    public ErrorDetails(HttpException httpException) throws ErrorDetailsException {
        this.httpException = httpException;
        if (httpException.getCause().code() != 422)
            throw new ErrorDetailsException("Error code is not 422");
    }

    static private Map<String, String> generateMap(JSONObject jsonObject) throws ErrorDetailsException {
        Map<String, String> ret = new HashMap<>();
        java.util.Iterator<java.lang.String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                JSONArray errors2 = jsonObject.getJSONArray(key);
                String errorsJoined = "";
                for (int i = 0; i < errors2.length(); i++) {
                    try {
                        errorsJoined += errors2.getString(i) + "\n";
                    } catch (JSONException e3) {
                        Log.w("JSON parsing error", e3);
                        throw new ErrorDetailsException("JSON parsing error", e3);
                    }
                }
                if (errorsJoined.length() > 0) {
                    //remove last ", "
                    errorsJoined = errorsJoined.substring(0, errorsJoined.length() - 1);
                    ret.put(key, errorsJoined);
                }
            } catch (JSONException e1) {
                //it is not an array
                try {
                    ret.put(key, jsonObject.getString(key));
                } catch (JSONException e2) {
                    Log.w("JSON parsing error", e2);
                    throw new ErrorDetailsException("JSON parsing error", e2);
                }
            }
        }
        return ret;
    }

    public Map<String, String> getErrors() throws ErrorDetailsException {
        if (errors == null)
            generateErrors();
        return errors;
    }

    private void generateErrors() throws ErrorDetailsException {
        String body = httpException.getErrorBody();
        try {
            errors = generateMap(new JSONObject(body));
        } catch (JSONException e) {
            Log.w("JSON error", body + e);
            throw new ErrorDetailsException("JSON error", e);
        }
    }

    public static class ErrorDetailsException extends Exception {
        public ErrorDetailsException() {
            super();
        }

        public ErrorDetailsException(String detailMessage) {
            super(detailMessage);
        }

        public ErrorDetailsException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ErrorDetailsException(Throwable throwable) {
            super(throwable);
        }
    }
}
