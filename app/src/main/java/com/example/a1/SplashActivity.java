package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    public static final String CHANNEL_1_ID = "channel1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        createNotificationChannels();
//        this.run();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userId = FirebaseAuth.getInstance().getUid();

            if(userId == null){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
                finish();
            }
        }, 3500);

    }

    private void createNotificationChannels() {
         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             NotificationChannel channel1 = new NotificationChannel(
                     CHANNEL_1_ID,
                    "channel 1",
                     NotificationManager.IMPORTANCE_HIGH
             );
             channel1.enableVibration(true);
             channel1.enableLights(true);
             channel1.setDescription("this is channel1");
             NotificationManager myNotificationManager = getSystemService(NotificationManager.class);
             myNotificationManager.createNotificationChannel(channel1);
         }
    }

//    @Override
//    public void run() {
//        try {
//            Thread.sleep(3500);
//            String userId = FirebaseAuth.getInstance().getUid();
//            if(userId == null){
//                startActivity(new Intent(this, LoginActivity.class));
//                finish();
//            }else {
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
}