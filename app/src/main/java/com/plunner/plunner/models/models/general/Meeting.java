
package com.plunner.plunner.models.models.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.models.Listable;
import com.plunner.plunner.models.models.Model;
import com.plunner.plunner.models.models.ModelList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
abstract public class Meeting<S extends Meeting> extends Model<S> implements Listable {

    @SerializedName("id")
    @Expose
    protected String id;
    @SerializedName("title")
    @Expose
    protected String title;
    @SerializedName("description")
    @Expose
    protected String description;
    @SerializedName("group_id")
    @Expose
    protected String groupId;
    @SerializedName("start_time")
    @Expose
    protected Object startTime;
    @SerializedName("duration")
    @Expose
    protected String duration;
    @SerializedName("created_at")
    @Expose
    protected String createdAt;
    @SerializedName("updated_at")
    @Expose
    protected String updatedAt;
    protected ModelList<? extends MeetingTimeslot> timeslots;

    /**
     * No args constructor for use in serialization
     */
    public Meeting() {
    }

    /**
     * @param startTime
     * @param id
     * @param duration
     * @param title
     * @param description
     */
    public Meeting(String id, String title, String description, Object startTime, String duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
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
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Get timeslots if they are <strong>already loaded</strong> via loadTimeslots
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public ModelList<? extends MeetingTimeslot> getTimeslots() throws CloneNotSupportedException {
        return timeslots.clone();
    }

    /**
     * @return The groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return The startTime
     */
    public Object getStartTime() {
        return startTime;
    }

    /**
     * @param startTime The start_time
     */
    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    /**
     * @return The duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration The duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
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

    public Meeting withId(String id) {
        this.id = id;
        return this;
    }

    public Meeting withTitle(String title) {
        this.title = title;
        return this;
    }

    public Meeting withDescription(String description) {
        this.description = description;
        return this;
    }

    public Meeting withStartTime(Object startTime) {
        this.startTime = startTime;
        return this;
    }

    public Meeting withDuration(String duration) {
        this.duration = duration;
        return this;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(title).append(description).append(groupId).
                append(startTime).append(duration).append(createdAt).append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Meeting) == false) {
            return false;
        }
        Meeting rhs = ((Meeting) other);
        return new EqualsBuilder().append(id, rhs.id).append(title, rhs.title).
                append(description, rhs.description).append(groupId, rhs.groupId).
                append(startTime, rhs.startTime).append(duration, rhs.duration).
                append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).isEquals();
    }

    //TODO abstract load timeslots
}
