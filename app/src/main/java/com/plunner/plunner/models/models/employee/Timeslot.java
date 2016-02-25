
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

import java.util.List;

import javax.annotation.Generated;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import rx.Subscription;

@Generated("org.jsonschema2pojo")
final public class Timeslot extends Model<Timeslot> implements Listable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("time_start")
    @Expose
    private String timeStart;
    @SerializedName("time_end")
    @Expose
    private String timeEnd;
    @SerializedName("calendar_id")
    @Expose
    private String calendarId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public Timeslot() {
    }

    /**
     * @param id
     * @param timeEnd
     * @param timeStart
     */
    public Timeslot(String id, String timeStart, String timeEnd) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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

    public Timeslot withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return The timeStart
     */
    public String getTimeStart() {
        return timeStart;
    }

    /**
     * @param timeStart The time_start
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public Timeslot withTimeStart(String timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    /**
     * @return The timeEnd
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * @param timeEnd The time_end
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Timeslot withTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    /**
     * @return The calendarId
     */
    public String getCalendarId() {
        return calendarId;
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


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(timeStart).append(timeEnd).append(calendarId).append(createdAt).append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Timeslot) == false) {
            return false;
        }
        Timeslot rhs = ((Timeslot) other);
        return new EqualsBuilder().append(id, rhs.id).append(timeStart, rhs.timeStart).append(timeEnd, rhs.timeEnd).append(calendarId, rhs.calendarId).append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).isEquals();
    }

    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
        return null;
    }

    @Override
    public Subscription delete(Subscriber subscriber) {
        return null;
    }

    @Override
    public Subscription save(Subscriber subscriber) {
        return null;
    }

    @Override
    public Subscription get(Subscriber subscriber, String... parameters) {
        return null;
    }

    @Override
    public Subscription getList(Subscriber subscriber, String... parameters) {
        if (parameters.length != 1)
            subscriber.onError(new ModelException("Get parameters number is not correct (!= 1)"));

        return Retrofit.subscribeList(Retrofit.createRetrofit(RestInterface.class).index(parameters[0]),
                new ListSubscriber<Timeslot>(subscriber, this));
    }

    static private interface RestInterface {
        @GET("/employees/calendars/{calendar}/timeslots/?current=1")
        Observable<List<Timeslot>> index(@Path("calendar") String calendarId);
    }
}
