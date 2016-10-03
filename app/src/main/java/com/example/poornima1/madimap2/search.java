package com.example.poornima1.madimap2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class search extends AppCompatActivity {
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
        protected void onPause(){

        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_contactus:
                Intent i=new Intent(search.this,contact.class);
                startActivity(i);
                return true;
            case R.id.action_feedback:
                Intent j=new Intent(search.this,feedback.class);
                startActivity(j);
                return true;
            case R.id.action_share:
                 Intent k=new Intent("shareactivity");
                 startActivity(k);
        }
return true;
    }

}
