package com.app.sammy.Models;

public class TimeStamps
{
    String tag;
    long time;

    public TimeStamps(String tag, long time) {
        this.tag = tag;
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public long getTime() {
        return time;
    }
}