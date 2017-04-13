package behaviorNotes.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import behaviorNotes.BehaviorNotesActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.example.bedopedia.bedopedia_android.StudentActivity;

import java.io.Serializable;
import java.util.List;

import behaviorNotes.Adapters.BehaviorNotesAdapter;
import behaviorNotes.BehaviorNote;

/**
 * Created by khaled on 3/9/17.
 */

public class PositiveFragment extends Fragment{

    public static final String KEY_NAME = "positiveNotesList";

    public static Fragment newInstance(List<BehaviorNote> positiveBehaviorNotes){
        Fragment fragment ;
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_NAME, (Serializable) positiveBehaviorNotes);
        fragment = new PositiveFragment();
        fragment.setArguments(bundle);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.positive_fragment, container, false);

        List<BehaviorNote> positiveBehaviorNotes = ( List<BehaviorNote> ) getArguments().getSerializable(KEY_NAME);
        ListView notes = (ListView) rootView.findViewById(R.id.positive_notes);
        BehaviorNotesAdapter adapter = new BehaviorNotesAdapter(BehaviorNotesActivity.context, R.layout.single_behaviour_note, positiveBehaviorNotes);
        notes.setAdapter(adapter);

        return rootView;
    }
}
