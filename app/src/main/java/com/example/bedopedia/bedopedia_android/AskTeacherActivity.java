package com.example.bedopedia.bedopedia_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.AskTeacherAdapter;
import Adapters.CourseAdapter;
import Models.AskTeacherMessage;

public class AskTeacherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);

        ArrayList<ArrayList<AskTeacherMessage>> items =  new ArrayList<>() ;
        ArrayList<String> header = new ArrayList<>();

        for (int i = 0 ; i < 5 ; i++) {
            ArrayList<AskTeacherMessage> temp = new ArrayList<>();
            temp.add(new AskTeacherMessage());
            temp.add(new AskTeacherMessage());
            temp.add(new AskTeacherMessage());
            temp.add(new AskTeacherMessage());
            items.add(temp);
            header.add("Math");
        }

        AskTeacherAdapter askTeacherAdapter = new AskTeacherAdapter(AskTeacherActivity.this, R.layout.activity_ask_teacher,items, header);

        ListView askTeacherListView = (ListView) findViewById(R.id.category_list_view);
        askTeacherListView.setAdapter(askTeacherAdapter);




    }
}
