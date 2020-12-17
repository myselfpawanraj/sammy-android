package com.app.sammy.Models;

public class TimeStamps
{
    String tag;
    Integer time;

    public TimeStamps(String tag, Integer time) {
        this.tag = tag;
        this.time = time;
    }

    public String getTag() {
        return tag;
    }

    public Integer getTime() {
        return time;
    }
}