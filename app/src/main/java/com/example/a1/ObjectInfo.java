package com.example.a1;

import java.util.Calendar;
import java.util.Date;

public class ObjectInfo {
//    private String date;
    private String from;       //
    private String to;
    private String end;
    private Boolean done;
    private Date date;


    public ObjectInfo(String from, String to, String end, Boolean done, Date date) {
        this.from = from;
        this.to = to;
        this.end = end;
        this.done = done;
        this.date = date;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public ObjectInfo() {

    }


}
