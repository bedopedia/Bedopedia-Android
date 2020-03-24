package login;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.skolera.skolera_android.R;

import Tools.FragmentUtils;



public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        Toolbar forgetPasswordToolbar = (Toolbar) findViewById(R.id.default_toolbar_id);
        setSupportActionBar(forgetPasswordToolbar);
        ActionBar forgetPasswordActionbar = getSupportActionBar();
        forgetPasswordActionbar.setDisplayHomeAsUpEnabled(true);
        forgetPasswordActionbar.setTitle(R.string.ForgetPasswordTitle);
        FragmentUtils.createFragment(getSupportFragmentManager(), ForgetPasswordFRagment.newInstance(), R.id.forget_password );

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true ;
    }

}
