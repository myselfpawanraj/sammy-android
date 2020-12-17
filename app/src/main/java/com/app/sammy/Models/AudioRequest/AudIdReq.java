
package com.app.sammy.Models.AudioRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudIdReq {

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
    private AudResponseFinal audResponseFinal;

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

    public AudResponseFinal getAudResponseFinal() {
        return audResponseFinal;
    }

    public void setAudResponseFinal(AudResponseFinal audResponseFinal) {
        this.audResponseFinal = audResponseFinal;
    }

}
