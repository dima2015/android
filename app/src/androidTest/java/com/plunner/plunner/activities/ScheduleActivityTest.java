package com.plunner.plunner.activities;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.ScheduleActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by giorgiopea on 04/03/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ScheduleActivityTest {

    @Rule
    public ActivityTestRule<ScheduleActivity> mActivityRule = new ActivityTestRule<>(
            ScheduleActivity.class);

    @Test
    public void testErrors(){
        onView(withId(R.id.menu_compose_schedule_send)).perform(click());
        onView(withId(R.id.compose_schedule_schedule_name_error)).check(ViewAssertions.matches(isDisplayed()));
        onView(allOf(withId(android.support.design.R.id.snackbar_text)))
                .check(ViewAssertions.matches(isDisplayed()));
    }
    @Test
    public void testStatusChange(){
        onView(withId(R.id.compose_schedule_enabled_switch)).check(ViewAssertions.matches(isChecked()));
        onView(allOf(withId(R.id.compose_schedule_status), withText("ENABLED"))).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.compose_schedule_enabled_switch)).perform(click());
        onView(withId(R.id.compose_schedule_enabled_switch)).check(ViewAssertions.matches(not(isChecked())));
        onView(allOf(withId(R.id.compose_schedule_status), withText("DISABLED"))).check(ViewAssertions.matches(isDisplayed()));
    }

}