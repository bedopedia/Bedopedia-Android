package com.example.bedopedia.bedopedia_android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import Tools.FragmentUtils;



public class AskTeacherActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);

        Toolbar askTeacherToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(askTeacherToolbar);
        ActionBar askTeacherActionbar = getSupportActionBar();
        askTeacherActionbar.setDisplayHomeAsUpEnabled(true);
        askTeacherActionbar.setTitle(R.string.AskTeacherTitle);
        FragmentUtils.createFragment(getSupportFragmentManager(), AskTeacherFragment.newInstance(this), R.id.askTeacherContainer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
