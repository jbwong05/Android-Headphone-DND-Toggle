package com.jrw35outlook.headphonedndtoggle;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;

public class MainActivity extends Activity {
    private static final int SET_NOTIFICATION_POLICY_REQUEST = 0;
    private static final int GET_DAYS_REQUEST = 1;
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
        if(reenableState.toReenable){
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
        if(requestCode==SET_NOTIFICATION_POLICY_REQUEST){
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
        } else if(requestCode==GET_DAYS_REQUEST){
            reenableState.updateDays(data.getBooleanArrayExtra(String.valueOf(R.string.selected_buttons_return_id)));
            String text = "";
            for(int i=0; i<7; i++){
                if(reenableState.getDaysArray()[i]){
                    text += Day.values()[i].toString() + " ";
                }
            }
            ((TextView)reenableOptions[1]).setText(text);
        }
    }

    private void setSwitch(Switch theSwitch, boolean state){
        theSwitch.setChecked(state);
    }

    public void onReenableSwitchClick(View view){
        reenableState.toReenable = !reenableState.toReenable;
        if(reenableState.toReenable){//switched to enabled
            toggleReenableOptionsVisibility(View.VISIBLE);
        } else{
            toggleReenableOptionsVisibility(View.INVISIBLE);
        }
    }

    public void onDaysButtonClick(View view){
        Intent intent = new Intent(this, DayPickerActivity.class);
        intent.putExtra(String.valueOf(R.string.extra_selected_buttons_array_id), reenableState.getDaysArray());
        startActivityForResult(intent, GET_DAYS_REQUEST);
    }

    public void onStartTimeButtonClick(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void onEndTimeButtonClick(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }
}
