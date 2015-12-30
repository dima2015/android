package com.plunner.plunner.models.adapters;

import android.util.Log;

import com.plunner.plunner.models.callbacks.interfaces.CallOnError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.callbacks.interfaces.SetModel;
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
    public Subscriber(Callable<T> callable)
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
    public void onError(Throwable e) {
        boolean onError = false;
        //generic error
        if (callable != null && callable instanceof CallOnError) {
            ((CallOnError) callable).onError(e);
            onError = true;
        }
        // HTTP error
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            if(callable != null && callable instanceof CallOnHttpError)
                ((CallOnHttpError) callable).onHttpError(response);
            int code = response.code();
            try {
                Log.w("Net error", Integer.toString(code)+ " " + response.message() + " " + response.response().errorBody().string().toString());
            } catch (IOException e1) {
                Log.w("errorBody error", Integer.toString(code) + " " + response.message() + " " + e1);
            }
            //TODO automatically fresh token if 401???
        }
        //NO HTTP error
        else {
            Log.e("Net error", "Unknown error", e);
            if (callable != null && callable instanceof CallOnNoHttpError) {
                ((CallOnNoHttpError) callable).onNoHttpError(e);
                onError = true;
            }
            //if the no HTTP error is not tracked I print the stack trace
            if (!onError)
                e.printStackTrace();
        }

    }

    @Override
    public void onNext(T t) {
        Log.v("data", t.toString());
        if (callable != null && callable instanceof SetModel)
            ((SetModel) callable).setModel(t);
        if(callable != null && callable instanceof CallOnNext)
            ((CallOnNext) callable).onNext(t);
    }
}
