package com.twwm.trafficviolationreport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Login(View view)
    {
        startActivity(new Intent(MainActivity.this, LogedIn.class));
    }

    public void SignUp(View view)
    {
        startActivity(new Intent(MainActivity.this, SignUp.class));
    }

    public void adminLogin(View view)
    {
        startActivity(new Intent(MainActivity.this, AdminLogin.class));
    }

    public void searched(View view)
    {
        startActivity(new Intent(MainActivity.this, SearchOpen.class));
    }

}
