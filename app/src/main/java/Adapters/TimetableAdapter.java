package Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.AbsentFragment;
import Fragments.ExcusedFragment;
import Fragments.LateFragment;
import Fragments.TodayFragment;
import Fragments.TomorrowFragment;

/**
 * Created by khaled on 3/2/17.
 */

public class TimetableAdapter extends FragmentPagerAdapter {

    public TimetableAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        if(position == 0){
            f = new TodayFragment();
        } else {
            f = new TomorrowFragment();
        }

        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "Today";
        return "Tomorrow";
    }
}
