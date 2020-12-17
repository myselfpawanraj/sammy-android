package com.app.sammy.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Responce {

    @SerializedName("id1")
    @Expose
    private String id1;

    @SerializedName("id2")
    @Expose
    private String id2;

    public String getId1() {
        return id1;
    }

    public void setId(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

}