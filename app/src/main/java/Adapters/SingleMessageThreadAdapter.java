package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skolera.skolera_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Models.Message;
import Tools.ImageViewHelper;
import Tools.SharedPreferenceUtils;

/**
 * Created by khaled on 3/29/17.
 */

public class SingleMessageThreadAdapter extends ArrayAdapter {
    public Context context;
    final String userIdKey = "user_id";

    public SingleMessageThreadAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
        this.context =  context;
    }

    public static  class SingleMessageHolder{
        TextView body;
        ImageView avatar;
        TextView time;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Message message = (Message) getItem(position);
        SingleMessageHolder singleMessageHolderItem;


        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(context, "cur_user");
        String id = SharedPreferenceUtils.getStringValue(userIdKey, "", sharedPreferences);
        int currentUserId = Integer.parseInt(id);

        if(message.getCreator().getId() == currentUserId){
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_send_message, parent, false);

            singleMessageHolderItem=new SingleMessageHolder();
            singleMessageHolderItem.body = (TextView) view.findViewById(R.id.send_message_body);
            singleMessageHolderItem.avatar = (ImageView) view.findViewById(R.id.send_message_avatar);
            singleMessageHolderItem.time = (TextView) view.findViewById(R.id.time);

            singleMessageHolderItem.body.setText(android.text.Html.fromHtml(message.getBody()).toString());
            ImageViewHelper.getImageFromUrlWithIdFailure(context,message.getCreator().getAvatar(),singleMessageHolderItem.avatar,R.drawable.student);


            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            Date date = null;
            try {
                date = fmt.parse(message.getCreatedAt());
                SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
                String messageTime = fmtOut.format(date);
                String time [] = messageTime.split(":");
                time[0] = get12Hour(Integer.parseInt(time[0])+ 2);
                messageTime = time[0]+ ":" +time[1];
                singleMessageHolderItem.time.setText(messageTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else{
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_recieved_message, parent, false);

            singleMessageHolderItem=new SingleMessageHolder();
            singleMessageHolderItem.body = (TextView) view.findViewById(R.id.received_message_body);
            singleMessageHolderItem.avatar = (ImageView) view.findViewById(R.id.single_receive_message_avatar);
            singleMessageHolderItem.time = (TextView) view.findViewById(R.id.time);

            singleMessageHolderItem.body.setText(android.text.Html.fromHtml(message.getBody()).toString());
            ImageViewHelper.getImageFromUrlWithIdFailure(context,message.getCreator().getAvatar(),singleMessageHolderItem.avatar,R.drawable.student);

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            Date date = null;
            try {
                date = fmt.parse(message.getCreatedAt());
                SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
                String messageTime = fmtOut.format(date);
                String time [] = messageTime.split(":");
                time[0] = get12Hour(Integer.parseInt(time[0])+ 2);
                messageTime = time[0]+ ":" +time[1];
                singleMessageHolderItem.time.setText(messageTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return view;
    }

    public String get12Hour(int x) {

        if (x > 12)
            x-=12;

        return String.valueOf(x);
    }
}
