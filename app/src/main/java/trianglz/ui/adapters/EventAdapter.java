package trianglz.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.skolera.skolera_android.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import trianglz.managers.SessionManager;
import trianglz.models.Event;
import trianglz.utils.Util;

/**
 * Created by Farah A. Moniem on 25/07/2019.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    public Context context;
    List<Event> items;
    public EventAdapter.EVENTSTATE state;
    public EventInterface eventInterface;

    public EventAdapter(Context context, EVENTSTATE eventstate, EventInterface eventInterface) {
        this.context = context;
        this.state = eventstate;
        this.items = new ArrayList<>();
        this.eventInterface = eventInterface;
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
        Log.d("TAG", "onBindViewHolder: " + event);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime;
        if(SessionManager.getInstance().getHeaderHashMap().containsKey("timezone")){
            String timeZone = SessionManager.getInstance().getHeaderHashMap().get("timezone");
            DateTimeZone zone = DateTimeZone.forID(timeZone);
            dateTime = fmt.parseDateTime(event.getStartDate()).withZoneRetainFields(zone);
        }else {
            dateTime = fmt.parseDateTime(event.getStartDate());
        }

        if (Util.getLocale(context).equals("ar")) {
            holder.eventDay.setText(String.format(new Locale("ar"), dateTime.getDayOfMonth() + ""));
            holder.eventMonth.setText(String.format(new Locale("ar"), getMonthName(dateTime.getMonthOfYear()) + ""));
        } else {
            holder.eventDay.setText(dateTime.getDayOfMonth()+"");
            holder.eventMonth.setText(getMonthName(dateTime.getMonthOfYear()).substring(0,3));
        }

        holder.eventTitle.setText(event.getTitle());
        if (event.getDescription() != null && !(TextUtils.isEmpty(event.getDescription()))) {
            holder.eventDetails.setVisibility(View.VISIBLE);
            holder.eventDetails.setText(event.getDescription());
        } else {
            holder.eventDetails.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventInterface.onEventClicked(event.getDescription());
            }
        });
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
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.tv_event_title);
            eventDetails = itemView.findViewById(R.id.tv_event_detail);
            eventDay = itemView.findViewById(R.id.tv_day_number);
            eventMonth = itemView.findViewById(R.id.tv_month_number);
            lineView = itemView.findViewById(R.id.view_line);
            cardView =itemView.findViewById(R.id.card_view);
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

    public interface EventInterface{
        public void onEventClicked(String zoomUrl);
    }
}

