package com.plunner.plunner.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.plunner.plunner.R;
import com.plunner.plunner.activities.activities.DashboardActivity;
import com.plunner.plunner.activities.activities.MeetingActivity;

import io.github.yavski.fabspeeddial.FabSpeedDial;

/**
 * Created by giorgiopea on 04/03/16.
 */
public class MeetingActivityTest extends ActivityInstrumentationTestCase2<MeetingActivity> {

    MeetingActivity activity;

    public MeetingActivityTest() {
        super(MeetingActivity.class);
    }
    @Override
    protected void setUp(){
        activity = getActivity();
    }

    public void testView(){
        TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.dashboard_activity_tab_layout);
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.dashboard_activity_view_pager);
        assertNotNull(tabLayout);
        assertNotNull(viewPager);
    }
}