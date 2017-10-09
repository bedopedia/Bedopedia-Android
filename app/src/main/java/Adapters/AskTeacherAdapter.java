package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.ArrayList;
import java.util.Collections;

import Models.MessageThread;
import Tools.UIUtils;

/**
 * Created by ali on 20/03/17.
 */

public class AskTeacherAdapter  extends ArrayAdapter {


    private Context context;
    private ArrayList<String> header;
    ArrayList<ArrayList<MessageThread>> items;

    public AskTeacherAdapter(Context context, int resource, ArrayList<ArrayList<MessageThread>> items, ArrayList<String> header) {
        super(context, resource, items);
        this.context =   context;
        this.header = header;
        this.items = items;
    }

    public static  class Holder{
        TextView categoryName;
        ListView categoryData;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ArrayList<MessageThread> MessageCategory= (ArrayList<MessageThread>) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view



        AskTeacherAdapter.Holder item = new AskTeacherAdapter.Holder();



        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.ask_teacher_category, parent, false);
        }


        MessageThreadsAdapter MessagesAdapter =  new MessageThreadsAdapter(context, R.layout.ask_teacher_category_list_item,MessageCategory);
        item.categoryData = (ListView) view.findViewById(R.id.ask_teacher_category_list_view);
        item.categoryData.setAdapter(MessagesAdapter);
        UIUtils.setListViewHeightBasedOnItems(item.categoryData);




        return view;
    }

    public ArrayList<MessageThread> reverse(ArrayList<MessageThread> list) {
        for(int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }
        return list;
    }
}
