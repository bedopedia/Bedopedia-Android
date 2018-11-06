package behaviorNotes.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skolera.skolera_android.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import trianglz.models.BehaviorNote;
import trianglz.ui.activities.adapters.BehaviourNotesAdapter;

/**
 * Created by khaled on 3/9/17.
 */

public class PositiveFragment extends Fragment {

    public static final String KEY_NAME = "positiveNotesList";
    private RecyclerView recyclerView;
    private BehaviourNotesAdapter adapter;
    private View rootView;
    private ArrayList<BehaviorNote> positiveBehaviorNotes;
    public static Fragment newInstance(List<BehaviorNote> positiveBehaviorNotes) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NAME, (Serializable) positiveBehaviorNotes);
        fragment = new PositiveFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.positive_fragment, container, false);
        positiveBehaviorNotes = (ArrayList<BehaviorNote>) getArguments().getSerializable(KEY_NAME);
        bindViews();
        return rootView;
    }

    private void bindViews() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new BehaviourNotesAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.addData(positiveBehaviorNotes);

    }

}
