
package com.app.sammy.Models.AudioRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("length")
    @Expose
    private Double length;
    @SerializedName("savedAs")
    @Expose
    private Object savedAs;

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Object getSavedAs() {
        return savedAs;
    }

    public void setSavedAs(Object savedAs) {
        this.savedAs = savedAs;
    }

}
