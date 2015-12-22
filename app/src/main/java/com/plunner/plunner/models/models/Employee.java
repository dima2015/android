
package com.plunner.plunner.models.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.callbacks.interfaces.Callable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

@Generated("org.jsonschema2pojo")
public class Employee extends Model {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_planner")
    @Expose
    private Boolean planner;
    //TODO automatically get? like Group for employee
    private ModelList<Group> groups = new ModelList<Group>();

    //TODO serialization?

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
    public ModelList<Group> getGroups() throws CloneNotSupportedException {
        return groups.clone();
    }

    public rx.Subscription loadGroups(LoadGroupsSubscriber subscriber) {
        return new Group().get(subscriber);
    }

    public rx.Subscription loadGroups(Callable<ModelList<Group>> callable) {
        return loadGroups(new LoadGroupsSubscriber(callable));
    }

    public rx.Subscription loadGroups() {
        return loadGroups(new LoadGroupsSubscriber());
    }

    static private interface RestInterface {
        @GET("/employees/employee/")
        Observable<Employee> get();
    }

    /**
     * LoadGroupsSubscriber that insert the groups list in employee model
     *
     * @param <T>
     */
    public class LoadGroupsSubscriber extends Subscriber<ModelList<Group>> {
        public LoadGroupsSubscriber(Callable callable) {
            super(callable);
        }

        public LoadGroupsSubscriber() {
        }

        @Override
        public void onNext(ModelList<Group> model) {
            Employee.this.groups = model;
            super.onNext(model);
        }
    }
}