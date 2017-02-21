package Adapters;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.ArrayList;

/**
 * Created by ali on 16/02/17.
 */

public class CourseListAdapter extends ArrayAdapter

    {


        private Context context;

        public CourseListAdapter(Context context, int resource, ArrayList<Pair<String,String>> items) {
        super(context, resource, items);
        this.context =   context;
    }

        public static  class Holder{
            TextView name;
            TextView grade;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Pair<String,String> courseItem = (Pair<String,String>) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        CourseListAdapter.Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_view_item, parent, false);
        }
        item=new CourseListAdapter.Holder();

        //TODO notification logo
        item.name = (TextView) view.findViewById(R.id.courseItemtName);
        item.grade =  (TextView) view.findViewById(R.id.courseItemtGrade);

        item.name.setText(courseItem.first);
        item.grade.setText(courseItem.second);


        return view;
    }
    }
