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
    private File stateFile;

    public FileAccessor(Context theContext){
        context = theContext;
        stateFile = retrieveFile();
    }

    private File retrieveFile(){
        File directory = context.getFilesDir();
        return new File(directory, String.valueOf(R.string.current_app_state_filename));
    }

    public boolean fileExists(){
        return stateFile.exists();
    }

    public void writeFile(String filename, String checked){
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(String.valueOf(filename), Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(checked);
            writer.close();
        } catch (Exception e) {
            Log.e("File", e.getMessage());
        }
    }

    public boolean readFile(){
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
}
