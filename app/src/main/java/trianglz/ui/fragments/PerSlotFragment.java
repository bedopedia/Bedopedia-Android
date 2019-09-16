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

import java.util.ArrayList;

import trianglz.ui.activities.StudentMainActivity;
import trianglz.ui.adapters.PerSlotAdapter;

/**
 * Created by Farah A. Moniem on 15/09/2019.
 */
public class PerSlotFragment extends Fragment implements View.OnClickListener , PerSlotAdapter.SlotAdapterInterface {

    StudentMainActivity activity;
    View rootView;
    RecyclerView recyclerView;
    Button applyButton;
    ImageButton closeButton;
    PerSlotAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (StudentMainActivity) getActivity();
        rootView = inflater.inflate(R.layout.layout_attendance_slots, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindViews();
        setListeners();
    }

    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        applyButton = rootView.findViewById(R.id.apply_btn);
        closeButton = rootView.findViewById(R.id.close_btn);
        adapter = new PerSlotAdapter(activity,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addData(getFakeData());
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        applyButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
    }

    private ArrayList<String> getFakeData() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            stringArrayList.add("Slot " + i);
        }
        return stringArrayList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_btn:
                break;
            case R.id.close_btn:
                getParentFragment().getChildFragmentManager().popBackStack();
                break;
        }

    }

    @Override
    public void onSlotClicked(int position) {

    }
}
