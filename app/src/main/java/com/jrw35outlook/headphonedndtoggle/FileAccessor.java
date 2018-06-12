package com.jrw35outlook.headphonedndtoggle;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileAccessor {
    private Context context;

    public FileAccessor(Context theContext){
        context = theContext;
    }

    public boolean fileExists(String filename){
        File directory = context.getFilesDir();
        return new File(directory, filename).exists();
    }

    public void writeFile(String filename, String checked){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(checked);
            writer.close();
        } catch (Exception e) {
            Log.e("File", e.getMessage());
        }
    }

    public boolean readStateFile(){
        boolean checked = false;
        try {
            FileInputStream inputStream = context.openFileInput(String.valueOf(R.string.current_app_state_filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String currentString;
            StringBuilder stringBuilder = new StringBuilder();
            while ( (currentString = reader.readLine()) != null ) {
                stringBuilder.append(currentString);
            }
            reader.close();
            Log.i("File", stringBuilder.toString());
            checked = stringBuilder.toString().equals(String.valueOf(R.string.checked));
        } catch (Exception e) {
            Log.e("File", e.toString());
        }
        return checked;
    }

    public ReenableState readReenableFile(){
        /*
        file format:
        1 //flag to re-enable
        1001011 //flags for days
        1205a //start time without :
        0636a //end time without :
         */
        ReenableState reenableState = new ReenableState();
        try {
            FileInputStream inputStream = context.openFileInput(String.valueOf(R.string.reenable_dnd_filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String currentString;
            currentString = reader.readLine();
            if(currentString.substring(0,1).equals(String.valueOf(R.string.renable))){
                currentString = reader.readLine();
                reenableState.setDays(currentString);
                currentString = reader.readLine();
                reenableState.setStartTime(currentString);
                currentString = reader.readLine();
                reenableState.setEndTime(currentString);
            } else{
                reenableState.toReenable = false;
            }
            reader.close();
            Log.i("File", currentString);
        } catch (Exception e) {
            Log.e("File", e.toString());
        }
        return reenableState;
    }
}
