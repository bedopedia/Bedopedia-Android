package student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


import com.example.bedopedia.bedopedia_android.R;
import Tools.FragmentUtils;


/**
 * Created by khaled on 2/22/17.
 */

public class StudentActivity extends AppCompatActivity {




    ActionBar studentACtivityActionBar ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);


        Toolbar studentACtivityToolBar = (Toolbar) findViewById(R.id.custom_toolbar_id);
        studentACtivityToolBar.setTitle("");
        setSupportActionBar(studentACtivityToolBar);
        ImageButton back = (ImageButton) findViewById(R.id.back_button_student);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        FragmentUtils.createFragment(getSupportFragmentManager(), StudentFragment.newInstance(), R.id.student_home_container);
    }



}
