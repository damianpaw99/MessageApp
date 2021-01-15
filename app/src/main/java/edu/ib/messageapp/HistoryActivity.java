package edu.ib.messageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ArrayList<HistoryItem> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent=getIntent();

        RecyclerView rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
        File fileDirectory = new File(String.valueOf(getExternalFilesDir(MainActivity.MESSAGES_FOLDER)));
        FileFilter filter = pathname -> pathname.getName().endsWith(".txt");
        File[] files = fileDirectory.listFiles(filter);
        try {
            for (int i = 0; i < files.length; i++) {
                try (FileInputStream is = new FileInputStream(files[i])) {
                    int data = is.read();
                    StringBuilder stringBuilder = new StringBuilder();
                    while (data != -1) {
                        stringBuilder.append((char) data);
                        data = is.read();
                    }
                    history.add(new HistoryItem(stringBuilder.toString(), files[i].getName().split("\\.")[0]));
                } catch (FileNotFoundException e) {
                    Log.e(MainActivity.TAG, e.toString());
                } catch (IOException e) {
                    Log.e(MainActivity.TAG, e.toString());
                }
            }
        } catch (NullPointerException e) {
            Log.e(MainActivity.TAG, e.toString());
        }
        HistoryItemAdapter historyAdapter = new HistoryItemAdapter(history);
        rvHistory.setAdapter(historyAdapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
    }
}