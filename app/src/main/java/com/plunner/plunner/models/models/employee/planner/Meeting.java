package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.FatherParameters;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.utility.LoadResource;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 25/02/16.
 */
final public class Meeting extends com.plunner.plunner.models.models.employee.Meeting<Meeting> implements FatherParameters {
    private String[] fatherParameters = null;

    private LoadResource<ModelList<MeetingTimeslot>> meetingsTimeslotManaged = new LoadResource<ModelList<MeetingTimeslot>>(new ModelList<MeetingTimeslot>(new MeetingTimeslot()));

    public LoadResource<ModelList<MeetingTimeslot>> getMeetingsTimeslotManaged() {
        meetingsTimeslotManaged.setParameters(fatherParameters[0], id);
        return meetingsTimeslotManaged;
    }

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 1)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 1)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(parameters[0]),
                new ListSubscriber<Meeting>(subscriber, this));
    }

    @Override
    public void setFatherParameters(String... fatherParameters) {
        this.fatherParameters = fatherParameters;
    }

    private interface RestInterface {
        //TODO check if current is keep in consideration
        @GET("/employees/planners/groups/{group}/meetings/?current=1")
        Observable<List<Meeting>> index(@Path("group") String groupId);
    }
}
