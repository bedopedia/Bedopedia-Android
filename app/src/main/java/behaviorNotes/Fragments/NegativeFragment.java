package behaviorNotes.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.skolera.skolera_android.R;

import java.io.Serializable;
import java.util.List;

import behaviorNotes.Adapters.BehaviorNotesAdapter;
import trianglz.models.BehaviorNote;

/**
 * Created by khaled on 3/9/17.
 */

public class NegativeFragment extends Fragment{
    public static final String KEY_NAME = "negativeNotesList";


    public static Fragment newInstance(List<BehaviorNote> negativeBehaviorNotes){
        Fragment fragment ;
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NAME, (Serializable) negativeBehaviorNotes);
        fragment = new NegativeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.negative_fragment, container, false);

        List<BehaviorNote> negativeBehaviorNotes = ( List<BehaviorNote> ) getArguments().getSerializable(KEY_NAME);
        ListView notes = (ListView) rootView.findViewById(R.id.negative_notes);
        BehaviorNotesAdapter adapter = new BehaviorNotesAdapter(getActivity(), R.layout.item_behaviour_note, negativeBehaviorNotes);
        notes.setAdapter(adapter);

        return rootView;
    }
}
