package trianglz.ui.activities.fragments;

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
import trianglz.ui.activities.adapters.BehaviourNotesAdapter;
import trianglz.utils.Constants;

/**
 * Created by khaled on 3/9/17.
 */
/**
 * modified by gemy
 */

public class NegativeFragment extends Fragment{
    private RecyclerView recyclerView;
    private BehaviourNotesAdapter adapter;
    private View rootView;
    private ArrayList<BehaviorNote> negativeBehaviourNotes;

    public static Fragment newInstance(List<BehaviorNote> negativeBehaviorNotes){
        Fragment fragment ;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_NEGATIVE_NOTES_LIST, (Serializable) negativeBehaviorNotes);
        fragment = new NegativeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.negative_fragment, container, false);
        bindViews();
        return rootView;
    }
    private void bindViews() {
        //noinspection unchecked
        negativeBehaviourNotes = (ArrayList<BehaviorNote>) Objects.requireNonNull(getArguments()).getSerializable(Constants.KEY_NEGATIVE_NOTES_LIST);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        adapter = new BehaviourNotesAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter.addData(negativeBehaviourNotes);

    }
}
