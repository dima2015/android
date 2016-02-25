package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.FatherParameters;
import com.plunner.plunner.models.models.ModelException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.http.DELETE;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 25/02/16.
 */
public class MeetingTimeslot extends com.plunner.plunner.models.models.employee.MeetingTimeslot<MeetingTimeslot>
        implements FatherParameters {
    private String[] fatherParameters = null;

    public MeetingTimeslot() {
    }

    public MeetingTimeslot(String id, String timeStart, String timeEnd) {
        super(id, timeStart, timeEnd);
    }

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 2)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 2)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(parameters[0], parameters[1]),
                new ListSubscriber<MeetingTimeslot>(subscriber, this));
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        Map<String, String> data = new HashMap<>();
        data.put("time_start", timeStart);
        data.put("time_end", timeEnd);
        if (id == null) {
            return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).create(
                            fatherParameters[0], fatherParameters[1], data),
                    subscriber);
        } else {
            return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).update(
                            fatherParameters[0], fatherParameters[1], id, data),
                    subscriber);
        }
    }

    @Override
    public Subscription delete(Subscriber subscriber) {
        return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).delete(
                        fatherParameters[0], fatherParameters[1], id),
                subscriber);
    }

    @Override
    public void setFatherParameters(String... fatherParameters) {
        this.fatherParameters = fatherParameters;
    }

    private interface RestInterface {
        @GET("/employees/planners/groups/{group}/meetings/{meeting}/timeslots/")
        Observable<List<MeetingTimeslot>> index(@Path("group") String groupId, @Path("meeting") String meetingId);

        @FormUrlEncoded
        @POST("/employees/planners/groups/{group}/meetings/{meeting}/timeslots/")
        Observable<MeetingTimeslot> create(@Path("group") String groupId, @Path("meeting") String meetingId,
                                           @FieldMap Map<String, String> data);

        @FormUrlEncoded
        @PUT("/employees/planners/groups/{group}/meetings/{meeting}/timeslots/{timeslot}/")
        Observable<MeetingTimeslot> update(@Path("group") String groupId, @Path("meeting") String meetingId,
                                           @Path("timeslot") String timeslotId, @FieldMap Map<String, String> data);

        @DELETE("/employees/planners/groups/{group}/meetings/{meeting}/timeslots/{timeslot}/")
        Observable<MeetingTimeslot> delete(@Path("group") String groupId, @Path("meeting") String meetingId,
                                           @Path("timeslot") String timeslotId);
    }
}
