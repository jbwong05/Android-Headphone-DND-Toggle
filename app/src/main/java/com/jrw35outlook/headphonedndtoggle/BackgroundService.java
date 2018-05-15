package com.jrw35outlook.headphonedndtoggle;

import android.Manifest;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {
    private HeadphoneReceiver headphoneReceiver;
    private IntentFilter headphoneFilter;
    private NotificationPolicyReceiver policyReceiver;
    private IntentFilter policyFilter;

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        headphoneReceiver = new HeadphoneReceiver();
        headphoneFilter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
        policyReceiver = new NotificationPolicyReceiver();
        policyFilter = new IntentFilter(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        this.registerReceiver(headphoneReceiver, headphoneFilter, Manifest.permission.ACCESS_NOTIFICATION_POLICY, null);
        this.registerReceiver(policyReceiver, policyFilter);
        Log.d("Service", "Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        this.unregisterReceiver(headphoneReceiver);
        this.unregisterReceiver(policyReceiver);
        Log.d("Service", "Service Stopped");
    }
}
