package trianglz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.ArrayList;

import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.TeacherAttendanceAdapter;

/**
 * Created by Farah A. Moniem on 09/09/2019.
 */
public class TeacherAttendanceFragment extends Fragment implements View.OnClickListener, TeacherAttendanceAdapter.TeacherAttendanceAdapterInterface {


    private StudentMainActivity activity;
    private View rootView;
    private Button fullDayButton, perSlotButton, assignAllButton, assignSelectedButton;
    private TextView todaysDate;
    private View fullDayView, perSlotView;
    private ImageButton backButton;
    private RecyclerView recyclerView;
    private TeacherAttendanceAdapter teacherAttendanceAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.activity_teacher_attendance, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
        setListeners();
     //   onBackPress();
    }

    private void bindViews() {
        fullDayButton = rootView.findViewById(R.id.full_day_btn);
        perSlotButton = rootView.findViewById(R.id.per_slot_btn);
        fullDayView = rootView.findViewById(R.id.full_day_view);
        perSlotView = rootView.findViewById(R.id.per_slot_view);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        backButton = rootView.findViewById(R.id.btn_back);
        assignAllButton = rootView.findViewById(R.id.assign_all_btn);
        assignSelectedButton = rootView.findViewById(R.id.assign_selected_btn);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        teacherAttendanceAdapter = new TeacherAttendanceAdapter(activity, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        teacherAttendanceAdapter.addData(getFakeData());
        recyclerView.setAdapter(teacherAttendanceAdapter);
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
        perSlotView.setVisibility(View.VISIBLE);
        fullDayView.setVisibility(View.INVISIBLE);
        perSlotButton.setTextColor(getResources().getColor(R.color.cerulean_blue, null));
        fullDayButton.setTextColor(getResources().getColor(R.color.greyish, null));
    }

    private ArrayList<String> getFakeData() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            stringArrayList.add("Test " + i);
        }
        return stringArrayList;
    }

    private void onBackPress() {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    activity.getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                activity.getSupportFragmentManager().popBackStack();
                break;
            case R.id.full_day_btn:
                showFullDayAttendance();
                break;
            case R.id.per_slot_btn:
                showPerSlotAttendance();
                break;
        }
    }

    @Override
    public void onAbsentClicked() {

    }

    @Override
    public void onPresentClicked() {

    }

    @Override
    public void onExcusedClicked() {

    }

    @Override
    public void onLateClicked() {

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
}

