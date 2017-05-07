package timetable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Tools.CalendarUtils;
import timetable.Fragments.TodayFragment;
import timetable.Fragments.TomorrowFragment;

/**
 * Created by khaled on 3/2/17.
 */

public class TimetableAdapter extends FragmentPagerAdapter {
    List<TimetableSlot> tomorrowSlots;
    List<TimetableSlot> todaySlots;
    final String ThuKey = "Thu";

    public TimetableAdapter(FragmentManager fm,  List<TimetableSlot> tomorrowSlots ,List<TimetableSlot> todaySlots){
        super(fm);
        this.tomorrowSlots = tomorrowSlots;
        this.todaySlots = todaySlots;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment newCreatedFragment = null;

        if(position == 0){
            newCreatedFragment = TodayFragment.newInstance(todaySlots);
        } else {
            newCreatedFragment = TomorrowFragment.newInstance(tomorrowSlots);

        }

        return newCreatedFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Calendar calendar = CalendarUtils.getCalendarWithoutDate();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date.getTime());
        String todayNumber =  new SimpleDateFormat("dd", Locale.ENGLISH).format(date.getTime());

        if (today.equals(ThuKey)){
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
