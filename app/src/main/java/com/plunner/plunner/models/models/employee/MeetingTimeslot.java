
package com.plunner.plunner.models.models.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.Listable;
import com.plunner.plunner.models.models.Model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

import rx.Subscription;

@Generated("org.jsonschema2pojo")
public class MeetingTimeslot<S extends MeetingTimeslot> extends Model<S> implements Listable {

    @SerializedName("id")
    @Expose
    protected String id;
    @SerializedName("time_start")
    @Expose
    protected String timeStart;
    @SerializedName("time_end")
    @Expose
    protected String timeEnd;
    @SerializedName("meeting_id")
    @Expose
    protected String meetingId;
    @SerializedName("created_at")
    @Expose
    protected String createdAt;
    @SerializedName("updated_at")
    @Expose
    protected String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public MeetingTimeslot() {
    }

    /**
     * @param id
     * @param timeEnd
     * @param timeStart
     */
    public MeetingTimeslot(String id, String timeStart, String timeEnd) {
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

    public MeetingTimeslot withId(String id) {
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

    public MeetingTimeslot withTimeStart(String timeStart) {
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

    public MeetingTimeslot withTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
        return this;
    }

    /**
     * @return The meetingId
     */
    public String getMeetingId() {
        return meetingId;
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
        return new HashCodeBuilder().append(id).append(timeStart).append(timeEnd).append(meetingId).append(createdAt).append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MeetingTimeslot) == false) {
            return false;
        }
        MeetingTimeslot rhs = ((MeetingTimeslot) other);
        return new EqualsBuilder().append(id, rhs.id).append(timeStart, rhs.timeStart).append(timeEnd, rhs.timeEnd).append(meetingId, rhs.meetingId).append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).isEquals();
    }

    @Override
    public Subscription fresh(FreshSubscriber subscriber) {
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
        return null;
    }
}
