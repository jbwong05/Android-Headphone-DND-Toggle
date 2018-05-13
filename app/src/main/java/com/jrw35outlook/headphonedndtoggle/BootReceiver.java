package com.jrw35outlook.headphonedndtoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class BootReceiver extends BroadcastReceiver {
    private final String CURRENT_APP_STATE_FILENAME = "state";
    private final int CHECKED = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(shouldStart(context)){
            Intent backgroundService = new Intent(context, BackgroundService.class);
            context.startService(backgroundService);
        }
    }

    private boolean shouldStart(Context context){
        boolean checked = false;
        try {
            FileInputStream inputStream = context.openFileInput(CURRENT_APP_STATE_FILENAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String currentString;
            StringBuilder stringBuilder = new StringBuilder();
            while ( (currentString = reader.readLine()) != null ) {
                stringBuilder.append(currentString);
            }
            reader.close();
            currentString = stringBuilder.toString();
            checked = (Integer.parseInt(currentString.substring(currentString.indexOf("checked:")+8, currentString.indexOf("checked:")+9))==CHECKED);
        } catch (Exception e) {
            Log. e("File", e.getMessage());
        }
        return checked;
    }
}
