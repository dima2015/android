package com.plunner.plunner.activities;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.plunner.plunner.activities.activities.ScheduleActivity;
import com.plunner.plunner.activities.activities.SettingsActivity;
import com.plunner.plunner.models.login.LoginException;
import com.plunner.plunner.models.login.LoginManager;
import com.plunner.plunner.models.models.employee.Employee;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by giorgiopea on 04/03/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> activityRule =  new ActivityTestRule<SettingsActivity>(SettingsActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, SettingsActivity.class);
            result.putExtra("TestingFlag", true);
            return result;
        }
    };


}
