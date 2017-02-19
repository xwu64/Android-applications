package edu.iit.xwu64.hw3_multi_notepad;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiaoliangwu on 2017/2/16.
 */

public class Note implements Serializable {
    private int id;
    private String date;
    private String title;
    private String content;

    public Note(int id, String date, String title, String content){
        this.id = id;
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public int getID(){
        return id;
    }

    public String getDate(){
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
