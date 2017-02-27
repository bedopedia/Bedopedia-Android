package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Date;
import java.util.List;

import Fragments.AbsentFragment;
import Fragments.ExcusedFragment;
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

       if(position == 0){
           f = new LateFragment();
       } else if (position == 1){
           f = new AbsentFragment();
       } else {
           f = new ExcusedFragment();
       }

       return f;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Late";
        } else if (position == 1){
            return "Absent";
        }
        return "Excused";
    }
}
