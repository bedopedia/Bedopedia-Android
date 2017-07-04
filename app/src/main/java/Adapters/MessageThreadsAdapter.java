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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Models.MessageThread;
import Tools.CalendarUtils;
import Tools.ImageViewHelper;

/**
 * Created by ali on 20/03/17.
 */

public class MessageThreadsAdapter extends ArrayAdapter

{


    private AskTeacherActivity context;
    ArrayList<MessageThread> items;
    final String messageThreadKey = "message_thread";

    public MessageThreadsAdapter(Context context, int resource, ArrayList<MessageThread> items) {
        super(context, resource, items);
        this.context =  (AskTeacherActivity) context;
        this.items = items;
    }

    public static  class MessageThreadHolder{
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
        MessageThreadsAdapter.MessageThreadHolder messageThreadHolderItem;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.ask_teacher_category_list_item, parent, false);
        }
        messageThreadHolderItem=new MessageThreadsAdapter.MessageThreadHolder();

        messageThreadHolderItem.title = (TextView) view.findViewById(R.id.ask_teacher_title);
        messageThreadHolderItem.lastMessage = (TextView) view.findViewById(R.id.ask_teacher_last_message);
        messageThreadHolderItem.date = (TextView) view.findViewById(R.id.ask_teacher_date);
        messageThreadHolderItem.messageCounter = (TextView) view.findViewById(R.id.ask_teacher_message_number);
        messageThreadHolderItem.avatar = (ImageView) view.findViewById(R.id.ask_teacher_user_avatar);
        messageThreadHolderItem.messageThreadLayout = (LinearLayout) view.findViewById(R.id.message_thread_layout);

        messageThreadHolderItem.title.setText(Message.getTitle());
        messageThreadHolderItem.lastMessage.setText(android.text.Html.fromHtml(Message.getLastMessage()).toString());
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        Date date = null;
        try {
            date = fmt.parse(Message.getDate());
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd/mm/yyyy");
            String [] dates = fmtOut.format(date).split(" ");
            Calendar cal = CalendarUtils.getCalendar(Calendar.getInstance().getTime());
            Integer day = cal.get(Calendar.DAY_OF_MONTH);
            String notificationDate = fmtOut.format(date);
             if (dates[0].equals(String.valueOf(day))) {
                notificationDate = "Today" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            } else if (dates[0].equals(String.valueOf(day  - 1))) {
                notificationDate = "Yesterday" + notificationDate.substring(dates[0].length()+dates[1].length()+1);
            }else {
                 messageThreadHolderItem.date.setTextColor(context.getResources().getColor(R.color.Manatee));
             }
            messageThreadHolderItem.date.setText(notificationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        messageThreadHolderItem.messageCounter.setText(Message.getNotSeenCnt().toString());
        ImageViewHelper.getImageFromUrl(context, Message.getImagePath(),messageThreadHolderItem.avatar);

        messageThreadHolderItem.messageThreadLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent messageThreadIntent = new Intent(context, MessageThreadActivity.class);
                messageThreadIntent.putExtra(messageThreadKey, Message);
                context.startActivity(messageThreadIntent);
            }
        });

        return view;
    }
}
