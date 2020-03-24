package trianglz.ui.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * This file is spawned by Gemy on 1/16/2019.
 */
public class WeeklyPlannerAdapter extends FragmentStatePagerAdapter {

    public ArrayList<Fragment> fragmentArrayList;
    public ArrayList<String> daysNameArrayList;
    public Context context;
    public WeeklyPlannerAdapter(FragmentManager fm, Context context) {
        super(fm);
        fragmentArrayList = new ArrayList<>();
        this.context = context;
        this.daysNameArrayList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position >= 0 &&  position <  daysNameArrayList.size() ){
            return daysNameArrayList.get(position);
        }else {
            return "";
        }

    }

    public void addFragmentArrayList(ArrayList<Fragment> fragmentArrayList,ArrayList<String> daysNameArrayList) {
        this.fragmentArrayList.clear();
        this.fragmentArrayList.addAll(fragmentArrayList);
        this.daysNameArrayList = daysNameArrayList;
        notifyDataSetChanged();
    }
}
