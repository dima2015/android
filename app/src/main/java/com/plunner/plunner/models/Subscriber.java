package com.plunner.plunner.models;

import android.util.Log;

import com.plunner.plunner.CanSetModelInterface;

import retrofit.HttpException;

/**
 * Created by claudio on 19/12/15.
 */
public class Subscriber<T extends Model> extends rx.Subscriber<T> {
    CanSetModelInterface canSetModel = null;

    /**
     * cosntructor called if we want to set the model get if wwe don't have errors on the the object
     * given
     * @param canSetModel
     */
    public Subscriber(CanSetModelInterface canSetModel)
    {
        super();
        this.canSetModel = canSetModel;
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
        // cast to retrofit.HttpException to get the response code
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            int code = response.code();
            Log.v("net error", Integer.toString(code));
        }
        //TODO else
    }

    @Override
    public void onNext(T t) {
        Log.v("data", t.toString());
        //if(canSetModel != null)
          //  canSetModel.setModel(t);
    }
}
