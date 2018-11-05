package timetable.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Tools.CalendarUtils;
import trianglz.models.TimeTableSlot;
import trianglz.utils.Constants;

/**
 * Created by khaled on 3/2/17.
 */

public class TomorrowFragment extends Fragment {

    private RelativeLayout mLayout;
    ArrayList<String> mainColors;
    ArrayList<String> headerColors;
    public static final String KEY_NAME = "tomorrowSlots";



    public static Fragment newInstance(List<TimeTableSlot> tomorrowSlots){
        Fragment fragment ;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_TOMORROW, (Serializable) tomorrowSlots);

        fragment = new TomorrowFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public int getInDp(int dimensionInPixel){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tomorrow_fragment, container, false);

        mainColors = new ArrayList<String>();
        headerColors = new ArrayList<String>();

        mainColors.add("#ffffecb3");
        headerColors.add("#ffffe57f");
        mainColors.add("#ffb2ebf2");
        headerColors.add("#ff84ffff");
        mainColors.add("#fff8bbd0");
        headerColors.add("#ffff80ab");
        mainColors.add("#ffe1bee7");
        headerColors.add("#ffea80fc");
        mainColors.add("#ffd1c4e9");
        headerColors.add("#ffb388ff");
        mainColors.add("#ffc5cae9");
        headerColors.add("#ff8c9eff");
        mainColors.add("#ffb3e5fc");
        headerColors.add("#ff80d8ff");
        mainColors.add("#ffb2dfdb");
        headerColors.add("#ffa7ffeb");
        mainColors.add("#ffffccbc");
        headerColors.add("#ffff9e80");
        mainColors.add("#ffffe0b2");
        headerColors.add("#ffffd180");

        mLayout = (RelativeLayout) rootView.findViewById(R.id.tomorrow_event_column);
        List<TimeTableSlot> tomorrowSlots = ( List<TimeTableSlot> ) getArguments().getSerializable(Constants.KEY_TOMORROW);
        displayDailyEvents(tomorrowSlots);

        TextView tomorrow7AM = (TextView) rootView.findViewById(R.id.tomorrow_7am);
        TextView tomorrow8AM = (TextView) rootView.findViewById(R.id.tomorrow_8am);
        TextView tomorrow9AM = (TextView) rootView.findViewById(R.id.tomorrow_9am);
        TextView tomorrow10AM = (TextView) rootView.findViewById(R.id.tomorrow_10am);
        TextView tomorrow11AM = (TextView) rootView.findViewById(R.id.tomorrow_11am);
        TextView tomorrow12PM = (TextView) rootView.findViewById(R.id.tomorrow_12pm);
        TextView tomorrow1PM = (TextView) rootView.findViewById(R.id.tomorrow_1pm);
        TextView tomorrow2PM = (TextView) rootView.findViewById(R.id.tomorrow_2pm);
        TextView tomorrow3PM = (TextView) rootView.findViewById(R.id.tomorrow_3pm);
        TextView tomorrow4PM = (TextView) rootView.findViewById(R.id.tomorrow_4pm);
        TextView tomorrow5PM = (TextView) rootView.findViewById(R.id.tomorrow_5pm);
        TextView tomorrow6PM = (TextView) rootView.findViewById(R.id.tomorrow_6pm);
        TextView tomorrow7PM = (TextView) rootView.findViewById(R.id.tomorrow_7pm);

        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        tomorrow7AM.setTypeface(roboto);
        tomorrow8AM.setTypeface(roboto);
        tomorrow9AM.setTypeface(roboto);
        tomorrow10AM.setTypeface(roboto);
        tomorrow11AM.setTypeface(roboto);
        tomorrow12PM.setTypeface(roboto);
        tomorrow1PM.setTypeface(roboto);
        tomorrow2PM.setTypeface(roboto);
        tomorrow3PM.setTypeface(roboto);
        tomorrow4PM.setTypeface(roboto);
        tomorrow5PM.setTypeface(roboto);
        tomorrow6PM.setTypeface(roboto);
        tomorrow7PM.setTypeface(roboto);

        return rootView;
    }


    private void displayDailyEvents( List<TimeTableSlot> tomorrowSlots){

        for(TimeTableSlot eObject : tomorrowSlots){
            Date eventDate = eObject.getFrom();
            Date endDate = eObject.getTo();
            String courseName = eObject.getCourseName();
            String classRoom = eObject.getClassRoom();
            int eventBlockHeight = getEventTimeFrame(eventDate, endDate);
            displayEventSection(eventDate, eventBlockHeight, courseName, classRoom, eventDate);
        }
    }

    private int getEventTimeFrame(Date start, Date end){
        long timeDifference = end.getTime() - start.getTime();
        int hours = (int) timeDifference / (60 * 60 * 1000);
        int minutes = (int) timeDifference / (60 * 1000) % 60;
        return (hours * 60) + minutes ;
    }

    private void displayEventSection(Date eventDate, int height, String courseName, String classRoom, Date startTime){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String displayValue = timeFormatter.format(eventDate);
        String[]hourMinutes = displayValue.split(":");
        int hours = Integer.parseInt(hourMinutes[0]);
        int minutes = Integer.parseInt(hourMinutes[1]);
        int topViewMargin = (hours * 60) + ((minutes * 60) / 100);
        createEventView(topViewMargin, height, courseName, classRoom, startTime);
    }

    private void createEventView(int topMargin, int height, String courseName, String classRoom, Date startTime){

        Calendar calendar = CalendarUtils.getGregorianCalendar(startTime);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int eventPosition = (int) (31.0 + ((hours - 7)*60.0) + (minutes/60.0) * 60.0) ;

        Typeface roboto = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Medium.ttf");
        Typeface roboto1 = Typeface.createFromAsset(getActivity().getAssets(), "font/Roboto-Regular.ttf");

        int randomNumber = (int) (Math.random()*10);

        TextView mEventView = new TextView(getActivity());
        RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam.topMargin = getInDp(eventPosition);
        lParam.leftMargin = getInDp(3);
        mEventView.setLayoutParams(lParam);
        mEventView.setPadding(24, 8, 24, 0);
        mEventView.setHeight(getInDp(height/2));
        mEventView.setWidth(1200);
        mEventView.setTextColor(Color.parseColor("#000000"));
        mEventView.setText(courseName);
        mEventView.setTypeface(roboto);
        mEventView.setTextSize(14);
        mEventView.setBackgroundColor(Color.parseColor(mainColors.get(randomNumber)));
        mLayout.addView(mEventView);

        TextView mEventView3 = new TextView(getActivity());
        RelativeLayout.LayoutParams lParam3 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam3.topMargin = getInDp(eventPosition+(height/2));
        lParam3.leftMargin = getInDp(3);
        mEventView3.setLayoutParams(lParam3);
        mEventView3.setPadding(24,8, 24, 16);
        mEventView3.setHeight(getInDp(height/2));
        mEventView3.setWidth(1200);
        mEventView3.setTextColor(Color.parseColor("#000000"));
        mEventView3.setTypeface(roboto1);
        mEventView3.setTextSize(10);
        mEventView3.setText(classRoom);
        mEventView3.setBackgroundColor(Color.parseColor(mainColors.get(randomNumber)));
        mLayout.addView(mEventView3);


        TextView mEventView1 = new TextView(getActivity());
        RelativeLayout.LayoutParams lParam1 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam1.topMargin = getInDp(eventPosition);
        lParam1.leftMargin = 0;
        mEventView1.setLayoutParams(lParam1);
        mEventView1.setPadding(24, 0, 24, 0);
        mEventView1.setHeight(getInDp(height));
        mEventView1.setWidth(getInDp(3));
        mEventView1.setGravity(0x11);
        mEventView1.setBackgroundColor(Color.parseColor(headerColors.get(randomNumber)));
        mLayout.addView(mEventView1);
    }
}
