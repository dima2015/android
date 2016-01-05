package com.plunner.plunner.ApplicationView.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.plunner.plunner.ApplicationView.Fragments.MeetingsFragment;
import com.plunner.plunner.ApplicationView.Fragments.SchedulesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 05/01/16.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> fragmentsTitles = new ArrayList<>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsTitles.get(position);
    }

    public void addFragment(Fragment fragment, String fragmentTitle){
        this.fragments.add(fragment);
        this.fragmentsTitles.add(fragmentTitle);
    }
}
