package com.plunner.plunner.utils;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

/**
 * Extension of {@link WeekViewEvent}
 * @see WeekViewEvent
 * @author Giorgio Pea
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

}
