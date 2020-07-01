package com.example.a1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView myTextViewCountDown;
    private Button myButtonStartCancel;
    static int seekBarValue;
    SeekBar seekBar;
    private CountDownTimer myCOuntDownTimwe;
    private boolean myTimerRunning;
    private long myStartTimeInMillis = 0;
    private long myTimeLeftInMillis;
    private long myEndTime;
    private boolean isLoggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myButtonStartCancel = findViewById(R.id.myButtonStartCancel);
        myTextViewCountDown = findViewById(R.id.myTextViewCountDown);
        seekBarFunc();

        myButtonStartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myTimerRunning){
                    Log.d("tag", "Verify()");
                    verifyCancel();
//                    cancelTimer();
                    Log.d("tag", "Out of Verify()");
                }else{
                    startTimer();
                }
            }
        });

    }


    public void verifyCancel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Tree will die if you quit.");
        builder.setMessage("Are you sure to quit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Log.d("tag", "Yes. Cancel Timer. Kill tree");
               cancelTimer();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            Log.d("tag", "Don't kill tree");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        if(myTimerRunning) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your Tree will die if you quit");
            builder.setMessage("Do you really want to quit ?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancelTimer();
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            super.onBackPressed();
        }

    }


    public void seekBarFunc(){
        seekBar = findViewById(R.id.mySeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d("tag", "SeekBar Start");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("tag", "SeekBar Stop at "+ seekBarValue);
                updatePercentage(seekBarValue);
//                TextView textViewCheck = (TextView)findViewById(R.id.textViewCheck);
//                textViewCheck.setText(String.valueOf(seekBarValue));
                setTime(seekBarValue);
            }
        });
    }


    public void setTime(int value){
       myStartTimeInMillis = value*60*1000;
       Log.d("tag", "setTime(), myStartTimeInMills = "+myStartTimeInMillis);
       resetTimer();
    }


    public void updatePercentage(int myProgress){
        TextView textView = findViewById(R.id.myTextViewPercentage);
        String str;
        String num = String.valueOf(myProgress);
//        Log.d("tag", num);
        str = num + "  / " + seekBar.getMax();
        textView.setText(str);
    }


    private void startTimer(){
        Log.d("tag", "startTimer(), myTimeLeftInMillis = "+  myTimeLeftInMillis);
        seekBar.setVisibility(View.INVISIBLE);
        myTimeLeftInMillis = myStartTimeInMillis;
        myEndTime = System.currentTimeMillis() + myTimeLeftInMillis;
        myCOuntDownTimwe = new CountDownTimer(myTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                myTimeLeftInMillis = millisUntilFinished;
                updateCounterView();
                Log.d("tag", "On tick" + myTimeLeftInMillis);
            }

            @Override
            public void onFinish() {
                myTimerRunning = false;
//              Tree Completed
                Log.d("tag", "Tree Planted");
                updateWatchInterface();
            }
        }.start();
        myTimerRunning = true;
        updateWatchInterface();
    }


    private void cancelTimer(){
        Log.d("tag", "Do you really want to cancel ?");
        seekBar.setVisibility(View.VISIBLE);
        myTimeLeftInMillis = 0;
        myStartTimeInMillis = 0;
        myTimerRunning = false;
        updateCounterView();
        updateWatchInterface();
        myCOuntDownTimwe.cancel();
    }


    private void resetTimer() {
        myTimeLeftInMillis = myStartTimeInMillis;
        updateCounterView();
        updateWatchInterface();
    }


    private void updateCounterView(){
        int hours = (int) (myTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((myTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (myTimeLeftInMillis / 1000) % 60;
        String timeLeftFromatted;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        myTextViewCountDown.setText(timeLeftFormatted);
    }


    private void updateWatchInterface(){
        if(myTimerRunning){
            myButtonStartCancel.setText("cancel");
        }else{
            myButtonStartCancel.setText("start");
        }
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putLong("startTimeMillis", myStartTimeInMillis);
//        editor.putLong("millisLeft", myTimeLeftInMillis);
//        editor.putBoolean("timerRunning", myTimerRunning);
//        editor.putLong("endTime", myEndTime);
//        editor.apply();
//        if(myCOuntDownTimwe != null){
//            myCOuntDownTimwe.cancel();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
//        myStartTimeInMillis = sharedPreferences.getLong("startTimeMillis", 1800000);
//        myTimeLeftInMillis = sharedPreferences.getLong("millisLeft", myStartTimeInMillis);
//        myTimerRunning = sharedPreferences.getBoolean("timerRunning", false);
//        updateCounterView();
//        if(myTimerRunning){
//            myEndTime = sharedPreferences.getLong("endTime", 0);
//            myTimeLeftInMillis = myEndTime - System.currentTimeMillis();
//            if(myTimeLeftInMillis < 0){
//                myTimeLeftInMillis = 0;
//                myTimerRunning = false;
//                updateCounterView();
//                updateWatchInterface();
//            }else{
//                startTimer();
//            }
//        }
//    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag", "onPause()");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("tag", "onStop()");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag", "onResume()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "onDestroy()");
    }


    public void register(View view){
        Log.d("tag", "register()");
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        isLoggedIn = true;
    }

    public void logIn(View view){
        Log.d("tag", "in logIn()");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        isLoggedIn = true;
    }

    public void logOut(View view){
        Log.d("tag", "in logOut()");
        FirebaseAuth.getInstance().signOut();
        Log.d("tag", "SignedOut");
        isLoggedIn = false;
    }

    public void fun(View view) {
        Log.d("tag", "in fun()");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Root2").push().child("Root3");
//        myref.setValue("2");

        HashMap<String, Object> myMap = new HashMap<>();
        myMap.put("Name", "c");
        myMap.put("StartTime", "12:00");
        myMap.put("ExpectedEndTime", "14:00");
        myMap.put("DidComplete", true);
        myMap.put("EndTime", "14:00");

        myref.updateChildren(myMap);
    }

    public void goForList(View view) {
        Log.d("tag", "Go for list");
        startActivity(new Intent(MainActivity.this, ListActivity.class));
        Log.d("tag", "Back from list Activity");
    }
}


//TODO Data Storing, menubar, ui design, whitelist, JobSheduler


