package Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;
import com.example.bedopedia.bedopedia_android.TimetableActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import Models.TimetableSlot;

/**
 * Created by khaled on 3/2/17.
 */

public class TodayFragment extends Fragment {

    public static RelativeLayout mLayout;
    public static TextView nowSign;
    public static TextView nowEventView;
    public Activity act;
    public int getInDp(int dimensionInPixel){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionInPixel, act.getResources().getDisplayMetrics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today_fragment, container, false);
        act = getActivity();
        mLayout = (RelativeLayout) rootView.findViewById(R.id.today_event_column);
        nowSign = new TextView(TimetableActivity.context);
        nowEventView = new TextView(TimetableActivity.context);
        displayDailyEvents();
        displayNowTime();

        TextView today7AM = (TextView) rootView.findViewById(R.id.today_7am);
        TextView today8AM = (TextView) rootView.findViewById(R.id.today_8am);
        TextView today9AM = (TextView) rootView.findViewById(R.id.today_9am);
        TextView today10AM = (TextView) rootView.findViewById(R.id.today_10am);
        TextView today11AM = (TextView) rootView.findViewById(R.id.today_11am);
        TextView today12PM = (TextView) rootView.findViewById(R.id.today_12pm);
        TextView today1PM = (TextView) rootView.findViewById(R.id.today_1pm);
        TextView today2PM = (TextView) rootView.findViewById(R.id.today_2pm);
        TextView today3PM = (TextView) rootView.findViewById(R.id.today_3pm);
        TextView today4PM = (TextView) rootView.findViewById(R.id.today_4pm);
        TextView today5PM = (TextView) rootView.findViewById(R.id.today_5pm);
        TextView today6PM = (TextView) rootView.findViewById(R.id.today_6pm);
        TextView today7PM = (TextView) rootView.findViewById(R.id.today_7pm);

        Typeface roboto = Typeface.createFromAsset(TimetableActivity.context.getAssets(), "font/Roboto-Medium.ttf");
        today7AM.setTypeface(roboto);
        today8AM.setTypeface(roboto);
        today9AM.setTypeface(roboto);
        today10AM.setTypeface(roboto);
        today11AM.setTypeface(roboto);
        today12PM.setTypeface(roboto);
        today1PM.setTypeface(roboto);
        today2PM.setTypeface(roboto);
        today3PM.setTypeface(roboto);
        today4PM.setTypeface(roboto);
        today5PM.setTypeface(roboto);
        today6PM.setTypeface(roboto);
        today7PM.setTypeface(roboto);

        return rootView;
    }
    public void updateTimeNow(){


        Date nowDate = new Date();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(nowDate);   // assigns calendar to given date
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        if(hours >= 7 && hours <= 19) {
            int linePosition = (int) (31.0 + ((hours - 7)*60.0) + (minutes/60.0) * 60.0) ;

            mLayout.removeView(nowSign);
            mLayout.removeView(nowEventView);

            RelativeLayout.LayoutParams lParam1 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lParam1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lParam1.topMargin = getInDp(linePosition-5);
            lParam1.leftMargin = 0;
            nowSign.setLayoutParams(lParam1);
            nowSign.setPadding(24, 0, 24, 0);
            nowSign.setHeight(getInDp(11));
            nowSign.setWidth(11);

            int id = R.drawable.now_circle;
            Drawable d = act.getResources().getDrawable(id);
            nowSign.setBackground(d);
            mLayout.addView(nowSign);


            RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lParam.topMargin = getInDp(linePosition);
            lParam.leftMargin = getInDp(10);
            nowEventView.setLayoutParams(lParam);
            nowEventView.setPadding(24, 0, 24, 0);
            nowEventView.setHeight(getInDp(1));
            nowEventView.setWidth(1200);
            nowEventView.setGravity(0x11);
            nowEventView.setBackgroundColor(Color.RED);
            mLayout.addView(nowEventView);


        }
    }
    public void displayNowTime(){

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        updateTimeNow();

                    }
                });
            }
        }, 0, 1000);


    }

    private void displayDailyEvents(){

        for(TimetableSlot eObject : TimetableActivity.todaySlots){
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
        return (hours * 60) + ((minutes * 60) / 100);
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

        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(startTime);   // assigns calendar to given date
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int eventPosition = (int) (31.0 + ((hours - 7)*60.0) + (minutes/60.0) * 60.0) ;

        Typeface roboto = Typeface.createFromAsset(TimetableActivity.context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface roboto1 = Typeface.createFromAsset(TimetableActivity.context.getAssets(), "font/Roboto-Regular.ttf");

        TextView mEventView = new TextView(TimetableActivity.context);
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
        mEventView.setTextSize(16);
        mEventView.setBackgroundColor(Color.parseColor("#33ef6c00"));
        mLayout.addView(mEventView);

        TextView mEventView3 = new TextView(TimetableActivity.context);
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
        mEventView3.setTextSize(12);
        mEventView3.setText(classRoom);
        mEventView3.setBackgroundColor(Color.parseColor("#33ef6c00"));
        mLayout.addView(mEventView3);


        TextView mEventView1 = new TextView(TimetableActivity.context);
        RelativeLayout.LayoutParams lParam1 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam1.topMargin = getInDp(eventPosition);
        lParam1.leftMargin = 0;
        mEventView1.setLayoutParams(lParam1);
        mEventView1.setPadding(24, 0, 24, 0);
        mEventView1.setHeight(getInDp(height));
        mEventView1.setWidth(getInDp(3));
        mEventView1.setGravity(0x11);
        mEventView1.setBackgroundColor(Color.parseColor("#66ef6c00"));
        mLayout.addView(mEventView1);
    }
}
