package com.jrw35outlook.headphonedndtoggle;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class NotificationPolicyReceiver extends BroadcastReceiver {
    private final int NOT_CHECKED = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager!=null && !notificationManager.isNotificationPolicyAccessGranted()) {
            writeFile(context, NOT_CHECKED);
            Intent serviceIntent = new Intent(context, BackgroundService.class);
            Log.i("Service", "Attempting to stop service");
            context.stopService(serviceIntent);
            Toast.makeText(context, "Headphone DND Service Stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFile(Context context, int checked){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(String.valueOf(R.string.current_app_state_filename), Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write("" + checked);
            writer.close();
        } catch (Exception e) {
            Log.e("File", e.getMessage());
        }
    }
}
