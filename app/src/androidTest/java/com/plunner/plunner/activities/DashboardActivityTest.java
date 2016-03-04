package com.plunner.plunner.activities;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by giorgiopea on 04/03/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> mActivityRule = new ActivityTestRule<>(
            DashboardActivity.class);

    @Test
    public void viewPresence(){
        onView(withId(R.id.dashboard_activity_fab)).check(ViewAssertions.matches(isDisplayed()));
    }


}
