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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {
    private final int SET_NOTIFICATION_POLICY_REQUEST = 0;
    private File appStateFile;
    private boolean isChecked;
    private Intent backgroundService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appStateFile = retrieveFile();
        initializePrivates();
        setSwitch();
    }

    @Override
    protected void onStop(){
        super.onStop();
        writeFile(isChecked ? String.valueOf(R.string.checked) : String.valueOf(R.string.not_checked));
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

    private File retrieveFile(){
        File directory = getApplicationContext().getFilesDir();
        return new File(directory, String.valueOf(R.string.current_app_state_filename));
    }

    private void initializePrivates(){
        if(appStateFile.exists()){
            Log.d("State File", "State file exists");
            isChecked = readFile();
        } else{
            Log.d("State file", "State file does not exist");
            writeFile(String.valueOf(R.string.checked));
            isChecked = false;
        }
        backgroundService = new Intent(this, BackgroundService.class);
    }

    private boolean readFile(){
        boolean checked = false;
        try {
            FileInputStream inputStream = getApplicationContext().openFileInput(String.valueOf(R.string.current_app_state_filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String currentString;
            StringBuilder stringBuilder = new StringBuilder();
            while ( (currentString = reader.readLine()) != null ) {
                stringBuilder.append(currentString);
            }
            reader.close();
            Log.d("File", stringBuilder.toString());
            checked = stringBuilder.toString().equals(String.valueOf(R.string.checked));
        } catch (Exception e) {
            Log.e("File", e.toString());
        }
        return checked;
    }

    private void writeFile(String checked){
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(String.valueOf(R.string.current_app_state_filename), Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(checked);
            writer.close();
        } catch (Exception e) {
            Log.e("File", e.getMessage());
        }
    }

    private void setSwitch(){
        if(isChecked){
            Switch toggle = findViewById(R.id.serviceSwitch);
            toggle.setChecked(true);
        }
    }
}
