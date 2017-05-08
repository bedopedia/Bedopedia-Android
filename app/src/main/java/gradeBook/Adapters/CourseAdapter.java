package gradeBook.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by ali on 13/02/17.
 */

public class CourseAdapter extends BaseExpandableListAdapter {


        private Context context;
        private ArrayList<ArrayList<String>> header;
        ArrayList<ArrayList<ArrayList<String>>> items;




        public CourseAdapter(Context context, int resource, ArrayList<ArrayList<ArrayList<String>>> items, ArrayList<ArrayList<String>> header) {
            this.context =   context;
            this.header = header;
            this.items = items;
        }

    @Override
    public int getGroupCount() {
        return header.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return items.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

       view = LayoutInflater.from(context).inflate(R.layout.course_list_item, parent, false);


        CourseAdapter.GroupHolder parentCourseItem=new CourseAdapter.GroupHolder();

        parentCourseItem.CategoryName = (TextView) view.findViewById(R.id.course_list_category_name);
        Typeface roboto = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Medium.ttf"); //use this.getAssets if you are calling from an Activity
        Typeface roboto2 = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Bold.ttf"); //use this.getAssets if you are calling from an Activity
        parentCourseItem.CategoryName.setTypeface(roboto);
        parentCourseItem.numOfCategory = (TextView) view.findViewById(R.id.categories_count);
        parentCourseItem.numOfCategory.setTypeface(roboto2);
        parentCourseItem.maxGrade = (TextView) view.findViewById(R.id.max_grade);
        parentCourseItem.maxGrade.setTypeface(roboto2);
        parentCourseItem.CategoryName.setText(header.get(groupPosition).get(0));
        parentCourseItem.numOfCategory.setText(header.get(groupPosition).get(1));
        parentCourseItem.maxGrade.setText(header.get(groupPosition).get(2));
        return view ;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

     view = LayoutInflater.from(context).inflate(R.layout.course_list_view_item, parent, false);
     CourseAdapter.ChildHolder childGradeItem=new CourseAdapter.ChildHolder();

        childGradeItem.mCourseNameTextView = (TextView) view.findViewById(R.id.single_course_name);
        childGradeItem.mCourseGradeTextView =  (TextView) view.findViewById(R.id.single_course_grade);
        childGradeItem.mCourseCommentTextView = (TextView) view.findViewById(R.id.single_course_grade_comment);
        childGradeItem.mCourseAverageTextView = (TextView) view.findViewById(R.id.single_course_average);

        Typeface roboto = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf"); //use this.getAssets if you are calling from an Activity
        childGradeItem.mCourseNameTextView.setTypeface(roboto);
        childGradeItem.mCourseGradeTextView.setTypeface(roboto);
        childGradeItem.mCourseCommentTextView.setTypeface(roboto);
        childGradeItem.mCourseAverageTextView.setTypeface(roboto);

        childGradeItem.mCourseNameTextView.setText(items.get(groupPosition).get(childPosition).get(0));
        childGradeItem.mCourseGradeTextView.setText(items.get(groupPosition).get(childPosition).get(1));
        childGradeItem.mCourseCommentTextView.setText(items.get(groupPosition).get(childPosition).get(2));
        Double avg = Double.valueOf(items.get(groupPosition).get(childPosition).get(3));
        avg = BigDecimal.valueOf(avg)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
        childGradeItem.mCourseAverageTextView.setText("Avg. grade is " +avg.toString());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public static  class GroupHolder{
            TextView CategoryName;
            TextView numOfCategory;
            TextView maxGrade;
        }

    public static class ChildHolder{
            TextView mCourseNameTextView ;
            TextView mCourseAverageTextView ;
            TextView mCourseGradeTextView ;
            TextView mCourseCommentTextView ;
        }
}