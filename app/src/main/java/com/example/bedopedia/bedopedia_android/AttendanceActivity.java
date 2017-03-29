package com.example.bedopedia.bedopedia_android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Adapters.AbsentLateAdapter;
import Adapters.AttendanceAdapter;
import Adapters.ExcusedAdapter;
import Models.Attendance;

/**
 * Created by khaled on 2/21/17.
 */


public class AttendanceActivity extends AppCompatActivity {

    private AttendanceAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String attendance;
    TextView selectedMonth;
    TextView lateCounter;
    TextView lateLabel;
    TextView excusedCounter;
    TextView excusedLabel;
    TextView absentCounter;
    TextView absentLabel;
    TextView excusedListCounter;
    TextView excusedListLabel;

    Context context;

    public static List<Attendance> absentDates;
    public static List<Attendance> lateDates;
    public static List<Attendance> excusedDates;

    Typeface robotoMedium;
    Typeface robotoRegular;
    Typeface robotoBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);

        context = this;
        absentDates = new ArrayList<Attendance>();
        lateDates = new ArrayList<Attendance>();
        excusedDates = new ArrayList<Attendance>();

        robotoMedium = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
        robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
        robotoBold = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");

        lateCounter = (TextView) findViewById(R.id.late_count);
        lateLabel = (TextView) findViewById(R.id.late_label);
        excusedCounter = (TextView) findViewById(R.id.excused_count);
        excusedLabel = (TextView) findViewById(R.id.excused_label);
        absentCounter = (TextView) findViewById(R.id.absent_count);
        absentLabel = (TextView) findViewById(R.id.absent_label);

        excusedListCounter = (TextView) findViewById(R.id.excused_list_counter);
        excusedListLabel = (TextView) findViewById(R.id.excused_list_label);

        DateFormatSymbols dfs = new DateFormatSymbols();
        final String[] months = dfs.getMonths();

        selectedMonth = (TextView) findViewById(R.id.selected_month);
        selectedMonth.setText(months[new Date().getMonth()]);

        selectedMonth.setTypeface(robotoMedium);
        lateCounter.setTypeface(robotoMedium);
        lateLabel.setTypeface(robotoRegular);
        excusedCounter.setTypeface(robotoMedium);
        excusedLabel.setTypeface(robotoRegular);
        absentCounter.setTypeface(robotoMedium);
        absentLabel.setTypeface(robotoRegular);

        excusedListCounter.setTypeface(robotoBold);
        excusedListLabel.setTypeface(robotoMedium);

        List<Attendance> dates= new ArrayList<Attendance>();
        for (int i = 0 ; i < excusedDates.size() ; i++){
            if(new Date().getMonth() == excusedDates.get(i).getDate().getMonth()){
                dates.add(excusedDates.get(i));
            }
        }
        ListView absentAttendaceList = (ListView) findViewById(R.id.excused_dates);
        ExcusedAdapter adapter = new ExcusedAdapter(context, R.layout.single_excused_attendance, dates);
        absentAttendaceList.setAdapter(adapter);
        excusedListCounter.setText(dates.size()+"");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Attendance");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
        compactCalendar.setSelected(false);
        
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                selectedMonth.setText(months[firstDayOfNewMonth.getMonth()]);
                List<Attendance> dates= new ArrayList<Attendance>();
                for (int i = 0 ; i < excusedDates.size() ; i++){
                    if(firstDayOfNewMonth.getMonth() == excusedDates.get(i).getDate().getMonth()){
                        dates.add(excusedDates.get(i));
                    }
                }
                ListView absentAttendaceList = (ListView) findViewById(R.id.excused_dates);
                ExcusedAdapter adapter = new ExcusedAdapter(context, R.layout.single_excused_attendance, dates);
                absentAttendaceList.setAdapter(adapter);
                excusedListCounter.setText(dates.size()+"");
            }
        });
        Bundle extras= getIntent().getExtras();

        attendance = extras.getString("attendances");
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(attendance);
        final JsonArray attenobdances = tradeElement.getAsJsonArray();
        lateDates.clear();
        absentDates.clear();
        excusedDates.clear();

        for(JsonElement element: attenobdances){
            JsonObject day = element.getAsJsonObject();
            Date date = new Date();
            date.setTime(day.get("date").getAsLong());

            if(day.get("status").getAsString().equals("late")){
                if(!day.get("comment").toString().equals("null"))
                    lateDates.add(new Attendance(date, day.get("comment").getAsString()));
                else
                    lateDates.add(new Attendance(date, "No Comment"));
            } else if(day.get("status").getAsString().equals("absent")){
                if(!day.get("comment").toString().equals("null"))
                    absentDates.add(new Attendance(date, day.get("comment").getAsString()));
                else
                    absentDates.add(new Attendance(date, "No Comment"));
            } else if (day.get("status").getAsString().equals("excused")){
                if ( !day.get("comment").toString().equals("null"))
                    excusedDates.add(new Attendance(date, day.get("comment").getAsString()));
                else
                    excusedDates.add(new Attendance(date, "No Comment"));
            }
        }

        for(int i = 0 ; i < lateDates.size() ; i++){
            Event event = new Event(Color.parseColor("#ff9013fe"), lateDates.get(i).getDate().getTime(), "Late");
            compactCalendar.addEvent(event);
        }

        for(int i = 0 ; i < excusedDates.size() ; i++){
            Event event = new Event(Color.parseColor("#fff5a623"), excusedDates.get(i).getDate().getTime(), "Excused");
            compactCalendar.addEvent(event);
        }

        for(int i = 0 ; i < absentDates.size() ; i++){
            Event event = new Event(Color.parseColor("#ffff3b30"), absentDates.get(i).getDate().getTime(), "Absent");
            compactCalendar.addEvent(event);
        }

        lateCounter.setText(lateDates.size()+"");
        excusedCounter.setText(excusedDates.size()+"");
        absentCounter.setText(absentDates.size()+"");



    }
}
