
package com.app.sammy.Models.Finalrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class File {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("savedAs")
    @Expose
    private String savedAs;
    @SerializedName("length")
    @Expose
    private Double length;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavedAs() {
        return savedAs;
    }

    public void setSavedAs(String savedAs) {
        this.savedAs = savedAs;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

}
