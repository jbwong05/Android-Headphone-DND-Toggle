package com.jrw35outlook.headphonedndtoggle;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private boolean isChecked;
    private Intent backgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isChecked = false;
        backgroundService = new Intent(this, BackgroundService.class);
    }

    public void onSwitchClick(View view){
        //Switch serviceSwitch = findViewById(R.id.serviceSwitch);
        isChecked = !isChecked;
        if(isChecked){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if(!notificationManager.isNotificationPolicyAccessGranted()){
                startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS), 0);
            }
            startService(backgroundService);
        } else{
            stopService(backgroundService);
        }
    }
}
