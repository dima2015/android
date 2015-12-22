package com.plunner.plunner.models.models.employee;

import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.ModelException;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class Meeting extends com.plunner.plunner.models.models.general.Meeting {
    public Meeting() {
    }

    public Meeting(String id, String title, String description, Object startTime, String duration) {
        super(id, title, description, startTime, duration);
    }

    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
        return null; //TODO implement
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null; //TODO implement
    }

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        if (parameters.length != 0)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 0)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(),
                new ListSubscriber<Meeting>(subscriber));
    }

    static private interface RestInterface {
        @GET("/employees/meetings/")
        Observable<List<Meeting>> index();
    }
}
