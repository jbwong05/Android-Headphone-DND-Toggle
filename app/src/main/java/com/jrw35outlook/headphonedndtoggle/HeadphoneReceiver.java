package com.jrw35outlook.headphonedndtoggle;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.widget.Toast;

public class HeadphoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS);
        if(devices.length!=0){
            if(headphonesPluggedIn(devices)){
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }
        }
    }

    private boolean headphonesPluggedIn(AudioDeviceInfo[] devices){
        int currentType;
        for(AudioDeviceInfo device : devices) {
            currentType = device.getType();
            if(currentType==AudioDeviceInfo.TYPE_WIRED_HEADSET || currentType==AudioDeviceInfo.TYPE_WIRED_HEADPHONES){
                return true;
            }
        }
        return false;
    }
}
