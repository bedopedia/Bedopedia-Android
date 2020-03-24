package login;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skolera.skolera_android.R;

import Tools.FragmentUtils;


public class schoolCode extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.school_code);
        FragmentUtils.createFragment(getSupportFragmentManager(), SchoolCodeFragment.newInstance(), R.id.school_code_container);


    }



}
