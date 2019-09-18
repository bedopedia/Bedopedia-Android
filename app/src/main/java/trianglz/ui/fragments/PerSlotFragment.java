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
import android.widget.Button;
import android.widget.ImageButton;

import com.skolera.skolera_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import trianglz.models.AttendanceTimetableSlot;
import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.PerSlotAdapter;
import trianglz.utils.Constants;

/**
 * Created by Farah A. Moniem on 15/09/2019.
 */
public class PerSlotFragment extends Fragment implements View.OnClickListener, PerSlotAdapter.SlotAdapterInterface {

    private StudentMainActivity activity;
    private View rootView;
    private RecyclerView recyclerView;
    private Button applyButton;
    private ImageButton closeButton;
    private PerSlotAdapter adapter;
    private AttendanceTimetableSlot timtableSlotSelected;
    private boolean slotSelected = false;
    private SelectSlotInterface selectSlotInterface;
    private ArrayList<AttendanceTimetableSlot> attendanceTimetableSlots;

    public static PerSlotFragment newInstance(SelectSlotInterface selectSlotInterface) {
        PerSlotFragment perSlotFragment = new PerSlotFragment();
        perSlotFragment.selectSlotInterface = selectSlotInterface;
        return perSlotFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.layout_attendance_slots, container, false);
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
            attendanceTimetableSlots = (ArrayList<AttendanceTimetableSlot>) bundle.getSerializable(Constants.TIMETABLE_SLOTS);
        }
    }

    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        applyButton = rootView.findViewById(R.id.apply_btn);
        closeButton = rootView.findViewById(R.id.close_btn);
        adapter = new PerSlotAdapter(activity, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        validateTodaySlots();
        adapter.addData(attendanceTimetableSlots);
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        applyButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
    }

    private void validateTodaySlots() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEEE");
        String formattedDate = df.format(c);
        for (int i = 0; i < attendanceTimetableSlots.size(); i++) {
            if (!attendanceTimetableSlots.get(i).getDay().toLowerCase().equals(formattedDate.toLowerCase())) {
                attendanceTimetableSlots.remove(i);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_btn:
                if (slotSelected == true) {
                    selectSlotInterface.onApplySelected(timtableSlotSelected);
                    getParentFragment().getChildFragmentManager().popBackStack();
                } else {
                    getParentFragment().getChildFragmentManager().popBackStack();
                }
                break;
            case R.id.close_btn:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }

    }

    @Override
    public void onSlotClicked(AttendanceTimetableSlot timtableSlot) {
        slotSelected = true;
        this.timtableSlotSelected = timtableSlot;
    }

    public interface SelectSlotInterface {
        void onApplySelected(AttendanceTimetableSlot timetableSlot);
    }
}
