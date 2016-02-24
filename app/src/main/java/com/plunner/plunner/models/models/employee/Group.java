
package com.plunner.plunner.models.models.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.Listable;
import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.models.ModelException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.validation.Valid;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

@Generated("org.jsonschema2pojo")
public class Group<S extends Group> extends Model<S> implements Listable {

    @SerializedName("id")
    @Expose
    protected String id;
    @SerializedName("created_at")
    @Expose
    protected String createdAt;
    @SerializedName("updated_at")
    @Expose
    protected String updatedAt;
    @SerializedName("name")
    @Expose
    protected String name;
    @SerializedName("description")
    @Expose
    protected String description;
    @SerializedName("company_id")
    @Expose
    protected String companyId;
    @SerializedName("planner_id")
    @Expose
    protected String plannerId;
    @SerializedName("planner_name")
    @Expose
    protected String plannerName;
    @SerializedName("meetings")
    @Expose
    @Valid
    protected List<Meeting> meetings = new ArrayList<>();


    /**
     * No args constructor for use in serialization
     */
    public Group() {
    }

    /**
     * @param id
     * @param description
     * @param name
     */
    public Group(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
        return null;
    }

    /**
     * @return The meetings
     */
    public List<Meeting> getMeetings() {
        //TODO clone?
        return new ArrayList<Meeting>(meetings); //new object
    }

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 0)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 0)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(),
                new ListSubscriber<Group>(subscriber, this));
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
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @return The plannerId
     */
    public String getPlannerId() {
        return plannerId;
    }

    /**
     * @return The plannerName
     */
    public String getPlannerName() {
        return plannerName;
    }

    public Group withId(String id) {
        this.id = id;
        return this;
    }

    public Group withName(String name) {
        this.name = name;
        return this;
    }

    public Group withDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(createdAt).append(updatedAt).append(name).
                append(description).append(companyId).append(plannerId).append(plannerName).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Group) == false) {
            return false;
        }
        Group rhs = ((Group) other);
        return new EqualsBuilder().append(id, rhs.id).append(createdAt, rhs.createdAt).
                append(updatedAt, rhs.updatedAt).append(name, rhs.name).
                append(description, rhs.description).append(companyId, rhs.companyId).
                append(plannerId, rhs.plannerId).append(plannerName, rhs.plannerName).isEquals();
    }

    static private interface RestInterface {
        @GET("/employees/groups/")
        Observable<List<Group>> index();
    }
}
