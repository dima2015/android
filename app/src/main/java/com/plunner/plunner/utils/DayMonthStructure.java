package com.plunner.plunner.utils;

/**
 * Created by giorgiopea on 02/02/16.
 */
public class DayMonthStructure {


    private String value;
    private boolean isCurrent;

    public DayMonthStructure(String value, boolean isCurrent){
        this.value = value;
        this.isCurrent = isCurrent;
    }

    public String getValue(){
        return this.value;
    }

    public boolean getIsCurrent(){
        return this.isCurrent;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }



}
