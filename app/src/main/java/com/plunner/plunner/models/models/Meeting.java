
package com.plunner.plunner.models.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.Subscriber;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

import rx.Subscription;

@Generated("org.jsonschema2pojo")
public class Meeting extends Model {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("start_time")
    @Expose
    private Object startTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public Meeting() {
    }

    /**
     * @param updatedAt
     * @param startTime
     * @param id
     * @param groupId
     * @param duration
     * @param title
     * @param createdAt
     * @param description
     */
    public Meeting(String id, String title, String description, String groupId, Object startTime, String duration, String createdAt, String updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.groupId = groupId;
        this.startTime = startTime;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Meeting withId(String id) {
        this.id = id;
        return this;
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

    public Meeting withTitle(String title) {
        this.title = title;
        return this;
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

    public Meeting withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @return The groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId The group_id
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Meeting withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
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

    public Meeting withStartTime(Object startTime) {
        this.startTime = startTime;
        return this;
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

    public Meeting withDuration(String duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Meeting withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Meeting withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(title).append(description).append(groupId).append(startTime).append(duration).append(createdAt).append(updatedAt).toHashCode();
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
        return new EqualsBuilder().append(id, rhs.id).append(title, rhs.title).append(description, rhs.description).append(groupId, rhs.groupId).append(startTime, rhs.startTime).append(duration, rhs.duration).append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).isEquals();
    }

    @Override
    public Subscription fresh(Subscriber subscriber) {
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
}
