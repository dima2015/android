
package com.plunner.plunner.models.models.employee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plunner.plunner.models.adapters.Subscriber;
import com.plunner.plunner.models.models.Model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;

import rx.Subscription;

@Generated("org.jsonschema2pojo")
final public class Caldav extends Model<Caldav> {

    @SerializedName("calendar_id")
    @Expose
    private String calendarId;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("calendar_name")
    @Expose
    private String calendarName;
    @SerializedName("sync_errors")
    @Expose
    private String syncErrors;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public Caldav() {
    }

    /**
     * @param username
     * @param calendarId
     * @param calendarName
     * @param url
     */
    public Caldav(String calendarId, String url, String username, String calendarName) {
        this.calendarId = calendarId;
        this.url = url;
        this.username = username;
        this.calendarName = calendarName;
    }

    /**
     * @return The calendarId
     */
    public String getCalendarId() {
        return calendarId;
    }

    /**
     * @param calendarId The calendar_id
     */
    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public Caldav withCalendarId(String calendarId) {
        this.calendarId = calendarId;
        return this;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public Caldav withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public Caldav withUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * @return The calendarName
     */
    public String getCalendarName() {
        return calendarName;
    }

    /**
     * @param calendarName The calendar_name
     */
    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Caldav withCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    /**
     * @return The syncErrors
     */
    public String getSyncErrors() {
        return syncErrors;
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
        return new HashCodeBuilder().append(calendarId).append(url).append(username).append(calendarName).append(syncErrors).append(createdAt).append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Caldav) == false) {
            return false;
        }
        Caldav rhs = ((Caldav) other);
        return new EqualsBuilder().append(calendarId, rhs.calendarId).append(url, rhs.url).append(username, rhs.username).append(calendarName, rhs.calendarName).append(syncErrors, rhs.syncErrors).append(createdAt, rhs.createdAt).append(updatedAt, rhs.updatedAt).isEquals();
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
}
