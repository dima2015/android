package com.plunner.plunner.activities;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.SettingsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by giorgiopea on 04/03/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingsActivityTest {

    @Rule
    public ActivityTestRule<SettingsActivity> activityRule = new ActivityTestRule<>(
            SettingsActivity.class);

    @Test
    public void viewPresence(){
        onView(withId(R.id.activity_user_settings_email)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.activity_user_settings_name)).check(ViewAssertions.matches(isDisplayed()));
        onView(allOf(withId(R.id.activity_user_settings_pwd), withText("Password"))).check(ViewAssertions.matches(isDisplayed()));
    }

}
