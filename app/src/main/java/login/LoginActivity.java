package login;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;

import com.skolera.skolera_android.R;
import Tools.FragmentUtils;


public class LoginActivity extends AppCompatActivity {

    private ActionBar loginActivityActionbar ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        Toolbar tb = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(tb);
        loginActivityActionbar = getSupportActionBar();
        loginActivityActionbar.setDisplayHomeAsUpEnabled(true);

        FragmentUtils.createFragment(getSupportFragmentManager(), LogInFragment.newInstance(), R.id.log_in_container);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }
}
