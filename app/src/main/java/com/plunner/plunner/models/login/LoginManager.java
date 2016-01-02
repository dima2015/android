package com.plunner.plunner.models.login;

import com.plunner.plunner.models.adapters.HttpException;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;


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

    public LoginManager setToken(String token) {
        if (tokenCanBeSet) {
            this.token = token;
        }
        return this;
    }

    @Override
    public void onHttpError(HttpException e) {
        //TODO implement CallOnHttpError
    }

    //TODO when the error is not http?
}
