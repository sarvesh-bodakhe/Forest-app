package com.example.a1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirstService extends Service {
    private static final String TAG = "MyFirstService";
    private CountDownTimer myTimer;
    private NotificationManagerCompat notificationManager;
//    private NotificationHelper myNotificationHelper;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
//        myNotificationHelper = new NotificationHelper(this);


//        return super.onStartCommand(intent, flags, startId);
        myTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "service tick" + millisUntilFinished);
                if(millisUntilFinished < 6000 && millisUntilFinished > 5000) {
                    createNotification();

                }
            }

            @Override
            public void onFinish() {
                stopSelf();
//                HomeFragment.cancelTimer();
                Log.d(TAG, "Service timer finished ");
            }
        }.start();

        return START_STICKY;
    }

    private void createNotification() {
        Intent notificationIntent = new Intent(getApplicationContext() , MainActivity. class ) ;
        notificationIntent.putExtra( "fromNotification" , true ) ;
//        notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        PendingIntent pendingIntent = PendingIntent. getActivity ( this, 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT ) ;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(getApplicationContext() , SplashActivity.CHANNEL_1_ID ) ;
        mBuilder.setContentTitle( "Warning!!!" ) ;
        mBuilder.setContentIntent(pendingIntent) ;
        mBuilder.setContentText( "Click Here To Save Your tree in 5 seconds" ) ;
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
//        mBuilder.setDeleteIntent(getDeleteIntent()) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( SplashActivity.CHANNEL_1_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( SplashActivity.CHANNEL_1_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }

    protected PendingIntent getDeleteIntent () {
        Log.d(TAG, "getDeleteIntent: ");
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class ) ;
        intent.setAction( "notification_ignored" ) ;
        return PendingIntent. getBroadcast (this, 0 , intent , PendingIntent. FLAG_CANCEL_CURRENT ) ;
    }

//    public NotificationCompat.Builder getChannel1Notification(String title , String message){
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        return  new NotificationCompat.Builder(getApplicationContext(), SplashActivity.CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.upset_tree_card_view)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setContentIntent(resultPendingIntent);
//    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
//        myTimer.cancel();
//        stopService(new Intent(this, MyFirstService.class));
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind: ");
        super.onRebind(intent);
    }

}
