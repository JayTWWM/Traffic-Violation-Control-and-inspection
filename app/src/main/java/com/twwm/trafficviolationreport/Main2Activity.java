package com.twwm.trafficviolationreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }

    public void report(View view)
    {
        startActivity(new Intent(Main2Activity.this, InfoActivity.class));
    }

    public void search(View view)
    {
        startActivity(new Intent(Main2Activity.this, Search.class));
    }

    public void searchApprove(View view)
    {
        startActivity(new Intent(Main2Activity.this, ApprovedSearch.class));
    }

}
