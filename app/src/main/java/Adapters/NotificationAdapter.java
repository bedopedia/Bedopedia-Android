package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import trianglz.models.Notification;
import Tools.CalendarUtils;

/**
 * Created by ali on 12/02/17.
 */

public class NotificationAdapter extends ArrayAdapter<Notification> {


    private Context context;
    final private String assignmentsKey = "assignments";
    final private String quizzesKey = "quizzes";
    final private String myDaysKey = "mydays";

    public NotificationAdapter(Context context, int resource, List<Notification> items) {
        super(context, resource, items);
        this.context =   context;
    }

    public static  class NotificationHolder{
        ImageView logo;
        TextView content;
        TextView date;
        TextView studentNames;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Notification notification = (Notification) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        NotificationHolder notificationHolderItem;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.notification_list_item, parent, false);
        }
        notificationHolderItem=new NotificationHolder();


        notificationHolderItem.logo = (ImageView) view.findViewById(R.id.notificationLogo);
        if (notification.getType().equals("quizzes.graded") || notification.getType().equals("assignments.graded")){
            notificationHolderItem.logo.setImageResource(R.drawable.grades);
        } else if (notification.getType().equals("assignments.updated")
                || notification.getType().equals("assignments.published")
                || notification.getType().equals("assignments.deleted")
                || notification.getType().equals("assignments.submission")
                || notification.getType().equals("assignments.upcoming_today")) {
            notificationHolderItem.logo.setImageResource(R.drawable.assignments);
        } else if (notification.getType().equals("quizzes.created")
                || notification.getType().equals("quizzes.deleted")
                || notification.getType().equals("quizzes.submission")
                || notification.getType().equals("quizzes.upcoming_today")) {
            notificationHolderItem.logo.setImageResource(R.drawable.quizzes_ico);
        } else if (notification.getType().equals("zones.event_created")
                || notification.getType().equals("zones.became_manager")
                || notification.getType().equals("zones.joined")) {
            notificationHolderItem.logo.setImageResource(R.drawable.zones);
        } else if (notification.getType().equals("events.upcoming_today")) {
            notificationHolderItem.logo.setImageResource(R.drawable.mydays);
        } else if (notification.getType().equals("virtual_room.notify_parents")) {
            notificationHolderItem.logo.setImageResource(R.drawable.virtualclass);
        }

        notificationHolderItem.content = (TextView) view.findViewById(R.id.notification_body);
        notificationHolderItem.date =  (TextView) view.findViewById(R.id.notificationDate);
        notificationHolderItem.studentNames = (TextView) view.findViewById(R.id.studentNames);

        Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        notificationHolderItem.content.setTypeface(roboto);
        notificationHolderItem.date.setTypeface(roboto);

        notificationHolderItem.content.setText(notification.getMessage());
        notificationHolderItem.content.setTypeface(roboto);

        notificationHolderItem.studentNames.setText(notification.getStudentNames());
        notificationHolderItem.studentNames.setTypeface(roboto);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Date date = null;
        try {
            date = fmt.parse(notification.getDate());
            SimpleDateFormat fmtOut = new SimpleDateFormat("d MMM, h:mm a");
            String [] dates = fmtOut.format(date).split(" ");
            Calendar cal = CalendarUtils.getCalendar(Calendar.getInstance().getTime());
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            String notificationDate = fmtOut.format(date);
            if (dates[0].equals(String.valueOf(day))) {
                notificationDate = "Today" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            } else if (dates[0].equals(String.valueOf(day  - 1))) {
                notificationDate = "Yesterday" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            }
            notificationHolderItem.date.setText(notificationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }
}
