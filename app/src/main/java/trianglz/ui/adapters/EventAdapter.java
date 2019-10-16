package trianglz.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trianglz.models.Event;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 25/07/2019.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public Context context;
    List<Event> items;
    public EventAdapter.EVENTSTATE state;

    public EventAdapter(Context context, EVENTSTATE eventstate) {
        this.context = context;
        this.state = eventstate;
        this.items = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (state == EVENTSTATE.ALL) {
            holder.lineView.setBackgroundResource(R.color.black);
        } else if (state == EVENTSTATE.ACADEMIC) {
            holder.lineView.setBackgroundResource(R.color.butterscotch);
        } else if (state == EVENTSTATE.EVENTS) {
            holder.lineView.setBackgroundResource(R.color.turquoise_blue);
        } else if (state == EVENTSTATE.VACATIONS) {
            holder.lineView.setBackgroundResource(R.color.soft_green);
        } else {
            holder.lineView.setBackgroundResource(R.color.iris);
        }
        Event event = items.get(position);
        DateTime dateTime = new DateTime(event.getStartDate());

        if (Util.getLocale(context).equals("ar")) {
            holder.eventDay.setText(String.format(new Locale("ar"), dateTime.getDayOfMonth() + ""));
            holder.eventMonth.setText(String.format(new Locale("ar"), getMonthName(dateTime.getMonthOfYear()) + ""));
        } else {
            holder.eventDay.setText(dateTime.getDayOfMonth() + "");
            holder.eventMonth.setText(getMonthName(dateTime.getMonthOfYear()) + "");
        }

        holder.eventTitle.setText(event.getTitle());
        if (event.getDescription() != null && !(TextUtils.isEmpty(event.getDescription()))) {
            holder.eventDetails.setVisibility(View.VISIBLE);
            holder.eventDetails.setText(event.getDescription());
        } else {
            holder.eventDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addData(ArrayList<Event> eventList, EVENTSTATE state) {
        this.state = state;
        this.items.clear();
        this.items.addAll(eventList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle, eventDetails, eventDay, eventMonth;
        public View lineView;

        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.tv_event_title);
            eventDetails = itemView.findViewById(R.id.tv_event_detail);
            eventDay = itemView.findViewById(R.id.tv_day_number);
            eventMonth = itemView.findViewById(R.id.tv_month_number);
            lineView = itemView.findViewById(R.id.view_line);
        }
    }

    public enum EVENTSTATE {
        ALL, ACADEMIC, EVENTS, VACATIONS, PERSONAL, QUIZZES, ASSIGNMENTS
    }


    String getMonthName(int m) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        return months[m-1];
    }
}

