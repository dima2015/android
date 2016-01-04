package com.plunner.plunner.models.adapters;

import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import com.plunner.plunner.R;
import com.plunner.plunner.general.Plunner;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.callbacks.interfaces.SetModel;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.Model;

import java.io.IOException;

import retrofit.HttpException;

/**
 * Created by claudio on 19/12/15.
 */
public class Subscriber<T extends Model> extends rx.Subscriber<T> {
    Callable callable = null;

    /**
     * constructor called if we want to set the model get if wwe don't have errors on the the object
     * given
     * @param callable
     */
    public Subscriber(Callable callable)
    {
        super();
        this.callable = callable;
    }

    public Subscriber()
    {
        super();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    final public void onError(Throwable e) {
        // HTTP error
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            com.plunner.plunner.models.adapters.HttpException httpException = null;
            try {
                httpException = new com.plunner.plunner.models.adapters.HttpException(response,
                        response.response().errorBody().string().toString());
                onError(httpException);
            } catch (IOException e1) {
                onError(new NoHttpException("errorBody error: " + Integer.toString(response.code()) +
                        " " + response.message() + " - " + e1.getMessage(), e1));
            }
        }
        //NO HTTP error
        else {
            onError(new NoHttpException(e.getMessage(), e));
        }
    }

    public void onError(NoHttpException e) {
        Log.e("Net error", e.getMessage(), e.getCause());
        if (callable != null && callable instanceof CallOnNoHttpError) {
            ((CallOnNoHttpError) callable).onNoHttpError(e);
        }
    }

    public void onError(com.plunner.plunner.models.adapters.HttpException e) {
        HttpException response = e.getCause();
        Log.w("Net error", Integer.toString(response.code()) + " " + response.message() + " " +
                e.getErrorBody());
        //invalidate token
        LoginManager loginManager = LoginManager.getInstance();
        if (response.code() == 401 && loginManager.getToken() != null) {
            Context context = Plunner.getAppContext();
            AccountManager.get(context).invalidateAuthToken(context.getString(R.string.account_type),
                    loginManager.getToken());
            //TODO request new token? here we can perform sync task but we need a timeout??? re call original on error?
            //TODO or the user should manage the refresh of the token, maybe we can create a abstract calss
            //TODO how to manage request timeout? on error?
        }
        if (callable != null && callable instanceof CallOnHttpError) {
            ((CallOnHttpError) callable).onHttpError(e);
        }
        //TODO automatically fresh token if 401???
    }

    @Override
    public void onNext(T t) {
        Log.v("data", t.toString());
        if(callable != null && callable instanceof SetModel)
            ((SetModel) callable).setModel(t);
        if(callable != null && callable instanceof CallOnNext)
            ((CallOnNext) callable).onNext(t);
    }
}
