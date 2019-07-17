package trianglz.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudentMainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragment_list;

    public StudentMainPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragment_list = new ArrayList<>();
        fragment_list.addAll(fragments);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return fragment_list.get(0);
        else if (position == 1)
            return fragment_list.get(1);
        else if (position == 2)
            return fragment_list.get(2);
        else if (position == 3)
            return fragment_list.get(3);
        else
            return fragment_list.get(4);

    }

    @Override
    public int getCount() {
        return fragment_list.size();
    }
}
