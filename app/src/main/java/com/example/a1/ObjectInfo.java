package com.example.a1;

public class ObjectInfo {
    private String from;       //
    //private int timerValue;         //In minutes
    private String to;
    private String end;
    private Boolean done;

    public ObjectInfo(String from, String to, String end, Boolean done) {
        this.from = from;
        this.to = to;
        this.end = end;
        this.done = done;
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
