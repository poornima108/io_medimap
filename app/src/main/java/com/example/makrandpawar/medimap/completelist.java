package com.example.makrandpawar.medimap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class completelist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completelist);

        Intent intent = getIntent();
        String med = intent.getStringExtra(MainActivity.medicinename);
    }
}
