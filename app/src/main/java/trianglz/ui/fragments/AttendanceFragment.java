package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import attendance.Attendance;
import trianglz.components.AvatarPlaceholderModified;
import trianglz.components.CircleTransform;
import trianglz.models.Student;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.AttendanceAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 04/09/2019.
 */
public class AttendanceFragment extends Fragment implements View.OnClickListener, CompactCalendarView.CompactCalendarViewListener {
    private StudentMainActivity activity;
    private View rootView;
    private CompactCalendarView compactCalendarView;
    private ArrayList<Attendance> absentDates, lateDates, excusedDates, presentDates;
    private JSONArray attendanceJsonArray;
    private RecyclerView recyclerView;
    private IImageLoader imageLoader;
    private AvatarView studentImageView;
    private Student student;
    private TextView monthYearTextView, lateCounterTextView, excusedCounterTextView, absentCounterTextView;
    private ImageButton backBtn;
    private Calendar calendar;
    private String attendance;
    private AttendanceAdapter attendanceAdapter;
    private LinearLayout lateLayout, absentLayout, excusedLayout;
    private View lateView, absentView, excusedView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_attendance, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValueFromIntent();
        bindViews();
        setListeners();
        setAttendance();
        setRecyclerView();
        setEvents();
    }


    private void bindViews() {
        activity.toolbarView.setVisibility(View.GONE);
        activity.headerLayout.setVisibility(View.GONE);
        compactCalendarView = rootView.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(), new Locale("en"));
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        absentDates = new ArrayList<>();
        lateDates = new ArrayList<>();
        excusedDates = new ArrayList<>();
        presentDates = new ArrayList<>();
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        imageLoader = new PicassoLoader();
        studentImageView = rootView.findViewById(R.id.img_student);
        monthYearTextView = rootView.findViewById(R.id.tv_month_year_header);
        lateCounterTextView = rootView.findViewById(R.id.tv_late_counter);
        excusedCounterTextView = rootView.findViewById(R.id.tv_excused_counter);
        absentCounterTextView = rootView.findViewById(R.id.tv_absent_counter);
        backBtn = rootView.findViewById(R.id.btn_back);
        calendar = Calendar.getInstance();
        setStudentImage(student.getAvatar(), student.firstName + " " + student.lastName);
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendarView.removeAllEvents();
        attendanceAdapter = new AttendanceAdapter(activity, AttendanceAdapter.STATE.LATE);
        lateLayout = rootView.findViewById(R.id.layout_late);
        excusedLayout = rootView.findViewById(R.id.layout_excused);
        absentLayout = rootView.findViewById(R.id.layout_absent);
        lateView = rootView.findViewById(R.id.view_late);
        absentView = rootView.findViewById(R.id.view_absent);
        excusedView = rootView.findViewById(R.id.view_excused);
        monthYearTextView.setText(setHeaderDate(Calendar.getInstance().getTime()));
    }

    private void setListeners() {
        backBtn.setOnClickListener(this);
        lateLayout.setOnClickListener(this);
        absentLayout.setOnClickListener(this);
        excusedLayout.setOnClickListener(this);
        compactCalendarView.setListener(this);
    }

    private void getValueFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            attendance = bundle.getString(Constants.KEY_ATTENDANCE);
            student = (Student) bundle.getSerializable(Constants.STUDENT);
        }
    }

    private void setAttendance() {
        try {
            attendanceJsonArray = new JSONArray(attendance);
            for (int i = 0; i < attendanceJsonArray.length(); i++) {
                JSONObject day = attendanceJsonArray.optJSONObject(i);
                Date date = Util.getAttendanceDate(day.optString(Constants.KEY_DATE), activity);
                switch (day.optString(Constants.KEY_STATUS)) {
                    case Constants.KEY_LATE:
                        if (!day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                            lateDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                        else
                            lateDates.add(new Attendance(date, ""));
                        break;
                    case Constants.KEY_ABSENT:
                        if (!day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                            absentDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                        else
                            absentDates.add(new Attendance(date, ""));
                        break;
                    case Constants.KEY_EXCUSED:
                        if (!day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                            excusedDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                        else
                            excusedDates.add(new Attendance(date, ""));
                        break;
                    case "present":
                        if (!day.optString(Constants.KEY_COMMENT).equals(Constants.KEY_NULL))
                            presentDates.add(new Attendance(date, day.optString(Constants.KEY_COMMENT)));
                        else
                            presentDates.add(new Attendance(date, ""));
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setRecyclerView() {
        List<Attendance> dates = new ArrayList<Attendance>();
        for (int i = 0; i < excusedDates.size(); i++) {
            if (new Date().getMonth() == excusedDates.get(i).getDate().getMonth() &&
                    new Date().getYear() == excusedDates.get(i).getDate().getYear()) {
                dates.add(excusedDates.get(i));
            }
        }
        recyclerView.setAdapter(attendanceAdapter);
        attendanceAdapter.addData(lateDates, AttendanceAdapter.STATE.LATE);
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


    private void setEvents() {
        createEvents(excusedDates, R.color.squash);
        createEvents(absentDates, R.color.orange_red);
        createEvents(lateDates, R.color.vivid_purple);
        createEvents(presentDates, R.color.jade_green);
        lateCounterTextView.setText(lateDates.size() + "");
        absentCounterTextView.setText(absentDates.size() + "");
        excusedCounterTextView.setText(excusedDates.size() + "");
    }

    private void createEvents(ArrayList<Attendance> attendanceArrayList, int color) {
        for (int i = 0; i < attendanceArrayList.size(); i++) {
            compactCalendarView.addEvent(new Event(getResources().getColor(color), attendanceArrayList.get(i).getDate().getTime()));
        }
    }

    private void deselectAll() {
        excusedView.setVisibility(View.INVISIBLE);
        lateView.setVisibility(View.INVISIBLE);
        absentView.setVisibility(View.INVISIBLE);
    }

    private String setHeaderDate(Date date) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(new Locale("en"));
        String[] months = dateFormatSymbols.getMonths();
        String monthString = months[date.getMonth()];
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy", new Locale("en"));
        String yearDate = fmt.format(date);
        return monthString + " " + yearDate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                activity.toolbarView.setVisibility(View.VISIBLE);
                activity.headerLayout.setVisibility(View.VISIBLE);
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.layout_late:
                deselectAll();
                lateView.setVisibility(View.VISIBLE);
                attendanceAdapter.addData(lateDates, AttendanceAdapter.STATE.LATE);
                break;
            case R.id.layout_absent:
                deselectAll();
                absentView.setVisibility(View.VISIBLE);
                attendanceAdapter.addData(absentDates, AttendanceAdapter.STATE.ABSENT);
                break;
            case R.id.layout_excused:
                deselectAll();
                excusedView.setVisibility(View.VISIBLE);
                attendanceAdapter.addData(excusedDates, AttendanceAdapter.STATE.EXCUSED);
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
        if (firstDayOfNewMonth.getYear() == calendar.getTime().getYear()
                && firstDayOfNewMonth.getMonth() == calendar.getTime().getMonth()
                && firstDayOfNewMonth.getDay() == calendar.getTime().getDay()) {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.jade_green));
        } else {
            compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }
}
