package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;


import java.util.List;

import Models.NotificationModel;

/**
 * Created by ali on 12/02/17.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationModel> {


    private Context context;

    public NotificationAdapter(Context context, int resource, List<NotificationModel> items) {
        super(context, resource, items);
        this.context =   context;
    }

    public static  class Holder{
        ImageView logo;
        TextView content;
        TextView date;
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

        //item.logo = (ImageView) view.findViewById(R.id.notificationLogo);
        item.content = (TextView) view.findViewById(R.id.notificationContent);
        item.date =  (TextView) view.findViewById(R.id.notificationDate);

        item.content.setText(notification.getContent());
        item.date.setText(notification.getDate());


        return view;
    }
}
