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
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final int SET_NOTIFICATION_POLICY_REQUEST = 0;
    private boolean isChecked;
    private FileAccessor file;
    private ReenableState reenableState;
    private View[] reenableOptions;
    private Intent backgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file = new FileAccessor(getApplicationContext());
        isChecked = (file.fileExists(String.valueOf(R.string.current_app_state_filename))) && file.readStateFile();
        setSwitch((Switch) findViewById(R.id.serviceSwitch), isChecked);
        initializeReenableOptions();
        checkReenableFile();
        backgroundService = new Intent(this, BackgroundService.class);
    }

    @Override
    protected void onStop(){
        super.onStop();
        file.writeFile(String.valueOf(R.string.current_app_state_filename), isChecked ? String.valueOf(R.string.checked) : String.valueOf(R.string.not_checked));
        file.writeFile(String.valueOf(R.string.reenable_dnd_filename), reenableOptions.toString());
    }

    private void initializeReenableOptions(){
        reenableOptions = new View[6];
        reenableOptions[0] = findViewById(R.id.daysButton);
        reenableOptions[1] = findViewById(R.id.daysTextView);
        reenableOptions[2] = findViewById(R.id.startTimeButton);
        reenableOptions[3] = findViewById(R.id.startTimeTextView);
        reenableOptions[4] = findViewById(R.id.endTimeButton);
        reenableOptions[5] = findViewById(R.id.endTimeTextView);
    }

    private void checkReenableFile(){
        if(file.fileExists(String.valueOf(R.string.reenable_dnd_filename))){
            reenableState = file.readReenableFile();
        } else{
            reenableState = new ReenableState();
        }
        if(reenableState.isToReenable()){
            setSwitch((Switch)findViewById(R.id.reenableSwitch), true);
            ((TextView)reenableOptions[1]).setText(reenableState.getDaysString());
            ((TextView)reenableOptions[3]).setText(reenableState.getStartTime());
            ((TextView)reenableOptions[5]).setText(reenableState.getEndTime());
        } else{
            setSwitch((Switch)findViewById(R.id.reenableSwitch), false);
            toggleReenableOptionsVisibility(View.INVISIBLE);
        }
    }

    private void toggleReenableOptionsVisibility(int visibility){
        for(View view : reenableOptions){
            view.setVisibility(visibility);
        }
    }

    public void onMainSwitchClick(View view){
        try {
            isChecked = !isChecked;
            if (isChecked) {
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

    private void setSwitch(Switch theSwitch, boolean state){
        theSwitch.setChecked(state);
    }

    public void onReenableSwitchClick(View view){

    }

    public void onDaysButtonClick(View view){

    }

    public void onStartTimeButtonClick(View view){

    }

    public void onEndTimeButtonClick(View view){

    }
}
