package Adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Models.Course;

/**
 * Created by ali on 13/02/17.
 */

public class CourseAdapter extends ArrayAdapter  {


        private Context context;

        public CourseAdapter(Context context, int resource, ArrayList<Pair<String,String>> items) {
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
        Pair<String,String> assignment = (Pair<String,String>) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

            CourseAdapter.Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
        }
        item=new CourseAdapter.Holder();

        //TODO notification logo
        item.name = (TextView) view.findViewById(R.id.assignmentName);
        item.grade =  (TextView) view.findViewById(R.id.assignmentGrade);

        item.name.setText(assignment.first);
        item.grade.setText(assignment.second);


        return view;
    }
}
