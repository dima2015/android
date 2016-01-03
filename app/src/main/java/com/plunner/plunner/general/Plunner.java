package com.plunner.plunner.general;

import android.app.Application;
import android.content.Context;

/**
 * Created by claudio on 03/01/16.
 */
public class Plunner extends Application {
    private static Context context;

    public static Context getAppContext() {
        return Plunner.context;
    }

    public void onCreate() {
        super.onCreate();
        Plunner.context = getApplicationContext();
    }
}

