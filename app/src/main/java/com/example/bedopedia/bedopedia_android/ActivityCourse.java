package com.example.bedopedia.bedopedia_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Adapters.CourseAdapter;

public class ActivityCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        ArrayList<Pair<String,String>> assignmentsTempData = new ArrayList<Pair<String,String>>();
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));
        assignmentsTempData.add(new Pair<String, String>("Assignments #1","20/100"));

        ArrayList<Pair<String,String>> classWorksTempData = new ArrayList<Pair<String,String>>();
        classWorksTempData.add(new Pair<String, String>("classWork #1","20/100"));
        classWorksTempData.add(new Pair<String, String>("classWork #1","20/100"));
        classWorksTempData.add(new Pair<String, String>("classWork #1","20/100"));
        classWorksTempData.add(new Pair<String, String>("classWork #1","20/100"));
        classWorksTempData.add(new Pair<String, String>("classWork #1","20/100"));
        classWorksTempData.add(new Pair<String, String>("classWork #1","20/100"));

        CourseAdapter assignmentAdapter = new CourseAdapter(this, R.layout.activity_course, assignmentsTempData   );
        CourseAdapter classWorkAdapter = new CourseAdapter(this, R.layout.activity_course, classWorksTempData );

        ListView assignmentsListView = (ListView) findViewById(R.id.assignments_list_view);
        ListView classWorksListView = (ListView) findViewById(R.id.classworks_list_view);

        assignmentsListView.setAdapter(assignmentAdapter);
        classWorksListView.setAdapter(classWorkAdapter);

    }
}
