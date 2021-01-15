package edu.ib.messageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String TAG="EDUIB";
    public static final String MESSAGES_FOLDER="Messages";


    /**
     *onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Method saving message to file and making intent
     * @param view
     */
    public void saveToFile(View view) {
        EditText text=(EditText) findViewById(R.id.etxtMessage);
        String msg=text.getText().toString();
        Log.d(TAG,msg);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,msg);
        startActivity(intent);

        LocalDateTime localDateTime=LocalDateTime.now();
        String fileName= localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".txt";
        Log.d(TAG,fileName);

        File data=new File(getExternalFilesDir(MESSAGES_FOLDER), fileName);

        try(FileOutputStream os=new FileOutputStream(data)){
            os.write(msg.getBytes());
            os.close();
            Toast.makeText(this,"File Saved",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.e(TAG,e.toString());
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }
    }

    public void changeToHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}