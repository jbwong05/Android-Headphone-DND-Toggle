package com.jrw35outlook.headphonedndtoggle;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {
    private HeadphoneReceiver receiver;
    private IntentFilter filter;

    public BackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        receiver = new HeadphoneReceiver();
        filter = new IntentFilter(AudioManager.ACTION_HEADSET_PLUG);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        this.registerReceiver(receiver, filter);
        Log.d("Service", "Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        this.unregisterReceiver(receiver);
        Log.d("Service", "Service Stopped");
    }
}
