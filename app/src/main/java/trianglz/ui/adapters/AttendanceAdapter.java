package trianglz.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Tools.CalendarUtils;
import attendance.Attendance;

/**
 * Created by ${Aly} on 11/5/2018.
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.Holder> {
    public Context context;
    List<Attendance> items;
    public STATE state;


    public AttendanceAdapter(Context context, STATE state) {
        this.context = context;
        this.items = new ArrayList<>();
        this.state = state;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_attendance, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (state == STATE.EXCUSED) {
            holder.lineView.setBackgroundColor(context.getResources().getColor(R.color.squash));
        } else if (state == STATE.LATE) {
            holder.lineView.setBackgroundColor(context.getResources().getColor(R.color.vivid_purple));
        } else {
            holder.lineView.setBackgroundColor(context.getResources().getColor(R.color.orange_red));
        }
        Attendance attendanceItem = items.get(position);
        Calendar cal = CalendarUtils.getCalendar(attendanceItem.getDate());
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        holder.dayNumberTextView.setText(day + "");
        holder.monthTextView.setText(months[month].substring(0, 3));
        if (attendanceItem.getComment() != null)
            holder.commentTextView.setText(attendanceItem.getComment());
        else
            holder.commentTextView.setText("");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(List<Attendance> attendanceList, STATE state) {
        this.state = state;
        this.items.clear();
        this.items.addAll(attendanceList);
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView dayNumberTextView, monthTextView, commentTextView;
        public View lineView;

        public Holder(View itemView) {
            super(itemView);
            dayNumberTextView = itemView.findViewById(R.id.tv_day_number);
            monthTextView = itemView.findViewById(R.id.tv_month);
            commentTextView = itemView.findViewById(R.id.tv_comment);
            lineView = itemView.findViewById(R.id.view_line);
        }
    }


    public enum STATE {
        LATE, EXCUSED, ABSENT
    }

}