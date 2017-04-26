package login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.bedopedia.bedopedia_android.R;

import Tools.FragmentUtils;


public class schoolCode extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.school_code);
        FragmentUtils.createFragment(getFragmentManager(), SchoolCodeFragment.newInstance(), R.id.school_code_container);


    }



}
