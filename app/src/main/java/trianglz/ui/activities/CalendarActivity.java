package trianglz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.BottomItemDecoration;
import trianglz.components.CircleTransform;
import trianglz.models.Student;
import trianglz.ui.adapters.EventAdapter;
import trianglz.utils.Constants;

public class CalendarActivity extends SuperActivity implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener {
    private CompactCalendarView compactCalendarView;
    private ArrayList<EventAdapter.DemoEvent> allEvents, academicEvents, events, personalEvents, vacations;
    private Student student;
    private TextView monthYearTextView, allCounterTextView, academicCounterTextView, eventsCounterTextView, vacationsCounterTextView, personalCounterTextView;
    private ImageButton backBtn;
    private Button createPersonalEventBtn;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private Calendar calendar;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private LinearLayout allLayout, academicLayout, eventsLayout, vacationsLayout, personalLayout;
    private View allView, academicView, eventsView, vacationsView, personalView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getValueFromIntent();
        bindViews();
        setListeners();
        String studentName = student.firstName + " " + student.lastName;
        setStudentImage(student.getAvatar(), studentName);
        setRecyclerView();
        setEvents();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.create_personal_event:
                openAddEventActivity();
                break;
            case R.id.layout_all:
                deselectAll();
                allView.setVisibility(View.VISIBLE);
                eventAdapter.addData(allEvents, EventAdapter.EVENTSTATE.ALL);
                break;
            case R.id.layout_academic:
                deselectAll();
                academicView.setVisibility(View.VISIBLE);
                eventAdapter.addData(academicEvents, EventAdapter.EVENTSTATE.ACADEMIC);
                break;
            case R.id.layout_events:
                deselectAll();
                eventsView.setVisibility(View.VISIBLE);
                eventAdapter.addData(events, EventAdapter.EVENTSTATE.EVENTS);
                break;
            case R.id.layout_vacations:
                deselectAll();
                vacationsView.setVisibility(View.VISIBLE);
                eventAdapter.addData(vacations, EventAdapter.EVENTSTATE.VACATIONS);
                break;
            case R.id.layout_personal:
                deselectAll();
                personalView.setVisibility(View.VISIBLE);
                eventAdapter.addData(personalEvents, EventAdapter.EVENTSTATE.PERSONAL);
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
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.removeAllEvents();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomItemDecoration(66, false));
        eventAdapter = new EventAdapter(this, EventAdapter.EVENTSTATE.ALL);
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

    private void getValueFromIntent() {
        student = (Student) getIntent().getBundleExtra(Constants.KEY_BUNDLE).getSerializable(Constants.STUDENT);
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

    private void openAddEventActivity() {
        Intent intent = new Intent(this, CreatePersonalEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivity(intent);
    }

    private void setEvents() {
        createEvents(allEvents, R.color.black);
        createEvents(academicEvents, R.color.butterscotch);
        createEvents(events, R.color.turquoise_blue);
        createEvents(vacations, R.color.soft_green);
        createEvents(personalEvents, R.color.iris);
        allCounterTextView.setText(allEvents.size() + "");
        academicCounterTextView.setText(academicEvents.size() + "");
        eventsCounterTextView.setText(events.size() + "");
        vacationsCounterTextView.setText(vacations.size() + "");
        personalCounterTextView.setText(personalEvents.size() + "");
    }

    private void createEvents(ArrayList<EventAdapter.DemoEvent> attendanceArrayList, int color) {
        for (int i = 0; i < attendanceArrayList.size(); i++) {
            compactCalendarView.addEvent(new Event(getResources().getColor(color), attendanceArrayList.get(i).getDate().getTime()));
        }
    }

    private void setRecyclerView() {
        recyclerView.setAdapter(eventAdapter);
        eventAdapter.addData(allEvents, EventAdapter.EVENTSTATE.ALL);
    }

//    private void populateArrays() {
//        String input_date="01/08/2019";
//        Date dt1=new Date();
//        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
//        try {
//             dt1=format1.parse(input_date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date d1 = new Date(2019, 7, 24);
//        Date d2 = new Date();  // Current date
//        Date d3 = new Date(2019, 7, 23);
//        Date d4 = new Date(2019, 7, 26);
//        Date d6 = new Date(2019, 7, 27);
//        Date d7 = new Date(2019, 7, 28);
//        Date d9 = new Date(2019, 7, 29);
//        Date d10 = new Date(2019, 7, 30);
//        Date d5 = new Date(2019, 7, 31);
//        Date d8 = new Date(2019, 7, 22);
//
//        EventAdapter.DemoEvent demoEvent= new EventAdapter.DemoEvent(d1,"All 1","All Details 1");
//        EventAdapter.DemoEvent demoEvent1= new EventAdapter.DemoEvent(d2,"All 2","All Details 2");
//        EventAdapter.DemoEvent demoEvent2= new EventAdapter.DemoEvent(d3,"Academic 1","Academic Details 1");
//        EventAdapter.DemoEvent demoEvent3= new EventAdapter.DemoEvent(d4,"Academic 2","Academic Details 2");
//        EventAdapter.DemoEvent demoEvent4= new EventAdapter.DemoEvent(d5,"Events 1","Events Details 1");
//        EventAdapter.DemoEvent demoEvent5= new EventAdapter.DemoEvent(d6,"Events 2","Events Details 2");
//        EventAdapter.DemoEvent demoEvent6= new EventAdapter.DemoEvent(dt1,"Vacations 1","Vacations Details 1");
//        EventAdapter.DemoEvent demoEvent7= new EventAdapter.DemoEvent(d8,"Vacations 2","Vacations Details 2");
//        EventAdapter.DemoEvent demoEvent8= new EventAdapter.DemoEvent(d9,"Personal 1","Personal Details 1");
//        EventAdapter.DemoEvent demoEvent9= new EventAdapter.DemoEvent(d10,"Personal 2","Personal Details 2");
//
//
//        allEvents = new ArrayList<>();
//        events = new ArrayList<>();
//        academicEvents = new ArrayList<>();
//        vacations = new ArrayList<>();
//        personalEvents = new ArrayList<>();
//        allEvents.add(demoEvent);
//        allEvents.add(demoEvent1);
//        academicEvents.add(demoEvent2);
//        academicEvents.add(demoEvent3);
//        events.add(demoEvent4);
//        events.add(demoEvent5);
//        vacations.add(demoEvent6);
//        vacations.add(demoEvent7);
//        personalEvents.add(demoEvent8);
//        personalEvents.add(demoEvent9);
//
//
//    }
}
