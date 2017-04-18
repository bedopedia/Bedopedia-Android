package timetable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timetable.Fragments.TodayFragment;
import timetable.Fragments.TomorrowFragment;

/**
 * Created by khaled on 3/2/17.
 */

public class TimetableAdapter extends FragmentPagerAdapter {
    List<TimetableSlot> tomorrowSlots;

    public TimetableAdapter(FragmentManager fm,  List<TimetableSlot> tomorrowSlots){
        super(fm);
        this.tomorrowSlots = tomorrowSlots;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment f = null;

        if(position == 0){
            f = new TodayFragment();
        } else {
            f = TomorrowFragment.newInstance(tomorrowSlots);

        }

        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date.getTime());
        String todayNumber =  new SimpleDateFormat("dd", Locale.ENGLISH).format(date.getTime());

        if (today.equals("Thu")){
            calendar.add( Calendar.DATE, 3 );
        } else{
            calendar.add( Calendar.DATE, 1 );
        }

        date = calendar.getTime();
        String tomorrow = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date.getTime());
        String tomorrowNumber =  new SimpleDateFormat("dd", Locale.ENGLISH).format(date.getTime());
        today = today.toLowerCase();
        tomorrow = tomorrow.toLowerCase();

        if(position == 0)
            return todayNumber + " " + today + " - Today";
        return tomorrowNumber + " " + tomorrow + " - Tomorrow";
    }
}
