package com.plunner.plunner.utils;

import android.app.Activity;

import com.plunner.plunner.models.callbacks.interfaces.Callable;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.employee.Calendar;
import com.plunner.plunner.models.models.employee.Employee;
import com.plunner.plunner.models.models.employee.planner.Planner;

/**
 * Created by giorgiopea on 26/02/16.
 */
public class ComManager {

    private static ComManager instance;
    private LoginManager loginManager;

    private Employee user;
    private Calendar exchangeSchedule;

    public static ComManager getInstance(){
        if(instance == null){
            instance = new ComManager();
        }
        return instance;
    }

    public ComManager() {
        this.loginManager = LoginManager.getInstance();
    }

    public void login(Activity activity, LoginManager.storeTokenCallback callback){
        loginManager.storeToken(activity, callback);
    }

    public void retrieveUser(Callable callback){
        (new Employee<>()).getFactory(callback);
    }

    public void retrieveGroups(Callable callback){
        if(user == null){
            throw new NullPointerException("No user reference");
        }
        user.getGroups().load(callback);
    }
    public void retrievePlannedMeetings(Callable callback){
        if(user == null){
            throw new NullPointerException("No user reference");
        }
        user.getMeetings().load(callback);
    }
    public void retrieveSchedules(Callable callback){
        if(user == null){
            throw new NullPointerException("No user reference");
        }
        user.getCalendars().load(callback);
    }
    public void retrieveManagedGroups(Callable callback){
        if(user == null){
            throw new NullPointerException("No user reference");
        }
        ((Planner) user).getGroupsManaged().load(callback);
    }

    public Employee getUser() {
        return user;
    }

    public void setUser(Employee user) {
        this.user = user;
    }

    public boolean isUserPlanner(){
        return user.isPlanner();
    }
    public void setExchangeSchedule(Calendar schedule){
        exchangeSchedule = schedule;
    }
    public Calendar getExchangeSchedule(){
        return exchangeSchedule;
    }
}
