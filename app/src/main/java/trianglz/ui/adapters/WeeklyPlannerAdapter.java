package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.skolera.skolera_android.R;

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
                return context.getResources().getString(R.string.sunday);
            case 1 :
                return context.getResources().getString(R.string.monday);
            case 2 :
                return context.getResources().getString(R.string.tuesday);
            case 3 :
                return context.getResources().getString(R.string.wednesday);
            case 4 :
                return context.getResources().getString(R.string.thursday);
            case 5 :
                return context.getResources().getString(R.string.friday);
            case 6 :
                return context.getResources().getString(R.string.saturday);
        }
        return null;
    }

    public void addFragmentArrayList(ArrayList<DayFragment> fragmentArrayList) {
        this.fragmentArrayList.clear();
        this.fragmentArrayList.addAll(fragmentArrayList);
        notifyDataSetChanged();
    }
}
