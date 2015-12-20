package com.plunner.plunner;

/**
 * Created by giorgiopea on 20/12/15.
 */
public class Meeting {

    private String title;
    private String desc;

    public Meeting(String title, String description){
        this.title = title;
        this.desc = description;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
