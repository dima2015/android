
package com.plunner.plunner.models.models.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;
import com.plunner.plunner.models.models.employee.utility.LoadResource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

@Generated("org.jsonschema2pojo")
public class Employee<S extends Employee> extends Model<S> {

    @SerializedName("id")
    @Expose
    protected String id;
    @SerializedName("name")
    @Expose
    protected String name;
    @SerializedName("email")
    @Expose
    protected String email;
    @SerializedName("company_id")
    @Expose
    protected String companyId;
    @SerializedName("created_at")
    @Expose
    protected String createdAt;
    @SerializedName("updated_at")
    @Expose
    protected String updatedAt;
    @SerializedName("is_planner")
    @Expose
    protected Boolean planner;


    private LoadResource<ModelList<Group>> groups = new LoadResource<ModelList<Group>>(new ModelList<Group>(new Group()));
    private LoadResource<ModelList<Meeting>> meetings = new LoadResource<ModelList<Meeting>>(new ModelList<Meeting>(new Meeting()));
    private LoadResource<ModelList<Calendar>> calendars = new LoadResource<ModelList<Calendar>>(new ModelList<Calendar>(new Calendar()));

    //TODO serialization?


    //TODO syncronized??
    //TODO clone

    /**
     * No args constructor for use in serialization
     */
    public Employee() {
    }

    /**
     * @param id
     * @param email
     * @param name
     */
    public Employee(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    //TODO check if the the old execution is still in waiting status
    //TODO delete, create and so on

    static public void getFactory() {
        //TODO implement getFactory that gives the right model planner or employee
    }

    @Override
    public rx.Subscription fresh(FreshSubscriber subscriber) {
        return get(subscriber);
    }

    @Override
    public rx.Subscription save(Subscriber subscriber) {
//TODO implement
        return null;
    }


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
     * Get groups, you should load them via the laod call
     *
     * @return laoder of groups
     */
    public LoadResource<ModelList<Group>> getGroups() {
        return groups;
    }

    /**
     * Get meetings, you should load them via the laod call
     *
     * @return laoder of meetings
     */
    public LoadResource<ModelList<Meeting>> getMeetings() {
        return meetings;
    }

    /**
     * Get calendars, you should load them via the laod call
     *
     * @return laoder of calendars
     */
    public LoadResource<ModelList<Calendar>> getCalendars() {
        return calendars;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The email
     */
    public void getEmail() {
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    public Employee withId(String id) {
        this.id = id;
        return this;
    }

    public Employee withName(String name) {
        this.name = name;
        return this;
    }

    public Employee withEmail(String email) {
        this.email = email;
        return this;
    }

    public Employee withCompanyId(String companyId) {
        this.companyId = companyId;
        return this;
    }

    /**
     *
     * @return
     * The planner
     */
    public boolean isPlanner() {
        return planner;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(email).append(companyId).
                append(createdAt).append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Employee) == false) {
            return false;
        }
        Employee rhs = ((Employee) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).
                append(email, rhs.email).append(companyId, rhs.companyId).
                append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).isEquals();
    }

    /**
     * Get calendars if they are <strong>already loaded</strong> via loadCalendars
     *
     * @return list of calendars
     */
    //  public ModelList<Calendar> getCalendars() throws CloneNotSupportedException {
    //    return calendars.clone();
    //}


   /* public rx.Subscription loadCalendars(LoadCalendarsSubscriber subscriber) {
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
    }*/

    //public rx.Subscription loadMeetings(Callable<ModelList<com.plunner.plunner.models.models.employee.Meeting>> callable) {
//        return loadMeetings(new LoadMeetingsSubscriber(callable));
    //  }

   /* public rx.Subscription loadMeetings() {
        return loadMeetings(new LoadMeetingsSubscriber());
    }*/

    static private interface RestInterface {
        @GET("/employees/employee/")
        Observable<Employee> get();
    }
}