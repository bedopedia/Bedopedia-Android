package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import trianglz.components.ExcusedDialog;
import trianglz.components.TakeAttendanceDialog;
import trianglz.core.presenters.TeacherAttendancePresenter;
import trianglz.core.views.TeacherAttendanceView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Attendance;
import trianglz.models.AttendanceStudent;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherAttendanceAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class TeacherAttendanceFragment extends Fragment implements View.OnClickListener, TeacherAttendanceAdapter.TeacherAttendanceAdapterInterface, TeacherAttendancePresenter {


    private View rootView;
    private int courseGroupId, day, month, year;
    private TextView todaysDate;
    private Boolean isMultipleSelected;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private StudentMainActivity activity;
    private TeacherAttendanceView teacherAttendanceView;
    private View fullDayView, perSlotView;
    private TakeAttendanceDialog takeAttendanceDialog;
    private TeacherAttendanceAdapter teacherAttendanceAdapter;
    private Button fullDayButton, perSlotButton, assignAllButton, assignSelectedButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_teacher_attendance, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getValuesFromIntent();
        bindViews();
        setListeners();
    }

    private void getValuesFromIntent() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            courseGroupId = bundle.getInt(Constants.KEY_COURSE_GROUP_ID, 0);
        }
    }

    private void bindViews() {
        fullDayButton = rootView.findViewById(R.id.full_day_btn);
        perSlotButton = rootView.findViewById(R.id.per_slot_btn);
        fullDayView = rootView.findViewById(R.id.full_day_view);
        perSlotView = rootView.findViewById(R.id.per_slot_view);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        todaysDate = rootView.findViewById(R.id.todays_date);
        todaysDate.setText(getCurrentDate());
        backButton = rootView.findViewById(R.id.btn_back);
        assignAllButton = rootView.findViewById(R.id.assign_all_btn);
        assignSelectedButton = rootView.findViewById(R.id.assign_selected_btn);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        teacherAttendanceAdapter = new TeacherAttendanceAdapter(activity, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(teacherAttendanceAdapter);
        teacherAttendanceView = new TeacherAttendanceView(activity, this);
        getDateInNumbers();
        getFullDayAttendance();
    }

    private void setListeners() {
        fullDayButton.setOnClickListener(this);
        perSlotButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        assignAllButton.setOnClickListener(this);
        assignSelectedButton.setOnClickListener(this);
    }

    private void showFullDayAttendance() {
        fullDayView.setVisibility(View.VISIBLE);
        perSlotView.setVisibility(View.INVISIBLE);
        fullDayButton.setTextColor(getResources().getColor(R.color.cerulean_blue, null));
        perSlotButton.setTextColor(getResources().getColor(R.color.greyish, null));

    }

    private void showPerSlotAttendance() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getPerSlotAttendance(courseGroupId, day, month, year, day, month, year);
        teacherAttendanceView.getFullDayTeacherAttendance(url);
        activity.showLoadingDialog();
    }

    private void getFullDayAttendance() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getFullDayAttendance(courseGroupId, day, month, year, day, month, year);
        teacherAttendanceView.getFullDayTeacherAttendance(url);
        activity.showLoadingDialog();
    }

    private void getDateInNumbers() {
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE d MMM");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
            case R.id.full_day_btn:
                showFullDayAttendance();
                break;
            case R.id.per_slot_btn:
                showPerSlotAttendance();
                break;
            case R.id.assign_all_btn:
                takeAttendanceDialog = new TakeAttendanceDialog(activity, false);
                takeAttendanceDialog.show();
                break;
            case R.id.assign_selected_btn:
                takeAttendanceDialog = new TakeAttendanceDialog(activity, true);
                takeAttendanceDialog.show();
                break;
        }
    }

    @Override
    public void onAbsentClicked(AttendanceStudent attendanceStudent) {

    }

    @Override
    public void onPresentClicked(AttendanceStudent attendanceStudent) {

    }

    @Override
    public void onExcusedClicked(AttendanceStudent attendanceStudent) {
        ExcusedDialog excusedDialog = new ExcusedDialog(activity);
        excusedDialog.show();
    }

    @Override
    public void onLateClicked(AttendanceStudent attendanceStudent) {

    }

    @Override
    public void onCheckClicked(Boolean isSelected) {
        if (isSelected) {
            assignAllButton.setVisibility(View.GONE);
            assignSelectedButton.setVisibility(View.VISIBLE);
        } else {
            assignAllButton.setVisibility(View.VISIBLE);
            assignSelectedButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetTeacherAttendanceSuccess(Attendance attendance) {
        if (attendance.getTimetableSlots() == null) {
            if (activity.progress.isShowing())
                activity.progress.dismiss();
            teacherAttendanceAdapter.addData(attendance.getStudents());
        }else{
            if (activity.progress.isShowing())
                activity.progress.dismiss();
            perSlotView.setVisibility(View.VISIBLE);
            fullDayView.setVisibility(View.INVISIBLE);
            perSlotButton.setTextColor(getResources().getColor(R.color.cerulean_blue, null));
            fullDayButton.setTextColor(getResources().getColor(R.color.greyish, null));
            PerSlotFragment perSlotFragment = new PerSlotFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.TIMETABLE_SLOTS,attendance.getTimetableSlots());
            perSlotFragment.setArguments(bundle);
            getParentFragment().getChildFragmentManager().
                    beginTransaction().add(R.id.course_root, perSlotFragment, "CoursesFragments").
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    addToBackStack(null).commit();
        }
    }

    @Override
    public void onGetTeacherAttendanceFailure(String message, int code) {
        activity.showErrorDialog(activity, code, "");
    }
}

