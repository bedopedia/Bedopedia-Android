package Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.ArrayList;

import Tools.UIUtils;

/**
 * Created by ali on 13/02/17.
 */

public class CourseAdapter extends ArrayAdapter  {


        private Context context;
        private ArrayList<ArrayList<String>> header;
        ArrayList<ArrayList<ArrayList<String>>> items;

        public CourseAdapter(Context context, int resource, ArrayList<ArrayList<ArrayList<String>>> items, ArrayList<ArrayList<String>> header) {
        super(context, resource, items);
            this.context =   context;
            this.header = header;
            this.items = items;
        }

        public static  class Holder{
            TextView CategoryName;
            TextView numOfCategory;
            TextView maxGrade;
            ListView categoryData;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        ArrayList<ArrayList<String>> courseItem = (ArrayList<ArrayList<String>>) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view




            CourseAdapter.Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
        }
        item=new CourseAdapter.Holder();

         item.CategoryName = (TextView) view.findViewById(R.id.CategoryName);
         Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Medium.ttf"); //use this.getAssets if you are calling from an Activity
         Typeface roboto2 = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Bold.ttf"); //use this.getAssets if you are calling from an Activity
         item.CategoryName.setTypeface(roboto);
         item.numOfCategory = (TextView) view.findViewById(R.id.numOfCategory);
         item.numOfCategory.setTypeface(roboto2);
         item.maxGrade = (TextView) view.findViewById(R.id.maxGrade);
         item.maxGrade.setTypeface(roboto2);
         item.CategoryName.setText(header.get(position).get(0));
         item.numOfCategory.setText(header.get(position).get(1));
         item.maxGrade.setText(header.get(position).get(2));

         item.categoryData = (ListView) view.findViewById(R.id.course_item_list_view);
         CourseListAdapter listViewAdapter = new CourseListAdapter(context,R.layout.course_list_view_item, courseItem );
         item.categoryData.setAdapter(listViewAdapter);
         UIUtils.setListViewHeightBasedOnItems(item.categoryData);


        //TODO notification logo
        return view;
    }
}
