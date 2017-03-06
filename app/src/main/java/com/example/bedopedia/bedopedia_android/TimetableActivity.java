package com.example.bedopedia.bedopedia_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import Adapters.TimetableAdapter;
import Models.TimetableSlot;
import Services.ApiClient;
import Services.ApiInterface;
import Tools.Dialogue;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by khaled on 3/1/17.
 */

public class TimetableActivity extends AppCompatActivity {

    private TimetableAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String studentId;
    ProgressDialog progress;
    public static List<TimetableSlot> todaySlots;
    public static List<TimetableSlot> tomorrowSlots;

    public static Context context;

    public void loading(){
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
    }

    private class TimetableAsyncTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading();
            progress.show();
        }

        protected void onProgressUpdate(String... progress) {
            loading();
        }

        @Override
        protected List<TimetableSlot> doInBackground(Object... param) {

            SharedPreferences sharedPreferences = getSharedPreferences("cur_user", MODE_PRIVATE);
            ApiInterface apiService = ApiClient.getClient(sharedPreferences).create(ApiInterface.class);
            String url = "api/students/" + studentId + "/timetable";
            Map<String, String> params = new HashMap<>();
            Call<ArrayList<JsonObject> > call = apiService.getServiseArr(url, params);

            call.enqueue(new Callback<ArrayList<JsonObject> >() {

                @Override
                public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                    progress.dismiss();
                    int statusCode = response.code();
                    if(statusCode == 401) {
                        Dialogue.AlertDialog(context,"Not Authorized","you don't have the right to do this");
                    } else if (statusCode == 200) {
                        Calendar calendar = Calendar.getInstance();
                        Date date = calendar.getTime();
                        String today = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                        calendar.add( Calendar.DATE, 1 );
                        date = calendar.getTime();
                        String tomorrow = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
                        today = today.toLowerCase();
                        tomorrow = tomorrow.toLowerCase();
                        if (today.equals("thursday")){
                            tomorrow = "sunday";
                        }

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        formatter.setTimeZone(TimeZone.getTimeZone("Egypt"));
                        for(int i = 0 ; i < response.body().size() ; i++){
                            JsonObject slot = response.body().get(i);
                            String from = slot.get("from").getAsString();
                            String to = slot.get("to").getAsString();
                            if(from.indexOf('.') != -1)
                                from = from.substring(0,from.indexOf('.')) + 'Z' ;
                            if(to.indexOf('.') != -1)
                                to = to.substring(0,to.indexOf('.')) + 'Z' ;
                            String day = slot.get("day").getAsString();
                            String courseName = slot.get("course_name").getAsString();
                            String classRoom = slot.get("school_unit").getAsString();

                            Date fromDate = null;
                            Date toDate = null;
                            try {

                                fromDate = formatter.parse(from.replaceAll("Z$", "+0000"));
                                toDate = formatter.parse(to.replaceAll("Z$", "+0000"));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (day.equals(today)){
                                todaySlots.add(new TimetableSlot(fromDate, toDate, day, courseName, classRoom));
                            } else if (day.equals(tomorrow)) {
                                tomorrowSlots.add(new TimetableSlot(fromDate, toDate, day, courseName, classRoom));
                            }

                        }

                    }
                    mSectionsPagerAdapter = new TimetableAdapter(getSupportFragmentManager());

                    mViewPager = (ViewPager) findViewById(R.id.timetable_container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.timetable_tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                }

                @Override
                public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                    progress.dismiss();
                    Dialogue.AlertDialog(context,"Connection Failed","Check your Netwotk connection and Try again");

                    mSectionsPagerAdapter = new TimetableAdapter(getSupportFragmentManager());

                    mViewPager = (ViewPager) findViewById(R.id.timetable_container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.timetable_tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                }
            });
            return null;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);
        context = this;
        todaySlots = new ArrayList<TimetableSlot>();
        tomorrowSlots = new ArrayList<TimetableSlot>();
        progress = new ProgressDialog(this);
        Bundle extras= getIntent().getExtras();
        studentId = extras.getString("student_id");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        TextView actionBarTitle = (TextView) findViewById(R.id.action_bar_title);
        actionBarTitle.setText("Timetable");
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        new TimetableAsyncTask().execute();




    }


}
