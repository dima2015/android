
package com.plunner.plunner.models.models.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.ListSubscriber;
import com.plunner.plunner.models.adapters.Retrofit;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.Listable;
import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.models.ModelException;
import com.plunner.plunner.models.models.ModelList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import javax.annotation.Generated;
import javax.validation.Valid;

import retrofit.http.GET;
import rx.Observable;
import rx.Subscription;

@Generated("org.jsonschema2pojo")
final public class Calendar extends Model<Calendar> implements Listable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("enabled")
    @Expose
    private String enabled;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("caldav")
    @Expose
    @Valid
    private Caldav caldav;
    private ModelList<Timeslot> timeslots = new ModelList<>(new Timeslot());

    /**
     * No args constructor for use in serialization
     */
    public Calendar() {
    }

    /**
     * @param id
     * @param enabled
     * @param caldav
     * @param name
     */
    public Calendar(String id, String name, String enabled, Caldav caldav) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.caldav = caldav;
    }

    public Calendar(String id, String name, String enabled) {
        this(id, name, enabled, null);
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

    public Calendar withId(String id) {
        this.id = id;
        return this;
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

    public Calendar withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @return The enabled
     */
    public String getEnabled() {
        return enabled;
    }

    /**
     * @param enabled The enabled
     */
    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Calendar withEnabled(String enabled) {
        this.enabled = enabled;
        return this;
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
     * @return The caldav
     */
    public Caldav getCaldav() {
        return caldav;
    }

    /**
     * @param caldav The caldav
     */
    public void setCaldav(Caldav caldav) {
        this.caldav = caldav;
    }

    public Calendar withCaldav(Caldav caldav) {
        this.caldav = caldav;
        return this;
    }

    /**
     * @return The timeslots
     */
    public ModelList<Timeslot> getTimeslots() throws CloneNotSupportedException {
        return timeslots.clone();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(employeeId).append(enabled).
                append(createdAt).append(updatedAt).append(caldav).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Calendar) == false) {
            return false;
        }
        Calendar rhs = ((Calendar) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).
                append(employeeId, rhs.employeeId).append(enabled, rhs.enabled).
                append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).
                append(caldav, rhs.caldav).isEquals();
    }

    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
        return null;
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null; //TODO save also the caldav, if != null
    }

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        return null;
    }

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 0)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 0)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(),
                new ListSubscriber<Calendar>(subscriber, this));
    }

    static private interface RestInterface {
        @GET("/employees/calendars/")
        Observable<List<Calendar>> index();
    }
}
