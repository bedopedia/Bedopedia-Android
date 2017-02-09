package com.example.bedopedia.bedopedia_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import Services.BackEnd;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Toast.makeText(this, BackEnd.getInstance().get("http://192.168.6.184:3000/api/posts", null),
                Toast.LENGTH_LONG).show();

    }
}
