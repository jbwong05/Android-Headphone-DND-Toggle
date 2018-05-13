package com.jrw35outlook.headphonedndtoggle;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    private final String CURRENT_APP_STATE_FILENAME = "state";
    private final int CHECKED = 1;
    private final int NOT_CHECKED = 0;
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
        writeFile(isChecked ? 1 : 0);
    }

    public void onSwitchClick(View view){
        try {
            isChecked = !isChecked;
            if (isChecked) {
                Log.d("Switch", "Switch is checked");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager!=null && !notificationManager.isNotificationPolicyAccessGranted()) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS), 0);
                }
                Log.d("Background Service", "Service Starting");
                startService(backgroundService);
            } else {
                stopService(backgroundService);
            }
        } catch (Exception e){
            Log.e("Notification Manager", e.getMessage());
        }
    }

    private File retrieveFile(){
        File directory = getApplicationContext().getFilesDir();
        return new File(directory, CURRENT_APP_STATE_FILENAME);
    }

    private void initializePrivates(){
        if(appStateFile.exists()){
            isChecked = readFile();
        } else{
            writeFile(NOT_CHECKED);
            isChecked = false;
        }
        backgroundService = new Intent(this, BackgroundService.class);
    }

    private boolean readFile(){
        boolean checked = false;
        try {
            FileInputStream inputStream = getApplicationContext().openFileInput(CURRENT_APP_STATE_FILENAME);
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
            Log.e("File", e.getMessage());
        }
        return checked;
    }

    private void writeFile(int checked){
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(CURRENT_APP_STATE_FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write("checked:" + checked + "\n");
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
