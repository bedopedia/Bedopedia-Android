package Adapters;

import android.content.Context;
import android.graphics.Typeface;
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

import Models.Badge;
import Models.CourseGroup;

/**
 * Created by khaled on 3/14/17.
 */

public class PadgesAdapter extends ArrayAdapter<Badge> {

    public Context context;

    public PadgesAdapter(Context context, int resource, List<Badge> items) {
        super(context, resource, items);
        this.context = context;
    }

    public static  class Holder{
        ImageView badgeIcon;
        TextView badgeName;
        TextView badgeReason;
        TextView courseName;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Badge badge = (Badge) getItem(position);
        Holder item;

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_grade, parent, false);
        }
        item=new Holder();
        Typeface robotoMedium = Typeface.createFromAsset(GradesAvtivity.context.getAssets(), "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(GradesAvtivity.context.getAssets(), "font/Roboto-Regular.ttf");

        item.badgeIcon = (ImageView) view.findViewById(R.id.badge_icon);
        item.badgeName = (TextView) view.findViewById(R.id.badge_name);
        item.badgeReason = (TextView) view.findViewById(R.id.badge_reason);
        item.courseName = (TextView) view.findViewById(R.id.badge_course_name);

        item.badgeName.setTypeface(robotoMedium);
        item.badgeReason.setTypeface(robotoRegular);
        item.courseName.setTypeface(robotoRegular);

//        item.badgeName.setText(badge.get);
//        item.courseGrade.setTypeface(robotoBold);
//        item.courseGrade.setText(courseGroup.getGrade());
//
//        item.singleGrade.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                context.itemClicked(position);
//            }
//        });
//
//
//
//        String imageName = courseGroup.getIcon();
//        int res = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
//        item.courseImage.setImageResource(res);
//
//        if(courseGroup.getGrade().charAt(0) == 'A'){
//            item.courseGrade.setBackgroundResource(R.drawable.grade_a_circle);
//        } else if(courseGroup.getGrade().charAt(0) == 'B'){
//            item.courseGrade.setBackgroundResource(R.drawable.grade_b_circle);
//        } else if(courseGroup.getGrade().charAt(0) == 'F'){
//            item.courseGrade.setBackgroundResource(R.drawable.grade_f_circle);
//        } else {
//            item.courseGrade.setBackgroundResource(R.drawable.grade_c_circle);
//        }
        return view;
    }
}


