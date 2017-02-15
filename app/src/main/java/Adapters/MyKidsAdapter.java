package Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedopedia.bedopedia_android.MyKidsActivity;
import com.example.bedopedia.bedopedia_android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Models.Student;
import Services.ApiClient;

/**
 * Created by mohamed on 2/9/17.
 */

public class MyKidsAdapter extends ArrayAdapter<Student> {

    public MyKidsActivity context;

    public MyKidsAdapter(Context context, int resource, List<Student> items) {
        super(context, resource, items);
        this.context = (MyKidsActivity) context;
    }

    public static  class Holder{
        ImageView avatar;
        TextView name;
        ImageButton openStudent;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Get the data item for this position
        Student student = (Student) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder item;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.single_student, parent, false);
        }
        item=new Holder();

        item.avatar = (ImageView) view.findViewById(R.id.student_avatar);
        item.name = (TextView) view.findViewById(R.id.student_name);
        item.openStudent = (ImageButton) view.findViewById(R.id.open_student);
        Log.d("student", student.toString());
        item.name.setText(student.getFirstName() + " " + student.getLastName());
        Picasso.with(context).load(ApiClient.BASE_URL+student.getAvatar()).into(item.avatar);
        item.openStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                context.itemClicked(position);
            }
        });

        return view;
    }

}
