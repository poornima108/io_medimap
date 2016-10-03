package com.example.makrandpawar.medimap;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class firebasetest extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
