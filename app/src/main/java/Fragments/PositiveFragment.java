package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bedopedia.bedopedia_android.BehaviorNotesActivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import Adapters.BehaviorNotesAdapter;
import Models.BehaviorNote;

/**
 * Created by khaled on 3/9/17.
 */

public class PositiveFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.positive_fragment, container, false);

        List<BehaviorNote> positiveBehaviorNotes = BehaviorNotesActivity.positiveNotesList;
        ListView notes = (ListView) rootView.findViewById(R.id.positive_notes);
        BehaviorNotesAdapter adapter = new BehaviorNotesAdapter(BehaviorNotesActivity.context, R.layout.single_behaviour_note, positiveBehaviorNotes);
        notes.setAdapter(adapter);

        return rootView;
    }
}
