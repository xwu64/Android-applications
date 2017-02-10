package edu.iit.xwu64.hw2_notepad;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView updateTime;
    private EditText noteText;
    private  String filename="log.json";
    private String date, contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateTime = (TextView) findViewById(R.id.lastUpdateTime);
        noteText = (EditText) findViewById(R.id.noteText);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "True");
        read();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause", "True");
        write();
    }

    public String getTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        return df.format(c.getTime());
    }

    public void read(){
        try {
            FileInputStream fis = openFileInput(filename);
            JsonReader reader = new JsonReader(new InputStreamReader((InputStream) fis, "UTF-8"));
            reader.beginArray();
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                if (name.equals("date")){
                    Log.d("read:", "date");
                    date = reader.nextString();
                }else{
                    contentText = reader.nextString();
                }
            }
            reader.endObject();
            reader.endArray();
            reader.close();
            Log.d("read:", "no e");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("read:", "e");
            try {
                FileOutputStream fos = openFileOutput(filename, 0);
                date = getTime();
                contentText = "";
                JsonWriter writer = new JsonWriter(new OutputStreamWriter((OutputStream) fos, "UTF-8"));
                writer.setIndent("    ");
                writer.beginArray();
                writer.beginObject();
                writer.name("date").value(date);
                writer.name("content").value(contentText);
                writer.endObject();
                writer.endArray();
                writer.close();
            }catch (FileNotFoundException e0){
                e0.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1){
                e1.printStackTrace();
            }

        } catch (UnsupportedEncodingException e1){
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d("date", date);

        if (date == null || date.length() == 0){

            date = getTime();
        }
        updateTime.setText(date);
        noteText.setText(contentText);
    }

    public void write(){
        try {
            FileOutputStream fos = openFileOutput(filename, 0);
            date = getTime();
            contentText = noteText.getText().toString();
            JsonWriter writer = new JsonWriter(new OutputStreamWriter((OutputStream) fos, "UTF-8"));
            writer.setIndent("    ");
            writer.beginArray();
            writer.beginObject();
            writer.name("date").value(date);
            writer.name("content").value(contentText);
            writer.endObject();
            writer.endArray();
            writer.close();
        }catch (FileNotFoundException e0){
            e0.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1){
            e1.printStackTrace();
        }

    }

}
