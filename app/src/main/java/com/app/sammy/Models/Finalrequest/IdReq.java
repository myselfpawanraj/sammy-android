
package com.app.sammy.Models.Finalrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdReq {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("progress")
    @Expose
    private Integer progress;
    @SerializedName("responseFinal")
    @Expose
    private ResponseFinal responseFinal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public ResponseFinal getResponseFinal() {
        return responseFinal;
    }

    public void setResponseFinal(ResponseFinal responseFinal) {
        this.responseFinal = responseFinal;
    }

}
