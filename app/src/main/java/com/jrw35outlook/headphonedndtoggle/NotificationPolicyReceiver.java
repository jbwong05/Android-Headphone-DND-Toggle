package com.jrw35outlook.headphonedndtoggle;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class NotificationPolicyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager!=null && !notificationManager.isNotificationPolicyAccessGranted()) {
            FileAccessor file = new FileAccessor(context);
            file.writeFile(String.valueOf(R.string.not_checked));
            Intent serviceIntent = new Intent(context, BackgroundService.class);
            Log.i("Service", "Attempting to stop service");
            context.stopService(serviceIntent);
            Toast.makeText(context, "Headphone DND Service Stopped", Toast.LENGTH_SHORT).show();
        }
    }
}
