package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.FatherParameters;
import com.plunner.plunner.models.models.ModelException;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 25/02/16.
 */
public class MeetingTimeslot extends com.plunner.plunner.models.models.employee.MeetingTimeslot<MeetingTimeslot>
        implements FatherParameters {
    private String[] fatherParameters = null;

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 2)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 2)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(parameters[0], parameters[1]),
                new ListSubscriber<MeetingTimeslot>(subscriber, this));
    }

    @Override
    public void setFatherParameters(String... fatherParameters) {
        this.fatherParameters = fatherParameters;
    }

    private interface RestInterface {
        @GET("/employees/planners/groups/{group}/meetings/{meeting}/timeslots/")
        Observable<List<MeetingTimeslot>> index(@Path("group") String groupId, @Path("meeting") String meetingId);
    }
}
