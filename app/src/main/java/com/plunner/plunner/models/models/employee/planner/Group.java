package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.utils.LoadResource;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 25/02/16.
 */
final public class Group extends com.plunner.plunner.models.models.employee.Group<Group> {

    private LoadResource<ModelList<Meeting>> meetingsManaged = new LoadResource<ModelList<Meeting>>(new ModelList<Meeting>(new Meeting()));

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 0)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 0)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(),
                new ListSubscriber<Group>(subscriber, this));
    }

    public LoadResource<ModelList<Meeting>> getMeetingsManaged() {
        meetingsManaged.setParameters(id);
        return meetingsManaged;
    }

    private interface RestInterface {
        @GET("/employees/planners/groups/")
        Observable<List<Group>> index();
    }
}
