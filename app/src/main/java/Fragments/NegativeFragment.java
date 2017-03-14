package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bedopedia.bedopedia_android.BehaviorNotesActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.example.bedopedia.bedopedia_android.StudentActivity;

import java.util.List;

import Adapters.BehaviorNotesAdapter;
import Models.BehaviorNote;

/**
 * Created by khaled on 3/9/17.
 */

public class NegativeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.negative_fragment, container, false);

        List<BehaviorNote> negativeBehaviorNotes = StudentActivity.negativeNotesList;
        ListView notes = (ListView) rootView.findViewById(R.id.negative_notes);
        BehaviorNotesAdapter adapter = new BehaviorNotesAdapter(BehaviorNotesActivity.context, R.layout.single_behaviour_note, negativeBehaviorNotes);
        notes.setAdapter(adapter);

        return rootView;
    }
}
