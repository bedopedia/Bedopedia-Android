package trianglz.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.skolera.skolera_android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import attendance.Attendance;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.BottomItemDecoration;
import trianglz.components.CircleTransform;
import trianglz.models.RootClass;
import trianglz.models.Student;
import trianglz.ui.adapters.AttendanceAdapter;
import trianglz.utils.Constants;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener {
    private CompactCalendarView compactCalendarView;
//  private ArrayList<Attendance> absentDates, lateDates, excusedDates, presentDates;

    private Student student;
    private TextView monthYearTextView, allCounterTextView, academicCounterTextView, eventsCounterTextView, vacationsCounterTextView, personalCounterTextView;
    private ImageButton backBtn;
    private Button createPersonalEventBtn;
    private RecyclerView recyclerView;
    private Calendar calendar;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private LinearLayout allLayout, academicLayout, eventsLayout, vacationsLayout, personalLayout;
    private View allView, academicView, eventsView, vacationsView,personalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getValueFromIntent();
        //String studentName = student.firstName + " " + student.lastName;
       // setStudentImage(student.getAvatar(), studentName);
        bindViews();
        setListeners();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.create_personal_event:
                Intent intent = new Intent(this, CreatePersonalEventActivity.class);
                //intent.putExtra(Constants.KEY_STUDENT_ID, student.getId());
                startActivity(intent);
                break;
            case R.id.layout_all:
                deselectAll();
                allView.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_academic:
                deselectAll();
                academicView.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_events:
                deselectAll();
                eventsView.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_vacations:
                deselectAll();
                vacationsView.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_personal:
                deselectAll();
                personalView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDayClick(Date dateClicked) {

    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        monthYearTextView.setText(setHeaderDate(firstDayOfNewMonth));
        if (firstDayOfNewMonth.getYear() == calendar.getTime().getYear()
                && firstDayOfNewMonth.getMonth() == calendar.getTime().getMonth()
                && firstDayOfNewMonth.getDay() == calendar.getTime().getDay()) {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.turquoise_blue_two));
        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    private void bindViews() {
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), new Locale("en"));
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomItemDecoration(66,false));
        imageLoader = new PicassoLoader();
        studentImageView = findViewById(R.id.img_student);
        monthYearTextView = findViewById(R.id.tv_month_year_header);
        allCounterTextView = findViewById(R.id.tv_all_counter);
        academicCounterTextView = findViewById(R.id.tv_academic_counter);
        eventsCounterTextView = findViewById(R.id.tv_events_counter);
        vacationsCounterTextView = findViewById(R.id.tv_vacations_counter);
        personalCounterTextView = findViewById(R.id.tv_personal_counter);
        backBtn = findViewById(R.id.btn_back);
        createPersonalEventBtn = findViewById(R.id.create_personal_event);

        calendar = Calendar.getInstance();
//        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.removeAllEvents();

        allLayout = findViewById(R.id.layout_all);
        academicLayout = findViewById(R.id.layout_academic);
        eventsLayout = findViewById(R.id.layout_events);
        vacationsLayout = findViewById(R.id.layout_vacations);
        personalLayout = findViewById(R.id.layout_personal);

        allView = findViewById(R.id.view_all);
        academicView = findViewById(R.id.view_academic);
        eventsView = findViewById(R.id.view_events);
        vacationsView = findViewById(R.id.view_vacations);
        personalView = findViewById(R.id.view_personal);
        monthYearTextView.setText(setHeaderDate(Calendar.getInstance().getTime()));
    }
    private void getValueFromIntent(){
        student = (Student)getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
    }
    private void setListeners() {
        backBtn.setOnClickListener(this);
        allLayout.setOnClickListener(this);
        academicLayout.setOnClickListener(this);
        eventsLayout.setOnClickListener(this);
        vacationsLayout.setOnClickListener(this);
        personalLayout.setOnClickListener(this);
        compactCalendarView.setListener(this);
        createPersonalEventBtn.setOnClickListener(this);
    }
    private String setHeaderDate(Date date) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(new Locale("en"));
        String[] months = dateFormatSymbols.getMonths();
        String monthString = months[date.getMonth()];
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy", new Locale("en"));
        String yearDate = fmt.format(date);
        return monthString + " " + yearDate;
    }

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(this)
                    .load(imageUrl)
                    .fit()
                    .noPlaceholder()
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
    private void deselectAll() {
        allView.setVisibility(View.INVISIBLE);
        academicView.setVisibility(View.INVISIBLE);
        eventsView.setVisibility(View.INVISIBLE);
        vacationsView.setVisibility(View.INVISIBLE);
        personalView.setVisibility(View.INVISIBLE);

    }
}
