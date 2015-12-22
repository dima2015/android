package com.plunner.plunner.models.models.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.ModelException;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class Group extends com.plunner.plunner.models.models.general.Group {
    @SerializedName("meetings")
    @Expose
    @Valid
    protected List<Meeting> meetings = new ArrayList<>();

    public Group() {
    }

    public Group(String id, String name, String description) {
        super(id, name, description);
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
                new ListSubscriber<Group>(subscriber));
    }

    /**
     * @return The meetings
     */
    public List<Meeting> getMeetings() {
        return new ArrayList<Meeting>(meetings); //new object
    }

    static private interface RestInterface {
        @GET("/employees/groups/")
        Observable<List<Group>> index();
    }
}
