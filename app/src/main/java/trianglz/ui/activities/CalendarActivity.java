package trianglz.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.BottomItemDecoration;
import trianglz.components.CircleTransform;
import trianglz.core.presenters.CalendarEventsPresenter;
import trianglz.core.views.CalendarEventsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Student;
import trianglz.ui.adapters.EventAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

public class CalendarActivity extends SuperActivity implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener, CalendarEventsPresenter {
    private CompactCalendarView compactCalendarView;
    private CalendarEventsView calendarEventsView;
    private String start = "2010-03-04T00:00:00.000Z";
    private String end = "2030-03-04T00:00:00.000Z";
    private boolean isParent = false;
    private ArrayList<trianglz.models.Event> allEvents, academicEvents, events, personalEvents, vacations, assignments, quizzes;
    private Student student;
    private TextView monthYearTextView, allCounterTextView, academicCounterTextView, eventsCounterTextView, assignmentsCounterTextView, quizzesCounterTextView, vacationsCounterTextView, personalCounterTextView, todayTextView;
    private ImageButton backBtn;
    private Button createPersonalEventBtn;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private Calendar calendar;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private LinearLayout allLayout, academicLayout, eventsLayout, vacationsLayout, personalLayout, assignmentsLayout, quizzesLayout;
    private View allView, academicView, eventsView, vacationsView, personalView, assignmentsView, quizzesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getValueFromIntent();
        bindViews();
        checkUserType();
        setListeners();
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        setRecyclerView();
        getEvents();
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
            case R.id.layout_assignments:
                deselectAll();
                assignmentsView.setVisibility(View.VISIBLE);
                eventAdapter.addData(assignments, EventAdapter.EVENTSTATE.ASSIGNMENTS);
                break;
            case R.id.layout_quizzes:
                deselectAll();
                quizzesView.setVisibility(View.VISIBLE);
                eventAdapter.addData(quizzes, EventAdapter.EVENTSTATE.QUIZZES);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_CANCELED) {
                getEvents();
            }
        }
    }

    @Override
    public void onDayClick(Date dateClicked) {
        if (dateClicked.getYear() == calendar.getTime().getYear()
                && dateClicked.getMonth() == calendar.getTime().getMonth()
                && dateClicked.getDate() == calendar.getTime().getDate()) {
            if (isParent) {
                compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.turquoise_blue_two));
            } else {
                compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.salmon));
            }
        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        monthYearTextView.setText(setHeaderDate(firstDayOfNewMonth));
        if (isParent) {
            compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.turquoise_blue_two));
        } else {
            compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.salmon));
        }
        if (firstDayOfNewMonth
                .getYear() == calendar.getTime().getYear()
                && firstDayOfNewMonth.getMonth() == calendar.getTime().getMonth() &&
                firstDayOfNewMonth.getDate() == calendar.getTime().getDate()) {
            if (isParent) {
                compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.turquoise_blue_two));
            } else {
                compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.salmon));
            }
        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    private void bindViews() {

        allEvents = new ArrayList<>();
        academicEvents = new ArrayList<>();
        events = new ArrayList<>();
        personalEvents = new ArrayList<>();
        vacations = new ArrayList<>();
        assignments = new ArrayList<>();
        quizzes = new ArrayList<>();
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), new Locale("en"));
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.removeAllEvents();
        calendarEventsView = new CalendarEventsView(CalendarActivity.this, this);

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
        assignmentsCounterTextView = findViewById(R.id.tv_assignments_counter);
        quizzesCounterTextView = findViewById(R.id.tv_quizzes_counter);
        todayTextView = findViewById(R.id.today_tv);
        backBtn = findViewById(R.id.btn_back);
        createPersonalEventBtn = findViewById(R.id.create_personal_event);

        calendar = Calendar.getInstance();

        allLayout = findViewById(R.id.layout_all);
        academicLayout = findViewById(R.id.layout_academic);
        eventsLayout = findViewById(R.id.layout_events);
        vacationsLayout = findViewById(R.id.layout_vacations);
        personalLayout = findViewById(R.id.layout_personal);
        assignmentsLayout = findViewById(R.id.layout_assignments);
        quizzesLayout = findViewById(R.id.layout_quizzes);

        allView = findViewById(R.id.view_all);
        academicView = findViewById(R.id.view_academic);
        eventsView = findViewById(R.id.view_events);
        vacationsView = findViewById(R.id.view_vacations);
        personalView = findViewById(R.id.view_personal);
        assignmentsView = findViewById(R.id.view_assignments);
        quizzesView = findViewById(R.id.view_quizzes);
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
        assignmentsLayout.setOnClickListener(this);
        quizzesLayout.setOnClickListener(this);
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
        assignmentsView.setVisibility(View.INVISIBLE);
        quizzesView.setVisibility(View.INVISIBLE);
    }

    private void openAddEventActivity() {
        Intent intent = new Intent(this, CreatePersonalEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        startActivityForResult(intent, 1);
    }

    private void setEvents() {
        createEvents(academicEvents, R.color.butterscotch);
        createEvents(events, R.color.turquoise_blue);
        createEvents(vacations, R.color.soft_green);
        createEvents(personalEvents, R.color.iris);
        createEvents(assignments, R.color.red);
        createEvents(quizzes, R.color.light_sage);
        allCounterTextView.setText(allEvents.size() + "");
        academicCounterTextView.setText(academicEvents.size() + "");
        eventsCounterTextView.setText(events.size() + "");
        vacationsCounterTextView.setText(vacations.size() + "");
        personalCounterTextView.setText(personalEvents.size() + "");
        quizzesCounterTextView.setText(quizzes.size() + "");
        assignmentsCounterTextView.setText(assignments.size() + "");
    }

    private void createEvents(ArrayList<trianglz.models.Event> eventsArrayList, int color) {
        for (int i = 0; i < eventsArrayList.size(); i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(eventsArrayList.get(i).getStartDate());
            Date newDate = c.getTime();
            while (newDate.getDate() != eventsArrayList.get(i).getEndDate().getDate()) {
                c.add(Calendar.DATE, 1);
                newDate = c.getTime();
                compactCalendarView.addEvent(new Event(getResources().getColor(color), newDate.getTime()));
            }
        }
    }

    private void setRecyclerView() {
        recyclerView.setAdapter(eventAdapter);
    }

    @Override
    public void onGetEventsSuccess(ArrayList<trianglz.models.Event> events) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        allEvents.clear();
        allEvents.addAll(events);

        Collections.sort(allEvents, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));

        eventAdapter.addData(allEvents, EventAdapter.EVENTSTATE.ALL);
        fillCalendarEventLists(events);
        setEvents();

    }

    @Override
    public void onGetEventsFailure(String message, int code) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        if (code == 401 || code == 500) {
            logoutUser(this);
        } else {
            showErrorDialog(this);
        }
    }

    private void getEvents() {
        if (Util.isNetworkAvailable(this)) {
            showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getEvents(341, "user", end, start);
            calendarEventsView.getEvents(url);
            Log.i("TAG", "getEvents: " + url);
        } else {
            Util.showNoInternetConnectionDialog(this);
        }
    }

    private void checkUserType() {
        if (SessionManager.getInstance().getStudentAccount()) {
            todayTextView.setTextColor((getResources().getColor(R.color.salmon)));
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.salmon));
            createPersonalEventBtn.setTextColor((getResources().getColor(R.color.salmon)));
            createPersonalEventBtn.setBackground(ContextCompat.getDrawable(CalendarActivity.this, R.drawable.curved_salmon_background));
        } else if (SessionManager.getInstance().getUserType()) {
            isParent = true;
            todayTextView.setTextColor((getResources().getColor(R.color.turquoise_blue_two)));
            compactCalendarView.setCurrentDayBackgroundColor((getResources().getColor(R.color.turquoise_blue_two)));
            createPersonalEventBtn.setTextColor((getResources().getColorStateList(R.color.turquoise_blue_two)));
            createPersonalEventBtn.setBackground(ContextCompat.getDrawable(CalendarActivity.this, R.drawable.curved_turquoise_blue_two_background));
        }
    }

    private void fillCalendarEventLists(ArrayList<trianglz.models.Event> eventList) {
        events.clear();
        assignments.clear();
        quizzes.clear();
        academicEvents.clear();
        vacations.clear();
        personalEvents.clear();
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getType().equals(Constants.TYPE_EVENT)) {
                events.add(eventList.get(i));
            } else if (eventList.get(i).getType().equals(Constants.TYPE_ASSIGNMENTS)) {
                assignments.add(eventList.get(i));
            } else if (eventList.get(i).getType().equals(Constants.TYPE_QUIZZES)) {
                quizzes.add(eventList.get(i));
            } else if (eventList.get(i).getType().equals(Constants.TYPE_ACADEMIC)) {
                academicEvents.add(eventList.get(i));
            } else if (eventList.get(i).getType().equals(Constants.TYPE_VACATIONS)) {
                vacations.add(eventList.get(i));
            } else if (eventList.get(i).getType().equals(Constants.TYPE_PERSONAL)) {
                personalEvents.add(eventList.get(i));
            }
        }
        Collections.sort(academicEvents, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));
        Collections.sort(assignments, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));
        Collections.sort(quizzes, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));
        Collections.sort(personalEvents, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));
        Collections.sort(events, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));
        Collections.sort(vacations, Collections.reverseOrder(new trianglz.models.Event.SortByDate()));


    }
}
