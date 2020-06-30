package com.example.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

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
                    cancelTimer();
                    Log.d("tag", "Cancel Pressed");
                }else{
                    startTimer();
                }
            }
        });
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
        myTimeLeftInMillis = myStartTimeInMillis;
        myEndTime = System.currentTimeMillis() + myTimeLeftInMillis;
        myCOuntDownTimwe = new CountDownTimer(myTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                myTimeLeftInMillis = millisUntilFinished;
                updateCounterView();
                Log.d("tag", "On tick");
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
}


