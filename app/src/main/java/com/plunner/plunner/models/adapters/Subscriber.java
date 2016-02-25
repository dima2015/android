package com.plunner.plunner.models.adapters;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.plunner.plunner.general.Plunner;
import com.plunner.plunner.models.callbacks.interfaces.CallOnHttpError;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNext;
import com.plunner.plunner.models.callbacks.interfaces.CallOnNoHttpError;
import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.models.Model;

import java.io.IOException;

import retrofit.HttpException;

/**
 * Created by claudio on 19/12/15.
 */
public class Subscriber<T extends Model> extends rx.Subscriber<T> {
    Callable<T> callable = null;

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
        e.setNetworkError(networkProblem());
        if (e.getNetworkError()) {
            Log.e("Net error", e.getMessage(), e.getCause());
            showToast("Network problem");
        } else
            Log.e("Unknown error", e.getMessage(), e.getCause());
        if (callable != null && callable instanceof CallOnNoHttpError) {
            ((CallOnNoHttpError<T>) callable).onNoHttpError(e);
        }
        //TODO maybe thsi is thrown even when there are json problems
    }

    private void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Plunner.getAppContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean networkProblem() {
        ConnectivityManager cm =
                (ConnectivityManager) Plunner.getAppContext().getSystemService(Plunner.getAppContext().CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return !(activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    public void onError(com.plunner.plunner.models.adapters.HttpException e) {
        HttpException response = e.getCause();
        Log.w("Net error", Integer.toString(response.code()) + " " + response.message() + " " +
                e.getErrorBody());
        if (callable != null && callable instanceof CallOnHttpError) {
            ((CallOnHttpError<T>) callable).onHttpError(e);
        }
        //TODO automatically fresh token if 401???
    }

    @Override
    public void onNext(T t) {
        Log.v("data", t.toString());
        if(callable != null && callable instanceof CallOnNext)
            ((CallOnNext<T>) callable).onNext(t);
    }
}
