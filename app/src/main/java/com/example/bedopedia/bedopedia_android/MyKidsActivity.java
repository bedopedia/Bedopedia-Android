package com.example.bedopedia.bedopedia_android;

/**
 * Created by mohamed on 2/9/17.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapters.MyKidsAdapter;
import Models.Student;

public class MyKidsActivity extends AppCompatActivity{

    List<Student> myKids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_kids);
        setTitle("My kids");
        myKids = new ArrayList<Student>();
        myKids.add(new Student("Heba", "Ashraf", "female", "h@gmail.com", "avatar", "student", "level-0", "Section A", "Stage-1", 25, null, null));
        myKids.add(new Student("Abeer", "El-sayed", "female", "a@gmail.com", "avatar", "student", "level-1", "Section B", "Stage-2", 25, null, null));
        myKids.add(new Student("Khadeja", "Hussein", "female", "k@gmail.com", "avatar", "student", "level-2", "Section C", "Stage-3", 25, null, null));

        MyKidsAdapter adapter = new MyKidsAdapter(this, R.layout.single_student, myKids);
        GridView studentsList = (GridView) findViewById(R.id.students_list);
        studentsList.setAdapter(adapter);
    }

    public void itemClicked (int index){
        Toast.makeText(getApplicationContext(),"you clicked " + myKids.get(index).getFirstName(),
                Toast.LENGTH_LONG).show();
    }
}
