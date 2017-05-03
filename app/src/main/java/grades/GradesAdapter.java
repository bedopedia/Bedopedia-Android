package grades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import gradeBook.ActivityCourse;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAdapter extends ArrayAdapter<CourseGroup> {

    public GradesAvtivity context;
    public String studentId ;
    List<CourseGroup> items ;
    String studentIdKey = "student_id";
    String courseGroupIdKey = "course_group_id";
    String courseIdKey = "course_id";
    String courseNameKey = "course_name";


    public GradesAdapter(Context context, int resource, List<CourseGroup> items , String studentId) {
        super(context, resource, items);
        this.context = (GradesAvtivity) context;
        this.studentId = studentId ;
        this.items = items ;
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
        final Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_grade, parent, false);
        }
        item=new Holder();
        Typeface robotoMedium = Typeface.createFromAsset(GradesAvtivity.context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoBold = Typeface.createFromAsset(GradesAvtivity.context.getAssets(), "font/Roboto-Bold.ttf");

        item.courseImage = (ImageView) view.findViewById(R.id.course_image);
        item.courseName = (TextView) view.findViewById(R.id.course_name);
        item.courseGrade = (TextView) view.findViewById(R.id.course_grade);
        item.openCourse = (ImageButton) view.findViewById(R.id.open_grade);
        item.singleGrade = (LinearLayout) view.findViewById(R.id.single_grade);

        item.courseName.setTypeface(robotoMedium);
        item.courseName.setText(courseGroup.getCourseName());
        item.courseGrade.setTypeface(robotoBold);
        item.courseGrade.setText(courseGroup.getGrade());

        item.singleGrade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i =  new Intent(context.getApplicationContext(), ActivityCourse.class);
                i.putExtra(studentIdKey,studentId);
                i.putExtra(courseGroupIdKey, String.valueOf(items.get(position).getId()));
                i.putExtra(courseIdKey, String.valueOf(items.get(position).getCourseId()));
                i.putExtra(courseNameKey, items.get(position).getCourseName());
                context.startActivity(i);
            }
        });


        String imageName = courseGroup.getIcon();
        int res = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        item.courseImage.setImageResource(res);

        if(courseGroup.getGrade().charAt(0) == 'A'){
            item.courseGrade.setBackgroundResource(R.drawable.grade_a_circle);
        } else if(courseGroup.getGrade().charAt(0) == 'F'){
            item.courseGrade.setBackgroundResource(R.drawable.grade_f_circle);
        } else {
            item.courseGrade.setBackgroundResource(R.drawable.grade_bcd_circle);
        }
        return view;
    }
}

