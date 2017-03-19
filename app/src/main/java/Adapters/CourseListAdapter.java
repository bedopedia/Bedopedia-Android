package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by ali on 16/02/17.
 */

public class CourseListAdapter extends ArrayAdapter

    {


        private Context context;

        public CourseListAdapter(Context context, int resource, ArrayList<ArrayList<String>> items) {
        super(context, resource, items);
        this.context =   context;
    }

        public static  class Holder{
            TextView name;
            TextView grade;
            TextView comment;
            TextView average;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ArrayList<String> courseItem = ( ArrayList<String>) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        CourseListAdapter.Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_view_item, parent, false);
        }
        item=new CourseListAdapter.Holder();

        //TODO notification logo
        item.name = (TextView) view.findViewById(R.id.courseItemtName);
        item.grade =  (TextView) view.findViewById(R.id.courseItemtGrade);
        item.comment = (TextView) view.findViewById(R.id.courseItemtgradeComment);
        item.average = (TextView) view.findViewById(R.id.courseItemtAvg);

        Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        item.name.setTypeface(roboto);
        item.grade.setTypeface(roboto);
        item.comment.setTypeface(roboto);
        item.average.setTypeface(roboto);

        item.name.setText(courseItem.get(0));
        item.grade.setText(courseItem.get(1));
        item.comment.setText(courseItem.get(2));
        Double avg = Double.valueOf(courseItem.get(3));
        avg = BigDecimal.valueOf(avg)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
        item.average.setText("Avg. grade is " +avg.toString());



        return view;
    }
    }
