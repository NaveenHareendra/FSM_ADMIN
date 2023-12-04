package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashScreen extends AppCompatActivity {

    //Handler will handles a some kind of a process up to a certain delay
    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Get Device ID -  To Enable Firebase Cloud Messaging Service

       FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println("Device 1 TOKEN (admin) = "+token);
                    }
        });

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent=new Intent(SplashScreen.this, WelcomePage.class);
                startActivity(myIntent);
            }
        },4000);
    }
}