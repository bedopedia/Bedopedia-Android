package Adapters;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.AttendanceActivity;
import com.example.bedopedia.bedopedia_android.MyKidsActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Models.Student;
import Services.ApiClient;
import Tools.UIUtils;

/**
 * Created by khaled on 2/22/17.
 */

public class AbsentLateAdapter extends ArrayAdapter {

    public Context context;

    public AbsentLateAdapter(Context context, int resource, List<Date> items) {
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
        Date date = (Date) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_attendance, parent, false);
        }
        item=new Holder();

        item.dateImage = (TextView) view.findViewById(R.id.date_image);
        item.dateText = (TextView) view.findViewById(R.id.date_text);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        item.dateImage.setText(day + "\n" + months[month]);
        item.dateText.setText(date.toString());

        return view;
    }
}
