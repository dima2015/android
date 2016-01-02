package com.plunner.plunner.models.login;


import android.util.Log;

import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit.Response;


/**
 * Created by claudio on 20/12/15.
 */
public class LoginManager implements CallOnHttpError {
    private static LoginManager ourInstance = new LoginManager();
    private String token;
    private boolean tokenCanBeSet = true;

    private LoginManager() {
    }

    public static LoginManager getInstance() {
        return ourInstance;
    }

    //TODO not static
    public static rx.Subscription loginByData(String company, String email, String password) {
        final LoginManager loginManager = getInstance();
        loginManager.tokenCanBeSet = false;
        return (new Token()).get(company, email, password, new Subscriber<Token>() {
            @Override
            public void onNext(Token token) {
                super.onNext(token);
                loginManager.tokenCanBeSet = true;
                loginManager.token = "Bearer " + token.getToken();
            }
        });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (tokenCanBeSet) {
            this.token = token;
        }
    }

    public LoginManager withToken(String token) {
        if (tokenCanBeSet) {
            this.token = token;
        }
        return this;
    }

    public LoginManager loginSync(String company, String email, String password) throws LoginException {
        Response<Token> response = null;
        try {
            response = new Token().getSync(company, email, password);
        } catch (IOException e) {
            Log.w("Net login error", e.getMessage());
            throw new LoginException("Net login error: " + e.getMessage(), e);
        }
        if (response.isSuccess()) {
            Token token = response.body();
            this.tokenCanBeSet = true;
            this.token = "Bearer " + token.getToken();
            return this;
        }
        try {
            String body = response.errorBody().string().toString();
            Log.w("Login error", Integer.toString(response.code()) + " " + response.message() +
                    " " + body);
            throw new LoginException(Integer.toString(response.code()) + " " +
                    response.message(), new JSONObject(body));
        } catch (IOException e) {
            Log.w("errorBody error", Integer.toString(response.code()) + " " + response.message() + e);
            throw new LoginException("errorBody error: " + Integer.toString(response.code()) + " " + e);
        } catch (JSONException e) {
            Log.w("JSON error", Integer.toString(response.code()) + " " + response.message() + e);
            throw new LoginException("JSON error: " + Integer.toString(response.code()) + " " + e);
        }

    }

    @Override
    public void onHttpError(HttpException e) {
        //TODO implement CallOnHttpError
    }

    //TODO when the error is not http?
}
