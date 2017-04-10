package attendance;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

/**
 * Created by khaled on 3/27/17.
 */

public class ExcusedAdapter extends ArrayAdapter {
    public Context context;

    public ExcusedAdapter(Context context, int resource, List<Attendance> items) {
        super(context, resource, items);
        this.context =  context;

    }

    public static  class Holder{
        TextView day;
        TextView month;
        TextView comment;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Attendance attendanceItem = (Attendance) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_excused_attendance, parent, false);
        }
        item=new Holder();

        Typeface robotoBold = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");

        item.day = (TextView) view.findViewById(R.id.day);
        item.month = (TextView) view.findViewById(R.id.month);
        item.comment = (TextView) view.findViewById(R.id.comment);

        item.day.setTypeface(robotoBold);
        item.month.setTypeface(robotoRegular);
        item.comment.setTypeface(robotoRegular);

        Calendar cal = Calendar.getInstance();
        cal.setTime(attendanceItem.getDate());
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        item.day.setText(day+"");
        item.month.setText(months[month].substring(0,3));
        if(attendanceItem.getComment() != null)
            item.comment.setText(attendanceItem.getComment());
        else
            item.comment.setText("No Comment available");

        return view;
    }
}
