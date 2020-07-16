package trianglz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.skolera.skolera_android.R;

import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import trianglz.components.ExcusedDialog;
import trianglz.components.TakeAttendanceDialog;
import trianglz.components.TopItemDecoration;
import trianglz.core.presenters.TeacherAttendancePresenter;
import trianglz.core.views.TeacherAttendanceView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Attendance;
import trianglz.models.AttendanceStudent;
import trianglz.models.AttendanceTimetableSlot;
import trianglz.models.Attendances;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherAttendanceAdapter;
import trianglz.utils.Constants;
import trianglz.utils.Util;


/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class TeacherAttendanceFragment extends Fragment implements View.OnClickListener, TakeAttendanceDialog.TakeAttendanceDialogInterface, PerSlotFragment.SelectSlotInterface, TeacherAttendanceAdapter.TeacherAttendanceAdapterInterface, TeacherAttendancePresenter, ExcusedDialog.ExcusedDialogInterface {


    private View rootView;
    private int courseGroupId, day, month, year;
    private TextView todaysDate;
    private Boolean isMultipleSelected;
    private Boolean perSlot = false;
    private Boolean isFragmentShowing = false;
    private AttendanceTimetableSlot attendanceTimetableSlot;
    private Attendance attendance;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private StudentMainActivity activity;
    private TeacherAttendanceView teacherAttendanceView;
    private View fullDayView, perSlotView;
    private TakeAttendanceDialog takeAttendanceDialog;
    private ArrayList<Integer> createIds;
    private ArrayList<AttendanceTimetableSlot> adapterTimeTableSlots;
    private TeacherAttendanceAdapter teacherAttendanceAdapter;
    private Button fullDayButton, perSlotButton, assignAllButton, assignSelectedButton;
    private SwipeRefreshLayout pullRefreshLayout;
    private LinearLayout placeholderLinearLayout;
    private LinearLayout skeletonLayout;
    private ShimmerFrameLayout shimmer;
    private LayoutInflater inflater;

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
        getDateInNumbers();
        getFullDayAttendance();
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
        createIds = new ArrayList<>();
        adapterTimeTableSlots = new ArrayList<>();
        pullRefreshLayout = rootView.findViewById(R.id.pullToRefresh);
        pullRefreshLayout.setColorSchemeResources(Util.checkUserColor());
        placeholderLinearLayout = rootView.findViewById(R.id.placeholder_layout);
        recyclerView.addItemDecoration(new TopItemDecoration((int) Util.convertDpToPixel(8, activity), false));
        skeletonLayout = rootView.findViewById(R.id.skeletonLayout);
        shimmer = rootView.findViewById(R.id.shimmer_view_container);
        this.inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void setListeners() {
        fullDayButton.setOnClickListener(this);
        perSlotButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        assignAllButton.setOnClickListener(this);
        assignSelectedButton.setOnClickListener(this);
        pullRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                placeholderLinearLayout.setVisibility(View.GONE);
                if (perSlot) {
                    getPerSlotAttendance();
                } else {
                    getFullDayAttendance();
                }
            }
        });
    }

    private void colorFullDayAttendance() {
        fullDayView.setVisibility(View.VISIBLE);
        perSlotView.setVisibility(View.INVISIBLE);
        fullDayButton.setTextColor(getResources().getColor(R.color.cerulean_blue, null));
        perSlotButton.setTextColor(getResources().getColor(R.color.greyish, null));
        todaysDate.setText(getCurrentDate());
    }

    private void colorPerSlotAttendance() {
        perSlotView.setVisibility(View.VISIBLE);
        fullDayView.setVisibility(View.INVISIBLE);
        perSlotButton.setTextColor(getResources().getColor(R.color.cerulean_blue, null));
        fullDayButton.setTextColor(getResources().getColor(R.color.greyish, null));
        todaysDate.setText(activity.getResources().getString(R.string.slot) + " " + attendanceTimetableSlot.getSlotNo());
    }

    private void getPerSlotAttendance() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getPerSlotAttendance(courseGroupId, day, month, year);
        teacherAttendanceView.getFullDayTeacherAttendance(url);
        showSkeleton(true);
    }

    private void getFullDayAttendance() {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.getFullDayAttendance(courseGroupId, day, month, year, day, month, year);
        teacherAttendanceView.getFullDayTeacherAttendance(url);
        showSkeleton(true);
    }

    private void getDateInNumbers() {
//        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        if(SessionManager.getInstance().getHeaderHashMap().containsKey("timezone")){
            String timeZone = SessionManager.getInstance().getHeaderHashMap().get("timezone");
            cal =  Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        }
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
    }

    private String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE d MMM");
        if(SessionManager.getInstance().getHeaderHashMap().containsKey("timezone")){
            df.setTimeZone(TimeZone.getTimeZone(SessionManager.getInstance().getHeaderHashMap().get("timezone")));
        }
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
                if (perSlot)
                    getFullDayAttendance();
                break;
            case R.id.per_slot_btn:
                isFragmentShowing = true;
                getPerSlotAttendance();
                break;
            case R.id.assign_all_btn:
                isMultipleSelected = false;
                takeAttendanceDialog = new TakeAttendanceDialog(activity, false, this);
                takeAttendanceDialog.show();
                break;
            case R.id.assign_selected_btn:
                isMultipleSelected = true;
                takeAttendanceDialog = new TakeAttendanceDialog(activity, true, this);
                takeAttendanceDialog.show();
                break;
        }
    }

    @Override
    public void onStatusClicked(int studentId, String status, String excusedString) {
        ArrayList<Integer> studentIds = new ArrayList<>();
        studentIds.add(studentId);
        if (status.equals(Constants.TYPE_EXCUSED)) {
            ExcusedDialog excusedDialog = new ExcusedDialog(activity, this, studentId);
            excusedDialog.setExcusedString(excusedString);
            excusedDialog.show();
        } else {
            getCreateAndUpdateBatch(studentIds, status);
        }
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
        createIds.clear();
        this.attendance = attendance;
        showSkeleton(false);
        if (attendance.getStudents().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            placeholderLinearLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            placeholderLinearLayout.setVisibility(View.GONE);
        }
        if (attendance.getTimetableSlots() == null) {
            perSlot = false;
            teacherAttendanceAdapter.attendanceTimetableSlot = null;
            colorFullDayAttendance();
            teacherAttendanceAdapter.addData(attendance.getStudents(), attendance.getAttendances());
        } else {
            validateTodaySlots();
            if (!adapterTimeTableSlots.isEmpty()) {
                if (isFragmentShowing) {
                    PerSlotFragment perSlotFragment = PerSlotFragment.newInstance(this);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.TIMETABLE_SLOTS, adapterTimeTableSlots);
                    perSlotFragment.setArguments(bundle);
                    getParentFragment().getChildFragmentManager().
                            beginTransaction().add(R.id.course_root, perSlotFragment, "CoursesFragments").
                            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                            addToBackStack(null).commit();
                    isFragmentShowing = false;
                } else {
                    teacherAttendanceAdapter.addData(attendance.getStudents(), attendance.getAttendances());
                }
            } else {
                activity.showErrorDialog(activity, -3, activity.getResources().getString(R.string.no_slots_available));
            }
        }
    }

    @Override
    public void onGetTeacherAttendanceFailure(String message, int code) {
        activity.showErrorDialog(activity, code, "");
        recyclerView.setVisibility(View.GONE);
        placeholderLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBatchAttendanceCreatedSuccess() {
        showSkeleton(false);
        if (perSlot) {
            getPerSlotAttendance();
        } else {
            getFullDayAttendance();
        }
    }

    @Override
    public void onBatchAttendanceCreatedFailure(String message, int code) {
        showSkeleton(false);
        activity.showErrorDialog(activity, code, "");
    }

    @Override
    public void onBatchAttendanceDeletedSuccess() {
        showSkeleton(false);
        if (perSlot) {
            getPerSlotAttendance();
        } else {
            getFullDayAttendance();
        }
    }

    @Override
    public void onBatchAttendanceDeletedFailure(String message, int code) {
        showSkeleton(false);
        if (perSlot) {
            getPerSlotAttendance();
        } else {
            getFullDayAttendance();
        }
    }


    @Override
    public void onSubmitClicked(String comment, int studentId) {
        String url;
        ArrayList<Integer> studentIds = new ArrayList<>();
        studentIds.add(studentId);
        url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createBatchAttendance();
        String date = day + "-" + month + "-" + year;
        if (perSlot) {
            teacherAttendanceView.createBatchAttendance(url, date, comment, Constants.TYPE_EXCUSED, studentIds, attendanceTimetableSlot.getId());
        } else {
            teacherAttendanceView.createBatchAttendance(url, date, comment, Constants.TYPE_EXCUSED, studentIds);
        }
        showSkeleton(true);
    }

    @Override
    public void onApplySelected(AttendanceTimetableSlot timetableSlot) {
        perSlot = true;
        this.attendanceTimetableSlot = timetableSlot;
        teacherAttendanceAdapter.attendanceTimetableSlot = timetableSlot;
        teacherAttendanceAdapter.addData(attendance.getStudents(), attendance.getAttendances());
        colorPerSlotAttendance();
    }

    @Override
    public void onStatusSelected(String status) {
        Collection<AttendanceStudent> values = teacherAttendanceAdapter.positionCheckStatusHashMap.values();
        ArrayList<AttendanceStudent> listOfValues = new ArrayList<>(values);
        ArrayList<Integer> studentIds = new ArrayList<>();
        ArrayList<Integer> attendanceIds = new ArrayList<>();
        if (isMultipleSelected) {
            for (int i = 0; i < listOfValues.size(); i++) {
                studentIds.add(listOfValues.get(i).getChildId());
                if (teacherAttendanceAdapter.positionStatusHashMap.containsKey(studentIds.get(i))) {
                    attendanceIds.add(teacherAttendanceAdapter.positionStatusHashMap.get(studentIds.get(i)).getId());
                }
            }
            if (status.equals(Constants.DELETE_ALL)) {
                deleteBatchAttendance(attendanceIds);
            } else {
                getCreateAndUpdateBatch(studentIds, status);
            }
            teacherAttendanceAdapter.positionCheckStatusHashMap.clear();
            teacherAttendanceAdapter.teacherAttendanceAdapterInterface.onCheckClicked(false);
            teacherAttendanceAdapter.notifyDataSetChanged();
        } else {
            if (status.equals(Constants.DELETE_ALL)) {
                for (Map.Entry<Integer, Attendances> entry : teacherAttendanceAdapter.positionStatusHashMap.entrySet()) {
                    attendanceIds.add(entry.getValue().getId());
                }
                deleteBatchAttendance(attendanceIds);
            } else {
                for (int i = 0; i < attendance.getStudents().size(); i++) {
                    studentIds.add(attendance.getStudents().get(i).getChildId());
                }
                getCreateAndUpdateBatch(studentIds, status);
            }
        }
    }

    public void getCreateAndUpdateBatch(ArrayList<Integer> studentIds, String status) {
        String url;
        String date = day + "-" + month + "-" + year;
        for (int i = 0; i < studentIds.size(); i++) {
            createIds.add(studentIds.get(i));
        }
        if (!createIds.isEmpty()) {
            url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createBatchAttendance();
            if (perSlot) {
                teacherAttendanceView.createBatchAttendance(url, date, "", status, createIds, attendanceTimetableSlot.getId());
                showSkeleton(true);
            } else {
                teacherAttendanceView.createBatchAttendance(url, date, "", status, createIds);
                showSkeleton(true);
            }
        }
    }

    public void deleteBatchAttendance(ArrayList<Integer> attendancesIds) {
        String url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.deleteBatchAttendance();
        if (!attendancesIds.isEmpty()) {
            teacherAttendanceView.deleteBatchAttendance(url, attendancesIds);
            showSkeleton(true);
        }
    }

    private void validateTodaySlots() {
        adapterTimeTableSlots.clear();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE", Locale.US);
        String formattedDate = df.format(c);
        for (int i = 0; i < attendance.getTimetableSlots().size(); i++) {
            if (attendance.getTimetableSlots().get(i).getDay().toLowerCase().equals(formattedDate.toLowerCase())) {
                adapterTimeTableSlots.add(attendance.getTimetableSlots().get(i));
            }
        }
    }

    public void showSkeleton(boolean show) {
        if (show) {
            skeletonLayout.removeAllViews();
            int skeletonRows = Util.getSkeletonRowCount(activity);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.skeleton_teacher_attendance_layout, (ViewGroup) rootView, false);
                skeletonLayout.addView(rowLayout);
            }
            placeholderLinearLayout.setVisibility(View.GONE);

            recyclerView.setVisibility(View.GONE);
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmer();
            shimmer.showShimmer(true);
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmer();
            shimmer.hideShimmer();
            shimmer.setVisibility(View.GONE);
        }
    }

}

