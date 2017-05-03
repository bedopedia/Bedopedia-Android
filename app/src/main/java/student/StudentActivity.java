package student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.example.bedopedia.bedopedia_android.R;
import Tools.FragmentUtils;


/**
 * Created by khaled on 2/22/17.
 */

public class StudentActivity extends AppCompatActivity {


    ActionBar ab ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_home);


        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        

        FragmentUtils.createFragment(getSupportFragmentManager(), StudentFragment.newInstance(), R.id.student_home_container);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()  == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
