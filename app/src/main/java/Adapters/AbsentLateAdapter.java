package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.bedopedia.bedopedia_android.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import Models.Attendance;


/**
 * Created by khaled on 2/22/17.
 */

public class AbsentLateAdapter extends ArrayAdapter {

    public Context context;

    public AbsentLateAdapter(Context context, int resource, List<Attendance> items) {
        super(context, resource, items);
        this.context =  context;

    }

    public static  class Holder{
        TextView dateImage;
        TextView dateText;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Attendance attendanceItem = (Attendance) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_attendance, parent, false);
        }
        item=new Holder();

        item.dateImage = (TextView) view.findViewById(R.id.date_image);
        item.dateText = (TextView) view.findViewById(R.id.date_text);
        Calendar cal = Calendar.getInstance();
        cal.setTime(attendanceItem.getDate());
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        item.dateImage.setText(day + "\n" + months[month].substring(0,3));
        if(attendanceItem.getComment() != null)
            item.dateText.setText(attendanceItem.getComment());
        else
            item.dateText.setText("No Comment available");

        return view;
    }
}
