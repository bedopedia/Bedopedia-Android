package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.AskTeacherActivity;
import com.example.bedopedia.bedopedia_android.MessageThreadActivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.ArrayList;

import Models.MessageThread;

/**
 * Created by ali on 20/03/17.
 */

public class MessageThreadsAdapter extends ArrayAdapter

{


    private AskTeacherActivity context;
    ArrayList<MessageThread> items;

    public MessageThreadsAdapter(Context context, int resource, ArrayList<MessageThread> items) {
        super(context, resource, items);
        this.context =  (AskTeacherActivity) context;
        this.items = items;
    }

    public static  class Holder{
        //TODO refactor this
        TextView title;
        TextView lastMessage;
        TextView date;
        TextView messageCounter;
        ImageView avatar;
        LinearLayout messageThreadLayout;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        final MessageThread Message = (MessageThread) getItem(position);

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
        item.messageThreadLayout = (LinearLayout) view.findViewById(R.id.message_thread_layout);

        item.title.setText(Message.getTitle());
        item.lastMessage.setText(android.text.Html.fromHtml(Message.getLastMessage()).toString());
        // TODO Date handling
        item.date.setText(Message.getDate());
        item.messageCounter.setText(Message.getNotSeenCnt().toString());
        //TODO  avatar handling

        item.messageThreadLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MessageThreadActivity.class);
                intent.putExtra("message_thread", Message);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
