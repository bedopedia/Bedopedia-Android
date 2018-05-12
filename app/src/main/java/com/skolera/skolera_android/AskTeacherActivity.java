package com.skolera.skolera_android;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import Tools.FragmentUtils;

import static com.skolera.skolera_android.R.id.ask_teacher_toolbar_id;


public class AskTeacherActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);

        Toolbar askTeacherToolbar = (Toolbar) findViewById(ask_teacher_toolbar_id);
        setSupportActionBar(askTeacherToolbar);
        ActionBar askTeacherActionbar = getSupportActionBar();
        askTeacherActionbar.setDisplayHomeAsUpEnabled(true);
        askTeacherActionbar.setTitle("Contact Teacher");
        Bundle extras= this.getIntent().getExtras();
        final String studentId = extras.getString("student_id");
        ImageView plusButton = (ImageView) findViewById(R.id.ask_teacher_new);

        if (studentId.equals("none")) {
            plusButton.setVisibility(View.INVISIBLE);
        }

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskTeacherActivity.this, com.skolera.skolera_android.ActivityNewMessage.class);
                intent.putExtra("student_id",studentId);
                startActivity(intent);
            }
        });
        FragmentUtils.createFragment(getSupportFragmentManager(), com.skolera.skolera_android.AskTeacherFragment.newInstance(this), R.id.askTeacherContainer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
