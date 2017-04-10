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
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.Message;
import Services.ApiClient;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by khaled on 3/29/17.
 */

public class SingleMessageThreadAdapter extends ArrayAdapter {
    public Context context;

    public SingleMessageThreadAdapter(Context context, int resource, List<Message> items) {
        super(context, resource, items);
        this.context = (Context) context;
    }

    public static  class Holder{
        TextView body;
        ImageView avatar;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Message message = (Message) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view

        SharedPreferences sharedPreferences = context.getSharedPreferences("cur_user", MODE_PRIVATE);
        String id = sharedPreferences.getString("user_id", "");
        int currentUserId = Integer.parseInt(id);


        if(message.getCreator().getId() == currentUserId){

            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.single_send_message, parent, false);
            }

            item=new Holder();

            item.body = (TextView) view.findViewById(R.id.send_message_body);
            item.avatar = (ImageView) view.findViewById(R.id.send_message_avatar);

            item.body.setText(android.text.Html.fromHtml(message.getBody()).toString());
            Picasso.with(context).load(ApiClient.BASE_URL+message.getCreator().getAvatar()).into(item.avatar);

        }else{
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.single_recieved_message, parent, false);
            }
            item=new Holder();

            item.body = (TextView) view.findViewById(R.id.received_message_body);
            item.avatar = (ImageView) view.findViewById(R.id.receive_message_avatar);

            item.body.setText(android.text.Html.fromHtml(message.getBody()).toString());
            Picasso.with(context).load(ApiClient.BASE_URL+message.getCreator().getAvatar()).into(item.avatar);
        }
        return view;
    }
}
