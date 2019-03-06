package trianglz.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.util.Objects;

import trianglz.components.CustomeLayoutManager;
import trianglz.models.DailyNote;
import trianglz.models.Day;
import trianglz.models.Student;
import trianglz.ui.activities.DailyNoteActivity;
import trianglz.ui.activities.WeeklyPlannerActivity;
import trianglz.ui.adapters.DayFragmentAdapter;
import trianglz.utils.Constants;

public class DayFragment extends Fragment implements DayFragmentAdapter.DayFragmentAdapterInterface {

    private RecyclerView recyclerView;
    private View rootView;
    private DayFragmentAdapter adapter;
    private  Day day;
    private Student student;


    public static Fragment newInstance(Day day,Student student) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_DAY, day);
        bundle.putSerializable(Constants.STUDENT,student);
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
        student = (Student) Objects.requireNonNull(getArguments()).getSerializable(Constants.STUDENT);
        adapter = new DayFragmentAdapter(getActivity(),this);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);
        CustomeLayoutManager customeLayoutManager = new CustomeLayoutManager(getActivity());
        customeLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(customeLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        adapter.addData(day.dailyNoteArrayList);
    }

    private void openDailyNoteActivity(DailyNote dailyNote){
        Intent intent = new Intent(getActivity(),DailyNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_DAILY_NOTE,dailyNote);
        bundle.putSerializable(Constants.STUDENT,student);
        intent.putExtra(Constants.KEY_BUNDLE,bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClicked(DailyNote dailyNote) {
        openDailyNoteActivity(dailyNote);
    }
}
