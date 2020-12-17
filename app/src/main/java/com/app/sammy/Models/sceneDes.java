package com.app.sammy.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class sceneDes {

    @SerializedName("caption")
    @Expose
    private String caption;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

}