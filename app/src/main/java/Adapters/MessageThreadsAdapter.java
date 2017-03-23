package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import Models.AskTeacherMessage;

/**
 * Created by ali on 20/03/17.
 */

public class MessageThreadsAdapter extends ArrayAdapter

{


    private Context context;
    ArrayList<AskTeacherMessage> items;

    public MessageThreadsAdapter(Context context, int resource, ArrayList<AskTeacherMessage> items) {
        super(context, resource, items);
        this.context =   context;
        this.items = items;
    }

    public static  class Holder{
        //TODO refactor this
        TextView title;
        TextView lastMessage;
        TextView date;
        TextView messageCounter;
        ImageView avatar;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        AskTeacherMessage Message = ( AskTeacherMessage) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        MessageThreadsAdapter.Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.ask_teacher_category_list_item, parent, false);
        }
        item=new MessageThreadsAdapter.Holder();

        item.title = (TextView) view.findViewById(R.id.ask_teacher_title);
        item.lastMessage = (TextView) view.findViewById(R.id.ask_teacher_last_message);
        item.date = (TextView) view.findViewById(R.id.ask_teacher_date);
        item.messageCounter = (TextView) view.findViewById(R.id.ask_teacher_message_number);
        item.avatar = (ImageView) view.findViewById(R.id.ask_teacher_user_avatar);

        item.title.setText(Message.getTitle());
        item.lastMessage.setText(Message.getLastMessage());
        // TODO Date handling
        item.date.setText(Message.getDate());
        item.messageCounter.setText(Message.getNotSeenCnt().toString());
        //TODO  avatar handling

        return view;
    }
}
