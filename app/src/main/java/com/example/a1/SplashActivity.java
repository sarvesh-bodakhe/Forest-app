package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        this.run();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userId = FirebaseAuth.getInstance().getUid();
            if(userId == null){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
            }
        }, 3500);

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