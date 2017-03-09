package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import Fragments.AbsentFragment;
import Fragments.ExcusedFragment;
import Fragments.LateFragment;
import Fragments.NegativeFragment;
import Fragments.PositiveFragment;

/**
 * Created by khaled on 3/9/17.
 */

public class BehaviorNotesFragmentAdapter extends FragmentPagerAdapter {
    public BehaviorNotesFragmentAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        if(position == 0){
            f = new PositiveFragment();
        } else {
            f = new NegativeFragment();
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
            return "Positive";
        return "Negative";
    }
}
