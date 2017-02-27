package Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.GradesAvtivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import Models.Course;
import Models.CourseGroup;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAdapter extends ArrayAdapter<CourseGroup> {
    public GradesAvtivity context;

    public GradesAdapter(Context context, int resource, List<CourseGroup> items) {
        super(context, resource, items);
        this.context = (GradesAvtivity) context;
    }

    public static  class Holder{
        ImageView courseImage;
        TextView courseName;
        TextView courseGrade;
        ImageButton openCourse;
        LinearLayout singleGrade;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        CourseGroup courseGroup = (CourseGroup) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_grade, parent, false);
        }
        item=new Holder();

        item.courseImage = (ImageView) view.findViewById(R.id.course_image);
        item.courseName = (TextView) view.findViewById(R.id.course_name);
        item.courseGrade = (TextView) view.findViewById(R.id.course_grade);
        item.openCourse = (ImageButton) view.findViewById(R.id.open_grade);
        item.singleGrade = (LinearLayout) view.findViewById(R.id.single_grade);

        item.courseName.setText(courseGroup.getCourseName());
        item.courseGrade.setText(courseGroup.getGrade());

        item.singleGrade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                context.itemClicked(position);
            }
        });



        String imageName = courseGroup.getIcon();
        int res = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        item.courseImage.setImageResource(res);

        if(courseGroup.getGrade().charAt(0) == 'A'){
            item.courseGrade.setBackgroundResource(R.drawable.grade_a_circle);
        } else if(courseGroup.getGrade().charAt(0) == 'B'){
            item.courseGrade.setBackgroundResource(R.drawable.grade_b_circle);
        } else if(courseGroup.getGrade().charAt(0) == 'F'){
            item.courseGrade.setBackgroundResource(R.drawable.grade_f_circle);
        } else {
            item.courseGrade.setBackgroundResource(R.drawable.grade_c_circle);
        }
        return view;
    }
}

