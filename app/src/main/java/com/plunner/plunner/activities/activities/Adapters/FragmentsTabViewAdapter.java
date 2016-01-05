package com.plunner.plunner.activities.activities.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giorgiopea on 05/01/16.
 */
public class FragmentsTabViewAdapter extends FragmentPagerAdapter {
    /**
     * Holds the fragments that corresponds to different tab views
     */
    private final List<Fragment> fragments = new ArrayList<>();
    /**
     * Holds the titles of the different tab views (the ones visible in the tabs)
     */
    private final List<String> fragmentsTitles = new ArrayList<>();

    public FragmentsTabViewAdapter(FragmentManager fm) {
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
