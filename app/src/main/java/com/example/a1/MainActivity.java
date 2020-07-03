package com.example.a1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DialogTitle;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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

    /*Object Variables*/
    private String infoStartTime;
    private String infoExpectedEndTIme;
    private String infoActualEndTime;
    private Boolean isTreeTrue = false;
    ObjectInfo currentObject;

    private String myUserId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myUserId = FirebaseAuth.getInstance().getUid();
        Log.d(TAG, "onCreate: myUserId = " + myUserId);
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
        Log.d(TAG, "startTimer: ");

//        int currhr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int currmin = Calendar.getInstance().get(Calendar.MINUTE);
//        Log.d("tag", "startTimer(), myTimeLeftInMillis = "+  myTimeLeftInMillis);
//        Log.d(TAG, "startTimer: Hour "+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//        Log.d(TAG, "startTimer: Minute "+ Calendar.getInstance().get(Calendar.MINUTE));
//        Log.d(TAG, "startTimer: endtime"+ myEndTime);
//        int hr = (int) (myStartTimeInMillis/1000/60/60);
//        int min = (int) (myStartTimeInMillis/1000/60);
//        int endhr, endmin;
//        currmin = currhr*60 + currmin;
//        int endtotalmin = currmin + (int) myStartTimeInMillis/1000/60 ;
//        endhr = endtotalmin/60;
//        endmin = endtotalmin%60;
//        Log.d(TAG, "startTimer: endhr endmi "+ endhr + " " +endmin);
//        String startTime = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + " : " +
//                (Calendar.getInstance().get(Calendar.MINUTE));
//
//        String endTime =
//        currentObject = new ObjectInfo();
//        currentObject.setFrom(startTime);

        createObjectInfo();

        seekBar.setVisibility(View.INVISIBLE);
        myTimeLeftInMillis = myStartTimeInMillis;
        myEndTime = System.currentTimeMillis() + myTimeLeftInMillis;
        myCOuntDownTimwe = new CountDownTimer(myTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                myTimeLeftInMillis = millisUntilFinished;
                updateCounterView();
//                Log.d("tag", "On tick" + myTimeLeftInMillis);
            }

            @Override
            public void onFinish() {
                myTimerRunning = false;
//              Tree Completed
                Log.d("tag", "Tree Planted");
                String endTimeActual = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))+ ":"+
                        String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
                currentObject.setEnd(endTimeActual);
                currentObject.setDone(true);
                Log.d(TAG, "FinishedTimer: Object Description " + currentObject.getFrom()+currentObject.getTo()
                        + currentObject.getEnd() + currentObject.getDone());
                uploadToDatabase();
                updateWatchInterface();
            }
        }.start();
        myTimerRunning = true;
        updateWatchInterface();
    }

    private void createObjectInfo() {
        Log.d(TAG, "createObjectInfo: ");
       // Calendar currCaledar = Calendar.getInstance();
        int currhr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currmin = Calendar.getInstance().get(Calendar.MINUTE);
        String startTime = String.valueOf(currhr) + ":" + String.valueOf(currmin);
//        Log.d("tag", "startTimer(), myTimeLeftInMillis = "+  myTimeLeftInMillis);
//        Log.d(TAG, "startTimer: Hour "+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
//        Log.d(TAG, "startTimer: Minute "+ Calendar.getInstance().get(Calendar.MINUTE));
        Log.d(TAG, "createObjectInfo: startTime: " +startTime);
//        Log.d(TAG, "startTimer: endtime"+ myEndTime);
        int hr = (int) (myStartTimeInMillis/1000/60/60);
        int min = (int) (myStartTimeInMillis/1000/60);
        int endhr, endmin;
        currmin = currhr*60 + currmin;
        int endtotalmin = currmin + (int) myStartTimeInMillis/1000/60 ;
        endhr = endtotalmin/60;
        endmin = endtotalmin%60;
//        Log.d(TAG, "startTimer: endhr endmi "+ endhr + " " +endmin);
        String endTime = String.valueOf(endhr) + ":" + String.valueOf(endmin);
//        Log.d(TAG, "createObjectInfo: endTIme : " + endTime);

        Date date = new Date();

        currentObject = new ObjectInfo();
        currentObject.setFrom(startTime);
        currentObject.setTo(endTime);
        currentObject.setDate(date);

        Log.d(TAG, "createObjectInfo: currentObject.getFrom() " + currentObject.getFrom());
        Log.d(TAG, "createObjectInfo: currentObject.getTo() " + currentObject.getTo());
    }


    private void cancelTimer(){
        Log.d(TAG, "cancelTimer: ");
        String endTimeActual = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))+ ":"+
                String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        Log.d(TAG, "cancelTimer: endTimeActual : " + endTimeActual);
        Log.d(TAG, "cancelTimer: currentObject.getTo() " + currentObject.getTo());
        currentObject.setEnd(endTimeActual);
        currentObject.setDone(false);
        uploadToDatabase();
        seekBar.setVisibility(View.VISIBLE);
        myTimeLeftInMillis = 0;
        myStartTimeInMillis = 0;
        myTimerRunning = false;
        updateCounterView();
        updateWatchInterface();
        myCOuntDownTimwe.cancel();
        Log.d(TAG, "cancelTimer: Object Description " + currentObject.getFrom()+currentObject.getTo()
                + currentObject.getEnd() + currentObject.getDone());
    }


    public void uploadToDatabase(){
        Log.d(TAG, "uploadToDatabase: MyUserId : " + myUserId);
        ObjectInfo objToUpload = currentObject;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("from",currentObject.getFrom());
        hashMap.put("to", currentObject.getTo());
        hashMap.put("end", currentObject.getEnd());
        hashMap.put("done", currentObject.getDone());
        hashMap.put("date", currentObject.getDate());

        if(myUserId == null){
            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("profiles").child("NoUserId"); //add user
            myref.push().setValue(hashMap);
            Log.d(TAG, "uploadToDatabase setValue with MyuserId = null " + myref);
        }
        else {
            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("profiles/").child(myUserId); //add user
            myref.push().setValue(hashMap);
            Log.d(TAG, "uploadToDatabase: setvalue with MyuserId = true" + myref);
        }

    }

    private void resetTimer() {
        Log.d(TAG, "resetTimer: ");
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
        Log.d(TAG, "MainActivity onPause: ");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity onStop: ");
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity onResume: ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity onDestroy: ");
    }


    public void register(View view){
        Log.d("tag", "register()");
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
//        isLoggedIn = true;
    }

    public void logIn(View view){
        Log.d(TAG, "logIn: userId : " + myUserId);
        if(myUserId == null) {
            isLoggedIn = true;
            Log.d("tag", "in logIn(), isLoggedIn useid null" );

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Log.d(TAG, "logIn: Out of Log in");
            startActivity(intent);
            Log.d(TAG, "logIn: ");
            myUserId = FirebaseAuth.getInstance().getUid();
            Log.d(TAG, "logIn: User Id after log in = " + myUserId);
        }else{
            Toast.makeText(this, "User Already Logged In", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "logIn: User Alreadt Logged in");
        }
    }

    public void logOut(View view){
        if(myUserId != null) {
            isLoggedIn = false;
            Log.d(TAG, "logOut: myUserId = " + myUserId);
            FirebaseAuth.getInstance().signOut();
            Log.d("tag", "SignedOut");
            myUserId = null;
            Log.d(TAG, "logOut: After Sign out myUserId = " + myUserId);
        }else{
            Toast.makeText(this, "User Not Logged In", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "logOut: User need to Log in");
            isLoggedIn = false;
        }
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

    public void showAppList(View view) {
        Log.d(TAG, "showAppList: showAppList() Started");
        startActivity(new Intent(MainActivity.this, AppListActivity.class));
    }
}
//
//
//
////TODO  menubar, ui design, whitelist
