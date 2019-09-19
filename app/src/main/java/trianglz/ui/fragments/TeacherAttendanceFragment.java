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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import trianglz.components.ExcusedDialog;
import trianglz.components.TakeAttendanceDialog;
import trianglz.core.presenters.TeacherAttendancePresenter;
import trianglz.core.views.TeacherAttendanceView;
import trianglz.managers.SessionManager;
import trianglz.managers.api.ApiEndPoints;
import trianglz.models.Attendance;
import trianglz.models.AttendanceStudent;
import trianglz.models.AttendanceTimetableSlot;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherAttendanceAdapter;
import trianglz.utils.Constants;


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
    private ArrayList<Integer> createIds, updateIds;
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
        createIds = new ArrayList<>();
        updateIds = new ArrayList<>();
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
        month = cal.get(Calendar.MONTH) + 1;
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
    public void onStatusClicked(int studentId, String status) {
        ArrayList<Integer> studentIds = new ArrayList<>();
        studentIds.add(studentId);
        if (status.equals(Constants.TYPE_EXCUSED)) {
            ExcusedDialog excusedDialog = new ExcusedDialog(activity, this, studentId);
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
        if (activity.progress.isShowing())
            activity.progress.dismiss();

        if (attendance.getTimetableSlots() == null) {
            perSlot = false;
            teacherAttendanceAdapter.attendanceTimetableSlot = null;
            colorFullDayAttendance();
            teacherAttendanceAdapter.addData(attendance.getStudents(), attendance.getAttendances());
        } else {
            if (!attendance.getTimetableSlots().isEmpty()) {
                if (isFragmentShowing) {
                    PerSlotFragment perSlotFragment = PerSlotFragment.newInstance(this);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.TIMETABLE_SLOTS, attendance.getTimetableSlots());
                    perSlotFragment.setArguments(bundle);
                    getParentFragment().getChildFragmentManager().
                            beginTransaction().add(R.id.course_root, perSlotFragment, "CoursesFragments").
                            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                            addToBackStack(null).commit();
                    isFragmentShowing = false;
                } else {
                    teacherAttendanceAdapter.addData(attendance.getStudents(), attendance.getAttendances());
                }
            }else{
                activity.showErrorDialog(activity,-3,activity.getResources().getString(R.string.no_slots_available));
            }

        }
    }

    @Override
    public void onGetTeacherAttendanceFailure(String message, int code) {
        activity.showErrorDialog(activity, code, "");
    }

    @Override
    public void onBatchAttendanceCreatedSuccess() {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        if (perSlot) {
            getPerSlotAttendance();
        } else {
            getFullDayAttendance();
        }
    }

    @Override
    public void onBatchAttendanceCreatedFailure(String message, int code) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, code, "");
    }

    @Override
    public void onUpdateAttendanceSuccess() {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        if (perSlot) {
            getPerSlotAttendance();
        } else {
            getFullDayAttendance();
        }
    }

    @Override
    public void onUpdateAttendanceFailure(String message, int code) {
        if (activity.progress.isShowing())
            activity.progress.dismiss();
        activity.showErrorDialog(activity, code, "");
    }

    @Override
    public void onSubmitClicked(String comment, int studentId) {
        String url;
        int attendanceId;
        ArrayList<Integer> studentIds = new ArrayList<>();
        studentIds.add(studentId);
        if (teacherAttendanceAdapter.positionStatusHashMap.containsKey(studentId)) {
            attendanceId = teacherAttendanceAdapter.positionStatusHashMap.get(studentId).getId();
            url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.updateAttendance(attendanceId);
            teacherAttendanceView.updateAttendance(url, "", Constants.TYPE_EXCUSED, attendanceId);
            activity.showLoadingDialog();
        } else {
            url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createBatchAttendance();
            String date = day + "-" + month + "-" + year;
            if (perSlot) {
                teacherAttendanceView.createBatchAttendance(url, date, comment, Constants.TYPE_EXCUSED, studentIds, attendanceTimetableSlot.getId());
            } else {
                teacherAttendanceView.createBatchAttendance(url, date, comment, Constants.TYPE_EXCUSED, studentIds);
            }
            activity.showLoadingDialog();
        }
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
        if (isMultipleSelected) {
            for (int i = 0; i < listOfValues.size(); i++) {
                studentIds.add(listOfValues.get(i).getChildId());
            }
            getCreateAndUpdateBatch(studentIds, status);
        } else {
            for (int i = 0; i < attendance.getStudents().size(); i++) {
                studentIds.add(attendance.getStudents().get(i).getChildId());
            }
            getCreateAndUpdateBatch(studentIds, status);
        }
    }

    public void getCreateAndUpdateBatch(ArrayList<Integer> studentIds, String status) {
        String url;
        int attendanceId;
        String date = day + "-" + month + "-" + year;
        for (int i = 0; i < studentIds.size(); i++) {
            if (teacherAttendanceAdapter.positionStatusHashMap.containsKey(studentIds.get(i))) {
                updateIds.add(studentIds.get(i));
            } else {
                createIds.add(studentIds.get(i));
            }
        }
        if (!createIds.isEmpty()) {
            url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.createBatchAttendance();
            if (perSlot) {
                teacherAttendanceView.createBatchAttendance(url, date, "", status, createIds, attendanceTimetableSlot.getId());
                activity.showLoadingDialog();
            } else {
                teacherAttendanceView.createBatchAttendance(url, date, "", status, createIds);
                activity.showLoadingDialog();
            }
        } else {
//            attendanceId = teacherAttendanceAdapter.positionStatusHashMap.get(studentIds.get(i)).getId();
//            url = SessionManager.getInstance().getBaseUrl() + ApiEndPoints.updateAttendance(attendanceId);
//            if (perSlot) {
//                teacherAttendanceView.updateAttendance(url, "", status, attendanceId, attendanceTimetableSlot.getId());
//            } else {
//                teacherAttendanceView.updateAttendance(url, "", status, attendanceId);
//                activity.showLoadingDialog();
//            }
        }

    }

}

