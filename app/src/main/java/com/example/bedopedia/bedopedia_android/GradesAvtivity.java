package com.example.bedopedia.bedopedia_android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import Adapters.GradesAdapter;
import Models.Course;
import Models.Grade;
import Models.Student;

/**
 * Created by mohamedkhaled on 2/13/17.
 */

public class GradesAvtivity extends AppCompatActivity {
    List<Grade> grades;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grades);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO go to another activity
            }
        });
        //setTitle("Grades");
        grades = new ArrayList<Grade>();
        grades.add(new Grade((float) 96.0, new Course("Math" , "discription" , 50) , new Student()));
        grades.add(new Grade((float) 94.0, new Course("English" , "discription" , 50) , new Student()));
        grades.add(new Grade((float) 92.0, new Course("Science" , "discription" , 50) , new Student()));

        GradesAdapter adapter = new GradesAdapter(this, R.layout.single_grade, grades);
        GridView gradesList = (GridView) findViewById(R.id.grades_list);
        gradesList.setAdapter(adapter);
    }

}
