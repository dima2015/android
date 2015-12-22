package com.plunner.plunner.models.models.employee;

import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

/**
 * Created by claudio on 22/12/15.
 */
public class Employee extends com.plunner.plunner.models.models.general.Employee {
    private ModelList<Group> groups = new ModelList<com.plunner.plunner.models.models.employee.Group>();
    private ModelList<Calendar> calendars = new ModelList<Calendar>();
    private ModelList<com.plunner.plunner.models.models.employee.Meeting> meetings = new ModelList<com.plunner.plunner.models.models.employee.Meeting>();

    //TODO syncronized??
    //TODO clone

    @Override
    public rx.Subscription fresh(FreshSubscriber subscriber) {
        return get(subscriber);
    }

    @Override
    public rx.Subscription save(Subscriber subscriber) {
//TODO implement
        return null;
    }

    //TODO check if the the old execution is still in waiting status
    //TODO delete, create and so on

    /**
     * <strong>CAUTION:</strong> this give a new object
     *
     * @param subscriber
     * @param parameters the parameters to perform the get unequivocally
     * @return
     */
    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        if (parameters.length != 0)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 0)"));

        return Retrofit.subscribe(Retrofit.createRetrofit(RestInterface.class).get(), subscriber);
    }

    /**
     * Get groups if they are <strong>already loaded</strong> via loadGroups
     *
     * @return list of groups
     */
    public ModelList<com.plunner.plunner.models.models.employee.Group> getGroups() throws CloneNotSupportedException {
        return groups.clone();
    }

    /**
     * Get meetings if they are <strong>already loaded</strong> via loadMeetings
     *
     * @return list of groups
     */
    public ModelList<com.plunner.plunner.models.models.employee.Meeting> getMeetings() throws CloneNotSupportedException {
        return meetings.clone();
    }


    /**
     * Get calendars if they are <strong>already loaded</strong> via loadCalendars
     *
     * @return list of calendars
     */
    public ModelList<Calendar> getCalendars() throws CloneNotSupportedException {
        return calendars.clone();
    }

    public rx.Subscription loadGroups(LoadGroupsSubscriber subscriber) {
        return new com.plunner.plunner.models.models.employee.Group().get(subscriber);
    }

    public rx.Subscription loadGroups(Callable<ModelList<Group>> callable) {
        return loadGroups(new LoadGroupsSubscriber(callable));
    }

    public rx.Subscription loadGroups() {
        return loadGroups(new LoadGroupsSubscriber());
    }

    public rx.Subscription loadCalendars(LoadCalendarsSubscriber subscriber) {
        return new Calendar().get(subscriber);
    }

    public rx.Subscription loadCalendars(Callable<ModelList<Calendar>> callable) {
        return loadCalendars(new LoadCalendarsSubscriber(callable));
    }

    public rx.Subscription loadCalendars() {
        return loadCalendars(new LoadCalendarsSubscriber());
    }

    public rx.Subscription loadMeetings(LoadMeetingsSubscriber subscriber) {
        return new com.plunner.plunner.models.models.employee.Meeting().get(subscriber);
    }

    public rx.Subscription loadMeetings(Callable<ModelList<com.plunner.plunner.models.models.employee.Meeting>> callable) {
        return loadMeetings(new LoadMeetingsSubscriber(callable));
    }

    public rx.Subscription loadMeetings() {
        return loadMeetings(new LoadMeetingsSubscriber());
    }

    static private interface RestInterface {
        @GET("/employees/employee/")
        Observable<Employee> get();
    }

    /**
     * LoadGroupsSubscriber that insert the groups list in employee model
     */
    public class LoadGroupsSubscriber extends Subscriber<ModelList<com.plunner.plunner.models.models.employee.Group>> {
        public LoadGroupsSubscriber(Callable callable) {
            super(callable);
        }

        public LoadGroupsSubscriber() {
        }

        @Override
        public void onNext(ModelList<com.plunner.plunner.models.models.employee.Group> groups) {
            Employee.this.groups = groups;
            super.onNext(groups);
        }
    }

    /**
     * LoadCalendarsSubscriber that insert the calendars list in employee model
     */
    public class LoadCalendarsSubscriber extends Subscriber<ModelList<Calendar>> {
        public LoadCalendarsSubscriber(Callable callable) {
            super(callable);
        }

        public LoadCalendarsSubscriber() {
        }

        @Override
        public void onNext(ModelList<Calendar> calendars) {
            Employee.this.calendars = calendars;
            super.onNext(calendars);
        }
    }

    /**
     * LoadCalendarsSubscriber that insert the calendars list in employee model
     */
    public class LoadMeetingsSubscriber extends Subscriber<ModelList<com.plunner.plunner.models.models.employee.Meeting>> {
        public LoadMeetingsSubscriber(Callable callable) {
            super(callable);
        }

        public LoadMeetingsSubscriber() {
        }

        @Override
        public void onNext(ModelList<Meeting> meetings) {
            Employee.this.meetings = meetings;
            super.onNext(meetings);
        }
    }
}
