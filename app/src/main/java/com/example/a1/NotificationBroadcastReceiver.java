package com.example.a1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NotificationBroadcastRe";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction() ;
//        if (action.equals( "notification_ignored" )) {
            Toast. makeText (context , "Notification Removed" , Toast. LENGTH_SHORT ).show() ;
            Log.d(TAG, "notification ignored");
            HomeFragment.cancelTimer();
//        }
    }
}
