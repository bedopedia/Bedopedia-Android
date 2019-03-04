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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import trianglz.models.DailyNote;
import trianglz.ui.adapters.DayFragmentAdapter;

public class DayFragment extends Fragment {

    private RecyclerView recyclerView;
    private View rootView;
    private DayFragmentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate((R.layout.fragment_day), container, false);
        bindViews();
        return rootView;

    }
    private void bindViews(){
        adapter = new DayFragmentAdapter(getActivity());
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        adapter.addData(createDummyData());
    }

    private List<DailyNote> createDummyData() {
        List<DailyNote> list = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            DailyNote note = new DailyNote("Arabic" + i, "",
                    "", "", new Date(),"");
            list.add(note);
        }
        return list;
    }
}
