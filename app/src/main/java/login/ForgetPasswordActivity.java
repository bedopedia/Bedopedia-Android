package login;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.bedopedia.bedopedia_android.R;
import Tools.FragmentUtils;



public class ForgetPasswordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);


        Toolbar tb = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.ForgetPasswordTitle);

        FragmentUtils.createFragment(getSupportFragmentManager(), ForgetPasswordFRagment.newInstance(), R.id.forget_password );

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }

}
