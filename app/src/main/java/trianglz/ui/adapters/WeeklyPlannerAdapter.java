package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import trianglz.ui.fragments.DayFragment;

/**
 * This file is spawned by Gemy on 1/16/2019.
 */
public class WeeklyPlannerAdapter extends FragmentPagerAdapter {

    public ArrayList<Fragment> fragmentArrayList;
    public Context context;
    public WeeklyPlannerAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragmentArrayList = new ArrayList<>();
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                return "Sunday";
            case 1 :
                return "Monday";
            case 2 :
                return "Tuesday";
            case 3 :
                return "Wednesday";
            case 4 :
                return "Thursday";
            case 5 :
                return "Friday";
            case 6 :
                return "Saturday";
        }
        return null;
    }

    public void addFragmentArrayList(ArrayList<DayFragment> fragmentArrayList) {
        this.fragmentArrayList.clear();
        this.fragmentArrayList.addAll(fragmentArrayList);
        notifyDataSetChanged();
    }
}
