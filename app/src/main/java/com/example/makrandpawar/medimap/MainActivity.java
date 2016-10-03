package com.example.makrandpawar.medimap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button findmedicine;
    EditText searchmedicine;
    static public String medicinename = "medicinename";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchmedicine = (EditText) findViewById(R.id.searchmedicine);
        findmedicine = (Button) findViewById(R.id.findmedicine);

        findmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String med = searchmedicine.getText().toString();
                Intent intent = new Intent(MainActivity.this,completelist.class);
                intent.putExtra(medicinename,med);
                startActivityForResult(intent,1);
            }
        });

    }

}
