package trianglz.ui.fragments;


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
import java.util.Objects;

import trianglz.models.BehaviorNote;
import trianglz.ui.adapters.BehaviourNotesAdapter;
import trianglz.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends Fragment{
    private RecyclerView recyclerView;
    private BehaviourNotesAdapter adapter;
    private View rootView;
    private ArrayList<BehaviorNote> otherBehaviorNotes;

    public static Fragment newInstance(List<BehaviorNote> otherBehaviorNotes){
        Fragment fragment ;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_OTHER_NOTES_LIST, (Serializable) otherBehaviorNotes);
        fragment = new OtherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_other, container, false);
        bindViews();
        return rootView;
    }
    private void bindViews() {
        //noinspection unchecked
        otherBehaviorNotes = (ArrayList<BehaviorNote>) Objects.requireNonNull(getArguments()).getSerializable(Constants.KEY_OTHER_NOTES_LIST);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new BehaviourNotesAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.addData(otherBehaviorNotes);

    }
}
