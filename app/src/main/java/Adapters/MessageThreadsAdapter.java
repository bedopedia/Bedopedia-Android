package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import Models.AskTeacherMessage;

/**
 * Created by ali on 20/03/17.
 */

public class MessageThreadsAdapter extends ArrayAdapter

{


    private Context context;
    ArrayList<AskTeacherMessage> items;

    public MessageThreadsAdapter(Context context, int resource, ArrayList<AskTeacherMessage> items) {
        super(context, resource, items);
        this.context =   context;
        this.items = items;
    }

    public static  class Holder{
        //TODO refactor this
        TextView name;
        TextView grade;
        TextView comment;
        TextView average;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ArrayList<String> courseItem = ( ArrayList<String>) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        MessageThreadsAdapter.Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_view_item, parent, false);
        }
        item=new MessageThreadsAdapter.Holder();

        //TODO assigning data



        return view;
    }
}
