package trianglz.ui.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CalendarView;

import com.fasterxml.jackson.databind.util.EnumValues;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import attendance.Attendance;
import attendance.ExcusedAdapter;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.Student;
import trianglz.utils.Constants;

public class AttendanceActivity extends AppCompatActivity {
    String TAG = "testttststts";
    private CompactCalendarView compactCalendarView;
    private ArrayList<Attendance> absentDates,lateDates,excusedDates;
    private JSONArray attendanceJsonArray;
    private RecyclerView recyclerView;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private Student student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        bindViews();
        getValueFromIntent();
        setRecyclerView();
        setStudentImage(student.getAvatar(),student.firstName +" " +student.lastName);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {


            }
        });


    }

    private void bindViews(){
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        absentDates = new ArrayList<>();
        lateDates = new ArrayList<>();
        excusedDates = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);

    }

    private void getValueFromIntent() {
        String attendance = getIntent().getBundleExtra(Constants.KEY_BUNDLE).getString(Constants.KEY_ATTENDANCE);
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
        try {
            attendanceJsonArray = new JSONArray(attendance);
            setAttendance();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAttendance() {
        for(int i = 0; i< attendanceJsonArray.length(); i++){
            JSONObject day = attendanceJsonArray.optJSONObject(i);
            Date date = new Date();
            date.setTime(day.optLong(Constants.KEY_DATE));
            if(day.optString(Constants.KEY_STATUS).equals(Constants.KEY_LATE)){
                if(!day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                    lateDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                else
                    lateDates.add(new Attendance(date, Constants.KEY_NO_COMMENT));
            } else if(day.optString(Constants.KEY_STATUS).equals(Constants.KEY_ABSENT)){
                if(!day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                    absentDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                else
                    absentDates.add(new Attendance(date, Constants.KEY_NO_COMMENT));
            } else if (day.optString(Constants.KEY_STATUS).equals(Constants.KEY_EXCUSED)){
                if ( !day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                    excusedDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                else
                    excusedDates.add(new Attendance(date, Constants.KEY_NO_COMMENT));
            }
        }

    }

    private void setRecyclerView() {
        List<Attendance> dates= new ArrayList<Attendance>();
        for (int i = 0 ; i < excusedDates.size() ; i++){
            if(new Date().getMonth() == excusedDates.get(i).getDate().getMonth() &&
                    new Date().getYear() == excusedDates.get(i).getDate().getYear()){
                dates.add(excusedDates.get(i));
            }
        }
        ExcusedAdapter adapter = new ExcusedAdapter(this, R.layout.single_excused_attendance, dates);
        recyclerView.setAdapter(adapter);
    }



    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            Picasso.with(this)
                    .load(imageUrl)
                    .fit()
                    .transform(new CircleTransform())
                    .into(studentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageLoader = new PicassoLoader();
                            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
                        }
                    });
        }
    }

}
