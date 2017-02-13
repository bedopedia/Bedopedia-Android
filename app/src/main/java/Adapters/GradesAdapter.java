package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.GradesAvtivity;
import com.example.bedopedia.bedopedia_android.R;

import java.util.List;

import Models.Grade;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAdapter extends ArrayAdapter<Grade> {
    public GradesAvtivity context;

    public GradesAdapter(Context context, int resource, List<Grade> items) {
        super(context, resource, items);
        this.context = (GradesAvtivity) context;
    }

    public static  class Holder{
        ImageView courseImage;
        TextView courseName;
        ImageView grade;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Grade grade = (Grade) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_grade, parent, false);
        }
        item=new Holder();

        item.courseImage = (ImageView) view.findViewById(R.id.course_image);
        item.courseName = (TextView) view.findViewById(R.id.course_name);
        item.grade = (ImageView) view.findViewById(R.id.grade);

        item.courseName.setText(grade.getCourse().getName());

        return view;
    }
}

