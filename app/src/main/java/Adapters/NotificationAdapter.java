package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Models.NotificationModel;

/**
 * Created by ali on 12/02/17.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {


    private Context context;
    private String assignmentsKey = "assignments";
    private String quizzesKey = "quizzes";
    private String myDaysKey = "mydays";

    public NotificationAdapter(Context context, int resource, List<NotificationModel> items) {
        super(context, resource, items);
        this.context =   context;
    }

    public static  class Holder{
        ImageView logo;
        TextView content;
        TextView date;
        TextView studentNames;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        NotificationModel notification = (NotificationModel) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.notification_list_item, parent, false);
        }
        item=new Holder();


        ImageView logo = (ImageView) view.findViewById(R.id.notificationLogo);
        if (notification.getType().equals(assignmentsKey)){
            logo.setImageResource(R.drawable.quizzes_ico);
        } else if (notification.getType().equals(quizzesKey)) {
            logo.setImageResource(R.drawable.quizzes_ico);
        } else if (notification.getType().equals(myDaysKey)) {
            logo.setImageResource(R.drawable.mydays_ico);
        }

        item.content = (TextView) view.findViewById(R.id.notification_body);
        item.date =  (TextView) view.findViewById(R.id.notificationDate);
        item.studentNames = (TextView) view.findViewById(R.id.studentNames);

        Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        item.content.setTypeface(roboto);
        item.date.setTypeface(roboto);

        item.content.setText(notification.getContent());
        item.content.setTypeface(roboto);

        item.studentNames.setText(notification.getStudentNames());
        item.studentNames.setTypeface(roboto);

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Date date = null;
        try {
            date = fmt.parse(notification.getDate());
            SimpleDateFormat fmtOut = new SimpleDateFormat("d MMM, h:mm a");
            String [] dates = fmtOut.format(date).split(" ");
            Calendar cal = Calendar.getInstance();
            cal.setTime(Calendar.getInstance().getTime());
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            String notificationDate = fmtOut.format(date);
            if (dates[0].equals(String.valueOf(day))) {
                notificationDate = "Today" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            } else if (dates[0].equals(String.valueOf(day  - 1))) {
                notificationDate = "Yesterday" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            }
            item.date.setText(notificationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return view;
    }
}
