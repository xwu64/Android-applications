package edu.iit.xwu64.hw3_multi_notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerView recyclerView;
    private List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private static final int Edit_REQ = 1;
    private String filename = "database.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.myrecycler);
        noteAdapter = new NoteAdapter(noteList, this);

        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new AsyncRead().execute();

        //noteList.add(new Note(0, "abc", "cdf", "00000000000000000000000000000000000000000000000000000000000000000000000000000001000000000"));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menuCreate:
                intent = new Intent(this, EditActivity.class);
                Note note = new Note(noteList.size(), "", "", "");
                intent.putExtra("note", note);
                startActivityForResult(intent, Edit_REQ);

                return true;
            case R.id.menuInfo:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Edit_REQ){
            if (resultCode == RESULT_OK){
                Note note = (Note) data.getSerializableExtra("note");
                Log.d("noteStringMain", ""+note.getID()+note.getTitle()+note.getContent()+note.getDate());

                if (note.getID()<noteList.size())
                {
                    int i = 0;
                    for(Iterator iter = noteList.iterator(); iter.hasNext();){
                        Note tmp = (Note) iter.next();
                        if (tmp.getID() == note.getID()) break;
                        i++;
                    }
                    noteList.remove(i);
                }

                noteList.add(0, note);
                noteAdapter.notifyDataSetChanged();

            } else{
                Log.d("Main", "onActivityResult: result Code: " + resultCode);
            }
        }else{
            Log.d("Main","onActivityResult: request Code: " + requestCode);
        }
    }

    @Override
    public boolean onLongClick(final View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                noteList.remove(pos);
                noteAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return false;
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Note note = noteList.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, Edit_REQ);
        Log.d("onclick", "123");
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            FileOutputStream fos = openFileOutput(filename, 0);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter((OutputStream) fos, "UTF-8"));
            writer.setIndent("    ");
            writer.beginArray();
            for(Iterator iter = noteList.iterator(); iter.hasNext();){
                Note note = (Note) iter.next();
                writer.beginObject();
                writer.name("id").value(note.getID());
                writer.name("title").value(note.getTitle());
                writer.name("date").value(note.getDate());
                writer.name("content").value(note.getContent());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class AsyncRead extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileInputStream fis = openFileInput(filename);
                JsonReader reader = new JsonReader(new InputStreamReader((InputStream) fis, "UTF-8"));
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    String title="";
                    String content="";
                    String date="";
                    int id=0;
                    while(reader.hasNext()){
                        String name = reader.nextName();
                        if (name.equals("id")){
                            id = reader.nextInt();
                        }else if(name.equals("title")){
                            title = reader.nextString();
                        }else if (name.equals("content")){
                            content = reader.nextString();
                        }else if (name.equals("date")){
                            date = reader.nextString();
                        }else{
                            reader.skipValue();
                        }
                    }
                    Note note = new Note(id,date,title,content);
                    noteList.add(note);
                    reader.endObject();

                }
                reader.endArray();
                reader.close();

            } catch (FileNotFoundException e) {
                try {
                    FileOutputStream fos = openFileOutput(filename,0);
                    fos.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

//    public void read(){
//        try {
//            FileInputStream fis = openFileInput(filename);
//            JsonReader reader = new JsonReader(new InputStreamReader((InputStream) fis, "UTF-8"));
//            reader.beginArray();
//            reader.beginObject();
//            while(reader.hasNext()){
//                String name = reader.nextName();
//                if (name.equals("date")){
//                    Log.d("read:", "date");
//                    date = reader.nextString();
//                }else{
//                    contentText = reader.nextString();
//                }
//            }
//            reader.endObject();
//            reader.endArray();
//            reader.close();
//            Log.d("read:", "no e");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.d("read:", "e");
//            try {
//                FileOutputStream fos = openFileOutput(filename, 0);
//                date = getTime();
//                contentText = "";
//                JsonWriter writer = new JsonWriter(new OutputStreamWriter((OutputStream) fos, "UTF-8"));
//                writer.setIndent("    ");
//                writer.beginArray();
//                writer.beginObject();
//                writer.name("date").value(date);
//                writer.name("content").value(contentText);
//                writer.endObject();
//                writer.endArray();
//                writer.close();
//            }catch (FileNotFoundException e0){
//                e0.printStackTrace();
//            } catch (UnsupportedEncodingException e1) {
//                e1.printStackTrace();
//            } catch (IOException e1){
//                e1.printStackTrace();
//            }
//
//        } catch (UnsupportedEncodingException e1){
//            e1.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //Log.d("date", date);
//
//        if (date == null || date.length() == 0){
//
//            date = getTime();
//        }
//        updateTime.setText(date);
//        noteText.setText(contentText);
//    }

}
