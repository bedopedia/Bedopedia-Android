package Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

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
        this.context = (Context) context;
    }

    public static  class SingleMessageHolder{
        TextView body;
        ImageView avatar;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Message message = (Message) getItem(position);
        SingleMessageHolder singleMessageHolderItem;

        // Check if an existing view is being reused, otherwise inflate the view

        SharedPreferences sharedPreferences = SharedPreferenceUtils.getSharedPreference(context, "cur_user");
        String id = SharedPreferenceUtils.getStringValue(userIdKey, "", sharedPreferences);
        int currentUserId = Integer.parseInt(id);


        if(message.getCreator().getId() == currentUserId){

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.single_send_message, parent, false);
            }

            singleMessageHolderItem=new SingleMessageHolder();

            singleMessageHolderItem.body = (TextView) view.findViewById(R.id.send_message_body);
            singleMessageHolderItem.avatar = (ImageView) view.findViewById(R.id.send_message_avatar);

            singleMessageHolderItem.body.setText(android.text.Html.fromHtml(message.getBody()).toString());
            ImageViewHelper.getImageFromUrl(context,message.getCreator().getAvatar(),singleMessageHolderItem.avatar);

        }else{
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.single_recieved_message, parent, false);
            }
            singleMessageHolderItem=new SingleMessageHolder();

            singleMessageHolderItem.body = (TextView) view.findViewById(R.id.received_message_body);
            singleMessageHolderItem.avatar = (ImageView) view.findViewById(R.id.single_receive_message_avatar);


            singleMessageHolderItem.body.setText(android.text.Html.fromHtml(message.getBody()).toString());
            ImageViewHelper.getImageFromUrl(context,message.getCreator().getAvatar(),singleMessageHolderItem.avatar);
        }
        return view;
    }
}
