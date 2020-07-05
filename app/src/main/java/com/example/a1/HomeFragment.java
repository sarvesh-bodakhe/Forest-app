package com.example.a1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class HomeFragment extends Fragment {

    private static final String TAG = "MainActivity";
    private static TextView myTextViewCountDown, myTextViewPercentage, myTextViewLeaveMeALone;
    private static Button myButtonStartCancel;
    static int seekBarValue;
    static SeekBar seekBar;
    private static CountDownTimer myCOuntDownTimwe;
    static boolean myTimerRunning;
    private static long myStartTimeInMillis = 0;
    private static long myTimeLeftInMillis = 0;
    private long myEndTime;
    private boolean isLoggedIn = false;

    /*Object Variables*/
    private String infoStartTime;
    private String infoExpectedEndTIme;
    private String infoActualEndTime;
    private Boolean isTreeTrue = false;
    static ObjectInfo currentObject;
    private GifImageView gifImageView;
    private static String myUserId ;
    private View view;
    public static ArrayList<ObjectInfo> myListOfObjects;
    DatabaseReference myRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        myButtonStartCancel = view.findViewById(R.id.myButtonStartCancel);
        myTextViewCountDown = view.findViewById(R.id.myTextViewCountDown);
        myListOfObjects = new ArrayList<ObjectInfo>();
        myTextViewPercentage = view.findViewById(R.id.myTextViewPercentage);
        myTextViewLeaveMeALone = view.findViewById(R.id.myTextViewLeaveMeALone);
//        gifImageView.canc
        myUserId = FirebaseAuth.getInstance().getUid();
        Log.d(TAG, "onCreate: myUserId = " + myUserId);
        if(myUserId == null)
            myUserId = "NoUserId";
        myRef = FirebaseDatabase.getInstance().getReference().child("profiles/").child(myUserId);
        Log.d(TAG, "initialize: myRef =  " + myRef);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    ObjectInfo currObject = myDataSnapshot.getValue(ObjectInfo.class);
                    Log.d(TAG, "onDataChange: Adding User");
                    myListOfObjects.add(currObject);
                }
                Log.d(TAG, "onDataChange: Size Of myListOfObjects " + myListOfObjects.size());
//                myAdapter = new RecycleViewAdapter(getActivity(), myListOfObjects);
//                myRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: ");
            }
        });

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
                    if(myStartTimeInMillis == 0){
                        Toast.makeText(getContext(), "Input Can't be zero", Toast.LENGTH_SHORT).show();
                    }else {
                        startTimer();
                        myTimerRunning = true;
                    }
                }
            }
        });


        return view;
    }


    private void verifyCancel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    public void seekBarFunc(){
        seekBar = view.findViewById(R.id.mySeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue = progress;
                updatePercentage(seekBarValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d("tag", "SeekBar Start");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("tag", "SeekBar Stop at "+ seekBarValue);
                updatePercentage(seekBarValue);
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
        TextView textView = view.findViewById(R.id.myTextViewPercentage);
        String str;
        String num = String.valueOf(myProgress);
//        Log.d("tag", num);
        str = num + "  / " + seekBar.getMax();
        textView.setText(str);
    }


    private void startTimer(){
//        gifImageView = view.findViewById(R.id.tree_gif);
        Log.d(TAG, "startTimer: ");
        createObjectInfo();

        seekBar.setVisibility(View.INVISIBLE);
        myTextViewPercentage.setVisibility(View.INVISIBLE);
        myTextViewLeaveMeALone.setVisibility(View.VISIBLE);
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
                String endTimeActual = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))+ ":"+
                        String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
                currentObject.setEnd(endTimeActual);
                currentObject.setDone(true);
                Log.d(TAG, "FinishedTimer: Object Description " + currentObject.getFrom()+currentObject.getTo()
                        + currentObject.getEnd() + currentObject.getDone());
                uploadToDatabase();
                updateWatchInterface();
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setProgress(0);
                myTextViewPercentage.setVisibility(View.VISIBLE);
                myTextViewLeaveMeALone.setVisibility(View.INVISIBLE);
                myStartTimeInMillis = myTimeLeftInMillis = 0;
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


    static void cancelTimer(){
        Log.d(TAG, "cancelTimer: ");
        String endTimeActual = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))+ ":"+
                String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        Log.d(TAG, "cancelTimer: endTimeActual : " + endTimeActual);
        Log.d(TAG, "cancelTimer: currentObject.getTo() " + currentObject.getTo());
        currentObject.setEnd(endTimeActual);
        currentObject.setDone(false);
        uploadToDatabase();
        seekBar.setVisibility(View.VISIBLE);
        myTextViewPercentage.setVisibility(View.VISIBLE);
        myTextViewLeaveMeALone.setVisibility(View.INVISIBLE);
        myTimeLeftInMillis = 0;
        myStartTimeInMillis = 0;
        myTimerRunning = false;
        updateCounterView();
        updateWatchInterface();
        myCOuntDownTimwe.cancel();
        Log.d(TAG, "cancelTimer: Object Description " + currentObject.getFrom()+currentObject.getTo()
                + currentObject.getEnd() + currentObject.getDone());
        myTextViewPercentage.setText("00 / 180");
        seekBar.setProgress(0);
    }


    public static void uploadToDatabase(){
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


    private static void updateCounterView(){
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


    private static void updateWatchInterface(){
        if(myTimerRunning){
            myButtonStartCancel.setText("cancel");
        }else{
            myButtonStartCancel.setText("Start");
        }
    }
}


