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
    public static final String TAG="Main";
    public static final String MESSAGES_FOLDER="Messages";
    public static final String OTHER_FILES="Other";
    public static final String EXTRA_MESSAGE="message";
    private final ArrayList<String> msgList=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    public static final String LIST_OF_FILES="listOfFiles.txt";

    /**
     *onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,msgList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditText editText = (EditText) findViewById(R.id.etxtMessage);
                editText.setText(((String) parent.getItemAtPosition(position)).split("-")[1]);
            }
        });
        //loading messages from files
        File fileList=new File(getExternalFilesDir(OTHER_FILES), LIST_OF_FILES);
        if(fileList.exists()){
            try(FileInputStream is=new FileInputStream(fileList)){
                byte [] bytes=new byte[10000];
                Log.d(TAG, String.valueOf(is.read(bytes)));
                is.close();
                String msg=new String(bytes);
                msg=msg.substring(0,msg.lastIndexOf('t')+1);
                Log.d(TAG,msg);
                String [] fileNames=msg.split("\n");
                for(int i=1;i<fileNames.length;i++){
                    Log.d(TAG,i+" "+fileNames[i]);
                    LocalDateTime localDateTime=LocalDateTime.parse(fileNames[i].split("\\.")[0],DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                    File loadFile=new File(getExternalFilesDir(MESSAGES_FOLDER),fileNames[i]);
                    FileInputStream inputStream=new FileInputStream(loadFile);
                    byte [] msgBytes=new byte[inputStream.read()];
                    inputStream.read(msgBytes);
                    inputStream.close();
                    String message=new String(msgBytes);
                    msgList.add(localDateTime.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"))+"-"+message);
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG,e.toString());
            } catch (IOException e) {
                Log.e(TAG,e.toString());
            }
        } else{
            try {
                fileList.createNewFile();
            } catch (IOException e) {
                Log.e(TAG,e.toString());
            }
        }

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
        intent.putExtra(EXTRA_MESSAGE,msg);
        startActivity(intent);

        LocalDateTime localDateTime=LocalDateTime.now();
        String date =  localDateTime.format(DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        msgList.add(date+"-"+msg);
        arrayAdapter.notifyDataSetChanged();
        String fileName= localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".txt";
        Log.d(TAG,fileName);

        File data=new File(getExternalFilesDir(MESSAGES_FOLDER), fileName);

        try(FileOutputStream os=new FileOutputStream(data)){
            os.write(msg.getBytes());
            os.close();
            File listOfFiles=new File(getExternalFilesDir(OTHER_FILES),LIST_OF_FILES);
            FileInputStream inputStream=new FileInputStream(listOfFiles);
            byte [] input=new byte[10000];
            inputStream.read(input);
            inputStream.close();
            FileOutputStream outputStream=new FileOutputStream(listOfFiles);
            String oldFileNames=new String(input);
            oldFileNames=oldFileNames.substring(0,oldFileNames.lastIndexOf('t')+1);
            Log.d(TAG,oldFileNames);
            outputStream.write((oldFileNames+"\n"+fileName).getBytes());
            outputStream.close();
            Toast.makeText(this,"File Saved",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Log.e(TAG,e.toString());
        } catch (IOException e) {
            Log.e(TAG,e.toString());
        }
    }
}