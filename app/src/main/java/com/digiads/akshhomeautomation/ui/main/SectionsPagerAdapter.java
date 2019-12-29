package com.digiads.akshhomeautomation.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.digiads.akshhomeautomation.R;
import com.digiads.akshhomeautomation.utils.DbUtils;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //@StringRes
   // private static final int[] TAB_TITLES = new int[]{"hello", "hello2"};
    private static final String[] TAB_TITLES = new String[]{"Living Room", "Bed Room"};
    ArrayList<String> arrayList;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        arrayList = new DbUtils(context).getAllRooms();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1,arrayList.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return arrayList.size();
    }
}