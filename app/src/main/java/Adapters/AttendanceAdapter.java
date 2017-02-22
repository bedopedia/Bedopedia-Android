package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.AbsentFragment;
import Fragments.LateFragment;

/**
 * Created by khaled on 2/21/17.
 */

public class AttendanceAdapter extends FragmentPagerAdapter {

    public AttendanceAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       Fragment f = null;

       if(position == 1){
           f = new AbsentFragment();
       } else {
           f = new LateFragment();
       }

       return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 1){
            return "Absent";
        }
        return "Late";
    }
}
