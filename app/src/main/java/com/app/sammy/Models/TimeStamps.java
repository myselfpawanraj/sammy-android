package com.app.sammy.Models;

public class TimeStamps
{
    String tag,time;

    public TimeStamps(String tag, String time) {
        this.tag = tag;
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public String getTime() {
        return time;
    }
}