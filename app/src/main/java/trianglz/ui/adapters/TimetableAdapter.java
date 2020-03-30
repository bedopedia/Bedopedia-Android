package trianglz.ui.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.skolera.skolera_android.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Tools.CalendarUtils;
import trianglz.managers.App;
import trianglz.models.TimeTableSlot;
import trianglz.ui.fragments.TodayFragment;
import trianglz.ui.fragments.TomorrowFragment;

/**
 * Created by khaled on 3/2/17.
 */

public class TimetableAdapter extends FragmentPagerAdapter {
    List<TimeTableSlot> tomorrowSlots;
    List<TimeTableSlot> todaySlots;
    final String ThuKey = "Thu";

    public TimetableAdapter(FragmentManager fm, List<TimeTableSlot> tomorrowSlots, List<TimeTableSlot> todaySlots) {
        super(fm);
        this.tomorrowSlots = tomorrowSlots;
        this.todaySlots = todaySlots;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment newCreatedFragment = null;

        if (position == 0) {
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
        String todayNumber = new SimpleDateFormat("dd", Locale.ENGLISH).format(date.getTime());

        if (today.equals(ThuKey)) {
            calendar.add(Calendar.DATE, 3);
        } else {
            calendar.add(Calendar.DATE, 1);
        }

        date = calendar.getTime();
        String tomorrow = new SimpleDateFormat("EEE", Locale.ENGLISH).format(date.getTime());
        String tomorrowNumber = new SimpleDateFormat("dd", Locale.ENGLISH).format(date.getTime());
        today = today.toLowerCase();
        tomorrow = tomorrow.toLowerCase();
        Context baseContext = App.getInstance().getBaseContext();
        if (position == 0)
//            return todayNumber + " " + today + " - Today";
//        return tomorrowNumber + " " + tomorrow + " - Tomorrow";
        {

            return baseContext.getString(R.string.today);
        }
        return baseContext.getString(R.string.tomorrow);
    }
}
