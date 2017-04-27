package behaviorNotes.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import behaviorNotes.BehaviorNotesActivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import behaviorNotes.BehaviorNote;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNotesAdapter extends ArrayAdapter {

    public Context context;

    public BehaviorNotesAdapter(Context context, int resource, List<BehaviorNote> items) {
        super(context, resource, items);
        this.context =  context;

    }

    public static  class Holder{
        TextView category;
        TextView noteContent;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        BehaviorNote note = (BehaviorNote) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_behaviour_note, parent, false);
        }
        item=new Holder();

        item.category = (TextView) view.findViewById(R.id.category);
        item.noteContent = (TextView) view.findViewById(R.id.note_content);

        Typeface robotoMedium = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");

        item.category.setTypeface(robotoMedium);
        item.noteContent.setTypeface(robotoRegular);
        item.category.setText(note.getCategory());
        item.noteContent.setText(note.getText());

        return view;
    }

}
