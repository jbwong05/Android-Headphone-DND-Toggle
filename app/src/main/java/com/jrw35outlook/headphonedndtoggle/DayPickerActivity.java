package com.jrw35outlook.headphonedndtoggle;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class DayPickerActivity extends Activity {
    private boolean[] selectedDays;
    private CheckBox[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_picker);
        selectedDays = getIntent().getBooleanArrayExtra(String.valueOf(R.string.extra_selected_buttons_array_id));
        setUpButtons();
    }

    private void setUpButtons(){
        buttons = new CheckBox[7];
        buttons[0] = findViewById(R.id.sundayButton);
        buttons[1] = findViewById(R.id.mondayButton);
        buttons[2] = findViewById(R.id.tuesdayButton);
        buttons[3] = findViewById(R.id.wednesdayButton);
        buttons[4] = findViewById(R.id.thursdayButton);
        buttons[5] = findViewById(R.id.fridayButton);
        buttons[6] = findViewById(R.id.saturdayButton);
        for(int i=0; i<7; i++){
            buttons[i].setChecked(selectedDays[i]);
        }
    }

    public void onDoneButtonClick(View view){
        super.onPause();
        for(int i=0; i<7; i++){
            selectedDays[i] = buttons[i].isChecked();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra(String.valueOf(R.string.selected_buttons_return_id), selectedDays);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
