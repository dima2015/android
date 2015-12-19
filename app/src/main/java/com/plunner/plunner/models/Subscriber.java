package com.plunner.plunner.models;

import android.util.Log;

import retrofit.HttpException;

/**
 * Created by claudio on 19/12/15.
 */
public class Subscriber<T extends Model> extends rx.Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        // cast to retrofit.HttpException to get the response code
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            int code = response.code();
            Log.d("net error", Integer.toString(code));
        }
        //TODO else
    }

    @Override
    public void onNext(T t) {
        Log.d("data", t.toString());
    }
}
