package trianglz.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
            holder.lineView.setBackground(context.getResources().getDrawable(R.drawable.curved_squash));
        } else if (state == STATE.LATE) {
            holder.lineView.setBackground(context.getResources().getDrawable(R.drawable.curved_purple_vivid));
        } else {
            holder.lineView.setBackground(context.getResources().getDrawable(R.drawable.curved_orange_red));
        }
        Attendance attendanceItem = items.get(position);
        Calendar cal = CalendarUtils.getCalendar(attendanceItem.getDate());
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("en"));
        String[] months = dfs.getMonths();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        holder.dayNumberTextView.setText(day + "");
        holder.monthTextView.setText(months[month].substring(0, 3));
        if (attendanceItem.getComment() != null && !attendanceItem.getComment().trim().isEmpty()) {
            holder.commentTextView.setText(attendanceItem.getComment());
        } else {
            holder.commentTextView.setText(context.getResources().getString(R.string.no_description));
        }
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