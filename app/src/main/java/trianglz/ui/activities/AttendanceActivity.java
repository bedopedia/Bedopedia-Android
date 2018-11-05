package trianglz.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private CompactCalendarView compactCalendarView;
    private ArrayList<Attendance> absentDates,lateDates,excusedDates,presentDates;
    private JSONArray attendanceJsonArray;
    private RecyclerView recyclerView;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private Student student;
    private TextView monthYearTextView,lateCounterTextView,excusedCounterTextView,absentCounterTextView;
    DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
    final String[] months = dateFormatSymbols.getMonths();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        bindViews();
        getValueFromIntent();
        setRecyclerView();
        setStudentImage(student.getAvatar(),student.firstName +" " +student.lastName);
        final Calendar calendar = Calendar.getInstance();
        compactCalendarView.addEvent(new Event(R.color.jade_green,calendar.getTime().getTime()));
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                if(dateClicked.getYear() == calendar.getTime().getYear()
                        && dateClicked.getMonth() == calendar.getTime().getMonth()
                        && dateClicked.getDay() == calendar.getTime().getDay()){
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.jade_green));
                }else {
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthYearTextView.setText(months[firstDayOfNewMonth.getMonth()]);

            }
        });
        compactCalendarView.removeAllEvents();
        createEvents(excusedDates,R.color.squash);
        createEvents(absentDates,R.color.orange_red);
        createEvents(lateDates,R.color.vivid_purple);
        createEvents(presentDates,R.color.jade_green);
        lateCounterTextView.setText(lateDates.size()+"");
        absentCounterTextView.setText(absentDates.size()+"");
        excusedCounterTextView.setText(excusedDates.size()+"");
    }

    private void bindViews(){
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        absentDates = new ArrayList<>();
        lateDates = new ArrayList<>();
        excusedDates = new ArrayList<>();
        presentDates = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        monthYearTextView = findViewById(R.id.tv_month_year_header);
        lateCounterTextView = findViewById(R.id.tv_late_counter);
        excusedCounterTextView = findViewById(R.id.tv_excused_counter);
        absentCounterTextView = findViewById(R.id.tv_absent_counter);
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
            }else if (day.optString(Constants.KEY_STATUS).equals("present")){
                if ( !day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                    presentDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                else
                    presentDates.add(new Attendance(date, Constants.KEY_NO_COMMENT));
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


    private void createEvents(ArrayList<Attendance> attendanceArrayList, int color){
        for(int i = 0; i<attendanceArrayList.size(); i++){
            compactCalendarView.addEvent(new Event(getResources().getColor(color),attendanceArrayList.get(i).getDate().getTime()));
        }
    }

}
