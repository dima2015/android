package com.plunner.plunner.utils;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

/**
 * Created by giorgiopea on 23/02/16.
 */
public class CustomWeekEvent extends WeekViewEvent {

    private boolean isNew;
    private boolean isEdited;

    public CustomWeekEvent(long id, String name, Calendar startTime, Calendar endTime, boolean isNew, boolean isEdited) {
        super(id, name, startTime, endTime);
        this.isNew = isNew;
        this.isEdited = isEdited;
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setIsEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

}
