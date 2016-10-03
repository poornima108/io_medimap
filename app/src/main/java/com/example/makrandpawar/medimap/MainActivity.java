package com.example.makrandpawar.medimap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button findmedicine;
    EditText searchmedicine;
    static public String medicinename = "medicinename";
    Context context;
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
                med=med.toLowerCase();
                if(!med.equals("") && med.trim().length()>0){
                    Intent intent = new Intent(MainActivity.this,completelist.class);
                    intent.putExtra(medicinename,med);
                    startActivityForResult(intent,1);
                }
                else {
                    Toast.makeText(MainActivity.this, "PLEASE ENTER MEDICINE NAME", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2) {
            searchmedicine.getText().clear();
            Toast.makeText(MainActivity.this,"MEDICINE NOT FOUND.TRY AGAIN",Toast.LENGTH_LONG).show();
        }
        if (requestCode == 1){
            searchmedicine.getText().clear();
        }
    }

}
