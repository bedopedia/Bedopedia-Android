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
    final String studentIdKey = "student_id";
    final String courseGroupIdKey = "course_group_id";
    final String courseIdKey = "course_id";
    final String courseNameKey = "course_name";


    public GradesAdapter(Context context, int resource, List<CourseGroup> items , String studentId) {
        super(context, resource, items);
        this.context = (GradesAvtivity) context;
        this.studentId = studentId ;
        this.items = items ;
    }

    public static  class GradesHolder{
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
        final GradesHolder gradeHolderItem;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_grade, parent, false);
        }
        gradeHolderItem =new GradesHolder();
        Typeface robotoMedium = Typeface.createFromAsset(GradesAvtivity.context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoBold = Typeface.createFromAsset(GradesAvtivity.context.getAssets(), "font/Roboto-Bold.ttf");

        gradeHolderItem.courseImage = (ImageView) view.findViewById(R.id.course_image);
        gradeHolderItem.courseName = (TextView) view.findViewById(R.id.course_name);
        gradeHolderItem.courseGrade = (TextView) view.findViewById(R.id.course_grade);
        gradeHolderItem.openCourse = (ImageButton) view.findViewById(R.id.open_grade);
        gradeHolderItem.singleGrade = (LinearLayout) view.findViewById(R.id.single_grade);

        gradeHolderItem.courseName.setTypeface(robotoMedium);
        gradeHolderItem.courseName.setText(courseGroup.getCourseName());
        gradeHolderItem.courseGrade.setTypeface(robotoBold);
        gradeHolderItem.courseGrade.setText(courseGroup.getGrade());

        gradeHolderItem.singleGrade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent activityCourseIntent =  new Intent(context.getApplicationContext(), ActivityCourse.class);
                activityCourseIntent.putExtra(studentIdKey,studentId);
                activityCourseIntent.putExtra(courseGroupIdKey, String.valueOf(items.get(position).getId()));
                activityCourseIntent.putExtra(courseIdKey, String.valueOf(items.get(position).getCourseId()));
                activityCourseIntent.putExtra(courseNameKey, items.get(position).getCourseName());
                context.startActivity(activityCourseIntent);
            }
        });

        if (!courseGroup.getIcon().equals("non")) {
            String imageName = courseGroup.getIcon();
            int res = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            gradeHolderItem.courseImage.setImageResource(res);

            if(courseGroup.getGrade().charAt(0) == 'A'){
                gradeHolderItem.courseGrade.setBackgroundResource(R.drawable.grade_a_circle);
            } else if(courseGroup.getGrade().charAt(0) == 'F'){
                gradeHolderItem.courseGrade.setBackgroundResource(R.drawable.grade_f_circle);
            } else {
                gradeHolderItem.courseGrade.setBackgroundResource(R.drawable.grade_bcd_circle);
            }
        }

        return view;
    }
}

