package com.example.makrandpawar.medimap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button findmedicine;
    EditText searchmedicine;
    DatabaseReference databaseReference;

    static public String medicine = "medicinename";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchmedicine = (EditText) findViewById(R.id.searchmedicine);
        findmedicine = (Button) findViewById(R.id.findmedicine);
        databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://medimap-fbbb5.firebaseio.com/");



        findmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String med = searchmedicine.getText().toString();
                med=med.toLowerCase();
                final String finalMed = med;
                if(!med.equals("") && med.trim().length()>0){
                    final String finalMed1 = med;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(finalMed).exists()){
                                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                                intent.putExtra(medicine, finalMed1);
                                startActivity(intent);
                            }
                            else {
                                searchmedicine.getText().clear();
                                Toast.makeText(MainActivity.this,"MEDICINE NOT FOUND.TRY AGAIN",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    searchmedicine.getText().clear();
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
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contactus:
                Intent i=new Intent(MainActivity.this,contact.class);
                startActivity(i);
                return true;
            case R.id.action_feedback:
                Intent j=new Intent(MainActivity.this,feedback.class);
                startActivity(j);
                return true;
        }
        return true;
    }

}
