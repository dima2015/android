package com.plunner.plunner.utils;

import com.plunner.plunner.models.models.employee.Employee;

/**
 * Created by giorgiopea on 25/02/16.
 */
public class GlobalData {

    private static GlobalData instance;

    private Employee userModel;



    public static GlobalData getInstance(){
        if(instance == null){
            instance = new GlobalData();
        }
        return instance;
    }

    public Employee getUserModel() {
        return userModel;
    }

    public void setUserModel(Employee userModel) {
        this.userModel = userModel;
    }
}
