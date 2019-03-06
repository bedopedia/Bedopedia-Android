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

import com.skolera.skolera_android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import trianglz.components.CustomeLayoutManager;
import trianglz.models.BehaviorNote;
import trianglz.models.DailyNote;
import trianglz.models.Day;
import trianglz.ui.adapters.DayFragmentAdapter;
import trianglz.utils.Constants;

public class DayFragment extends Fragment {

    private RecyclerView recyclerView;
    private View rootView;
    private DayFragmentAdapter adapter;
    private  Day day;


    public static Fragment newInstance(Day day) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_DAY, day);
        fragment = new DayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate((R.layout.fragment_day), container, false);
        bindViews();
        return rootView;

    }

    private void bindViews(){
        day = (Day) Objects.requireNonNull(getArguments()).getSerializable(Constants.KEY_DAY);
        adapter = new DayFragmentAdapter(getActivity());
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(getActivity());
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        adapter.addData(day.dailyNoteArrayList);
    }
}
