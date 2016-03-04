package com.plunner.plunner.utils;

import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.Meeting;

/**
 * Created by giorgiopea on 26/02/16.
 *
 */
public class DataExchanger {

    private static DataExchanger instance;

    private Employee user;
    private Calendar schedule;
    private Meeting meeting;

    public static DataExchanger getInstance() {
        if (instance == null) {
            instance = new DataExchanger();
        }
        return instance;
    }

    private DataExchanger() {}

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    public void setSchedule(Calendar schedule) {
        this.schedule = schedule;
    }

    public Calendar getSchedule() {
        return schedule;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
