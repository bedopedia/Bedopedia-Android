package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.bedopedia.bedopedia_android.R;

import java.util.Date;
import java.util.List;

/**
 * Created by khaled on 3/2/17.
 */

public class EventsAdapter extends ArrayAdapter<Date>{

    public Context context;

    public EventsAdapter(Context context, int resource, List<Date> items) {
        super(context, resource, items);
        this.context =   context;
    }

    public static  class EventsHolder{
        TextView hour;
        TextView AmPm;
        LinearLayout eventsLL;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Date date = (Date) getItem(position);
        EventsHolder eventsHolderItem;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_day, parent, false);
        }
        eventsHolderItem=new EventsHolder();

        eventsHolderItem.hour = (TextView) view.findViewById(R.id.hourTV);
        eventsHolderItem.AmPm = (TextView) view.findViewById(R.id.amTV);
        eventsHolderItem.hour.setTextColor(Color.BLUE);
        eventsHolderItem.AmPm.setTextColor(Color.BLUE);
        return view;
    }
}
