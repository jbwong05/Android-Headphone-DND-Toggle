package com.jrw35outlook.headphonedndtoggle;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends Activity {
    private static final int SET_NOTIFICATION_POLICY_REQUEST = 0;
    private boolean isChecked;
    private Intent backgroundService;
    private FileAccessor file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializePrivates();
        setSwitch();
    }

    @Override
    protected void onStop(){
        super.onStop();
        file.writeFile(isChecked ? String.valueOf(R.string.checked) : String.valueOf(R.string.not_checked));
    }

    private void initializePrivates(){
        file = new FileAccessor(getApplicationContext());
        if(file.fileExists()){
            Log.d("State File", "State file exists");
            isChecked = file.readFile();
        } else{
            Log.d("State file", "State file does not exist");
            file.writeFile(String.valueOf(R.string.checked));
            isChecked = false;
        }
        backgroundService = new Intent(this, BackgroundService.class);
        backgroundService.putExtra(String.valueOf(R.string.intent_name), true);
    }

    public void onSwitchClick(View view){
        try {
            isChecked = !isChecked;
            if (isChecked ) {
                Log.i("Switch", "Switch is checked");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager!=null && !notificationManager.isNotificationPolicyAccessGranted()) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS), SET_NOTIFICATION_POLICY_REQUEST);
                } else {
                    Log.i("Background Service", "Service Starting");
                    startService(backgroundService);
                }
            } else {
                stopService(backgroundService);
            }
        } catch (Exception e){
            Log.e("Notification Manager", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Activity Result", "Method called" + requestCode);
        if(requestCode==0){
            Log.i("Activity Result", "Correct Policy Request");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager!=null && notificationManager.isNotificationPolicyAccessGranted()) {
                Log.i("Background Service", "Service Starting");
                startService(backgroundService);
            } else{
                Switch theSwitch = findViewById(R.id.serviceSwitch);
                theSwitch.setChecked(false);
                isChecked = false;
            }
        }
    }

    private void setSwitch(){
        if(isChecked){
            Switch toggle = findViewById(R.id.serviceSwitch);
            toggle.setChecked(true);
        }
    }
}
