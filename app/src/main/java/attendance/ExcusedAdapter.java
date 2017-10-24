package attendance;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import Tools.CalendarUtils;

/**
 * Created by khaled on 3/27/17.
 */

public class ExcusedAdapter extends RecyclerView.Adapter<ExcusedAdapter.Holder> {
    static public Context context;
    int resource ;
    List<Attendance> items ;


    public ExcusedAdapter(Context context, int resource, List<Attendance> items) {
        this.context =  context;
        this.resource = resource ;
        this.items = items ;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(resource,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        Attendance attendanceItem = items.get(position);
        Calendar cal = CalendarUtils.getCalendar(attendanceItem.getDate());
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        holder.day.setText(day+"");
        holder.month.setText(months[month].substring(0,3));
        if(attendanceItem.getComment() != null)
            holder.comment.setText(attendanceItem.getComment());
        else
            holder.comment.setText("No Comment available");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static  class Holder extends RecyclerView.ViewHolder{

        public TextView day , month , comment ;

        public Holder(View itemView) {
            super(itemView);
            Typeface robotoBold = Typeface.createFromAsset(ExcusedAdapter.context.getAssets(), "font/Roboto-Bold.ttf");
            Typeface robotoRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            day = (TextView) itemView.findViewById(R.id.single_execused_day_view);
            month = (TextView) itemView.findViewById(R.id.single_execused_month_view);
            comment = (TextView) itemView.findViewById(R.id.single_execused_comment_view);
            day.setTypeface(robotoBold);
            month.setTypeface(robotoRegular);
            comment.setTypeface(robotoRegular);
        }
    }
}