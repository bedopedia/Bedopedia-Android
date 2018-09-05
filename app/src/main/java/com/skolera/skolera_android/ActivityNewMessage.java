package com.skolera.skolera_android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import Tools.FragmentUtils;

public class ActivityNewMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        Toolbar askTeacherToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(askTeacherToolbar);
        ActionBar askTeacherActionbar = getSupportActionBar();
        askTeacherActionbar.setDisplayHomeAsUpEnabled(true);
        askTeacherActionbar.setTitle("New Message");

        FragmentUtils.createFragment(getSupportFragmentManager(), com.skolera.skolera_android.NewMessageFragment.newInstance(), R.id.newMessageContainer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}

