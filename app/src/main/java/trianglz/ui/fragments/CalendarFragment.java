package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import trianglz.core.presenters.FragmentCommunicationInterface;
import trianglz.core.views.CalendarEventsView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Event;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.EventAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 05/09/2019.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener, CalendarEventsPresenter, FragmentCommunicationInterface {

    private StudentMainActivity activity;
    private View rootView;
    private CompactCalendarView compactCalendarView;
    private CalendarEventsView calendarEventsView;
    //todo change dates with the correct values
    private String start = "2010-03-04T00:00:00.000Z";
    private String end = "2030-03-04T00:00:00.000Z";
    private boolean isParent = false;
    private ArrayList<Event> allEvents, academicEvents, events, personalEvents, vacations, assignments, quizzes;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();

        rootView = inflater.inflate(R.layout.activity_calendar, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        setRecyclerView();
        getEvents();
        onBackPress();
    }

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Log.d("", "onKeyback: ");
                        getFragmentManager().popBackStack();
                        activity.toolbarView.setVisibility(View.VISIBLE);
                        activity.headerLayout.setVisibility(View.VISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void bindViews() {

        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        allEvents = new ArrayList<>();
        academicEvents = new ArrayList<>();
        events = new ArrayList<>();
        personalEvents = new ArrayList<>();
        vacations = new ArrayList<>();
        assignments = new ArrayList<>();
        quizzes = new ArrayList<>();
        compactCalendarView = rootView.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), new Locale("en"));
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.removeAllEvents();
        calendarEventsView = new CalendarEventsView(activity, this);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new BottomItemDecoration(66, false));
        eventAdapter = new EventAdapter(activity, EventAdapter.EVENTSTATE.ALL);
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        monthYearTextView = rootView.findViewById(R.id.tv_month_year_header);
        allCounterTextView = rootView.findViewById(R.id.tv_all_counter);
        academicCounterTextView = rootView.findViewById(R.id.tv_academic_counter);
        eventsCounterTextView = rootView.findViewById(R.id.tv_events_counter);
        vacationsCounterTextView = rootView.findViewById(R.id.tv_vacations_counter);
        personalCounterTextView = rootView.findViewById(R.id.tv_personal_counter);
        assignmentsCounterTextView = rootView.findViewById(R.id.tv_assignments_counter);
        quizzesCounterTextView = rootView.findViewById(R.id.tv_quizzes_counter);
        todayTextView = rootView.findViewById(R.id.today_tv);
        backBtn = rootView.findViewById(R.id.btn_back);
        createPersonalEventBtn = rootView.findViewById(R.id.create_personal_event);

        calendar = Calendar.getInstance();

        allLayout = rootView.findViewById(R.id.layout_all);
        academicLayout = rootView.findViewById(R.id.layout_academic);
        eventsLayout = rootView.findViewById(R.id.layout_events);
        vacationsLayout = rootView.findViewById(R.id.layout_vacations);
        personalLayout = rootView.findViewById(R.id.layout_personal);
        assignmentsLayout = rootView.findViewById(R.id.layout_assignments);
        quizzesLayout = rootView.findViewById(R.id.layout_quizzes);

        allView = rootView.findViewById(R.id.view_all);
        academicView = rootView.findViewById(R.id.view_academic);
        eventsView = rootView.findViewById(R.id.view_events);
        vacationsView = rootView.findViewById(R.id.view_vacations);
        personalView = rootView.findViewById(R.id.view_personal);
        assignmentsView = rootView.findViewById(R.id.view_assignments);
        quizzesView = rootView.findViewById(R.id.view_quizzes);
        monthYearTextView.setText(setHeaderDate(Calendar.getInstance().getTime()));
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);

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

    private void setStudentImage(String imageUrl, final String name) {
        if (imageUrl == null || imageUrl.equals("")) {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
        } else {
            imageLoader = new PicassoLoader();
            imageLoader.loadImage(studentImageView, new AvatarPlaceholderModified(name), "Path of Image");
            Picasso.with(activity)
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

    private void setRecyclerView() {
        recyclerView.setAdapter(eventAdapter);
    }

    private void getEvents() {
        if (Util.isNetworkAvailable(activity)) {
            activity.showLoadingDialog();
            String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getEvents(student.userId, "user", end, start);
            calendarEventsView.getEvents(url);
            Log.i("TAG", "getEvents: " + url);
        } else {
            Util.showNoInternetConnectionDialog(activity);
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

    private String setHeaderDate(Date date) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(new Locale("en"));
        String[] months = dateFormatSymbols.getMonths();
        String monthString = months[date.getMonth()];
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy", new Locale("en"));
        String yearDate = fmt.format(date);
        return monthString + " " + yearDate;
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
        CreatePersonalEventFragment createPersonalEventFragment = CreatePersonalEventFragment.newInstance(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.STUDENT, student);
        createPersonalEventFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().
                beginTransaction().add(R.id.menu_fragment_root, createPersonalEventFragment, "MenuFragments").
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).commit();
    }

    private void createEvents(ArrayList<trianglz.models.Event> eventsArrayList, int color) {
        for (int i = 0; i < eventsArrayList.size(); i++) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(eventsArrayList.get(i).startDate);
            while (c.getTimeInMillis() <= eventsArrayList.get(i).endDate) {
                compactCalendarView.addEvent(new com.github.sundeepk.compactcalendarview.domain.Event(getResources().getColor(color, null), c.getTimeInMillis() * 1000));
                c.add(Calendar.MILLISECOND, 86400);
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
                activity.getSupportFragmentManager().popBackStack();
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
    public void onDayClick(Date dateClicked) {
        if (dateClicked.getYear() == calendar.getTime().getYear()
                && dateClicked.getMonth() == calendar.getTime().getMonth()
                && dateClicked.getDate() == calendar.getTime().getDate()) {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.jade_green));
        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        monthYearTextView.setText(setHeaderDate(firstDayOfNewMonth));
        compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.jade_green));
        if (firstDayOfNewMonth
                .getYear() == calendar.getTime().getYear()
                && firstDayOfNewMonth.getMonth() == calendar.getTime().getMonth() &&
                firstDayOfNewMonth.getDate() == calendar.getTime().getDate()) {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.jade_green));

        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public void onGetEventsSuccess(ArrayList<Event> events) {
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
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
        if (activity.progress.isShowing()) {
            activity.progress.dismiss();
        }
        if (code == 401 || code == 500) {
            activity.logoutUser(activity);
        } else {
            activity.showErrorDialog(activity);
        }
    }

    @Override
    public void reloadEvents() {
        getEvents();
    }
}
