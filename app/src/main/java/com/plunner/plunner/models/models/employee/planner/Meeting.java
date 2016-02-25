package com.plunner.plunner.models.models.employee.planner;

import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.FatherParameters;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.utility.LoadResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
final public class Meeting extends com.plunner.plunner.models.models.employee.Meeting<Meeting> implements FatherParameters {
    private String[] fatherParameters = null;

    private LoadResource<ModelList<MeetingTimeslot>> meetingsTimeslotManaged = new LoadResource<ModelList<MeetingTimeslot>>(new ModelList<MeetingTimeslot>(new MeetingTimeslot()));

    public Meeting() {
    }


    /**
     * @param id          this must be = null to create a new object
     * @param title
     * @param description
     * @param duration
     */
    public Meeting(String id, String title, String description, String duration) {
        super(id, title, description, duration);
    }

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

    @Override
    public Subscription save(Subscriber subscriber) {
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);
        data.put("duration", duration);
        if (id == null) {
            return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).create(fatherParameters[0], data),
                    subscriber);
        } else {
            return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).update(fatherParameters[0], id, data),
                    subscriber);
        }
    }

    private interface RestInterface {
        //TODO check if current is keep in consideration
        @GET("/employees/planners/groups/{group}/meetings/?current=1")
        Observable<List<Meeting>> index(@Path("group") String groupId);

        @FormUrlEncoded
        @POST("/employees/planners/groups/{group}/meetings/")
        Observable<Meeting> create(@Path("group") String groupId, @FieldMap Map<String, String> data);

        @FormUrlEncoded
        @PUT("/employees/planners/groups/{group}/meetings/{meeting}/")
        Observable<Meeting> update(@Path("group") String groupId, @Path("meeting") String meetingId,
                                   @FieldMap Map<String, String> data);
    }
}
