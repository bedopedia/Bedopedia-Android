package behaviorNotes.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.util.List;

import trianglz.models.BehaviorNote;

/**
 * Created by khaled on 2/27/17.
 */

public class BehaviorNotesAdapter extends ArrayAdapter {

    public Context context;

    public BehaviorNotesAdapter(Context context, int resource, List<BehaviorNote> items) {
        super(context, resource, items);
        this.context =  context;

    }

    public static  class BehaviorNotesHolder{
        TextView category;
        TextView noteContent;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        BehaviorNote note = (BehaviorNote) getItem(position);
        BehaviorNotesHolder behaviorNotesHolderItem;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_behaviour_note, parent, false);
        }
        behaviorNotesHolderItem=new BehaviorNotesHolder();

        behaviorNotesHolderItem.category = (TextView) view.findViewById(R.id.category_name);
        behaviorNotesHolderItem.noteContent = (TextView) view.findViewById(R.id.note_content);

        Typeface robotoMedium = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");

        behaviorNotesHolderItem.category.setTypeface(robotoMedium);
        behaviorNotesHolderItem.noteContent.setTypeface(robotoRegular);
        behaviorNotesHolderItem.category.setText(note.getCategory());
        behaviorNotesHolderItem.noteContent.setText(android.text.Html.fromHtml(note.getText()).toString());

        return view;
    }

}
