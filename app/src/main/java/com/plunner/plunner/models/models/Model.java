package com.plunner.plunner.models.models;


import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;

import rx.Observable;

/**
 * Created by claudio on 19/12/15.
 */
abstract public class Model {

    static protected <T> T createRetrofit(Class<T> interfaceClass) {
        return Retrofit.createRetrofit(interfaceClass);
    }

    static protected <T extends Model> rx.Subscription subscribe(Observable<T> call, Subscriber<T> subscriber) {
        return Retrofit.subscribe(call, subscriber);
    }


    abstract public rx.Subscription fresh();
    abstract public rx.Subscription fresh(Subscriber subscriber);
    abstract public rx.Subscription save();
    abstract public rx.Subscription save(Subscriber subscriber);


}
