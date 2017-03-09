package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import Models.BehaviourNote;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviourNotesAdapter extends ArrayAdapter {

    public Context context;

    public BehaviourNotesAdapter(Context context, int resource, List<BehaviourNote> items) {
        super(context, resource, items);
        this.context =  context;

    }

    public static  class Holder{
        TextView teacherName;
        TextView category;
        TextView noteContent;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        BehaviourNote note = (BehaviourNote) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_behaviour_note, parent, false);
        }
        item=new Holder();

        item.teacherName = (TextView) view.findViewById(R.id.teacher_name);
        item.category = (TextView) view.findViewById(R.id.category);
        item.noteContent = (TextView) view.findViewById(R.id.note_content);

        item.teacherName.setText(note.getTeacherName());
        item.category.setText(note.getCategory());
        item.noteContent.setText(note.getText());

        return view;
    }

}
