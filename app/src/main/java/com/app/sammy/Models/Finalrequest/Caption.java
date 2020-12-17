
package com.app.sammy.Models.Finalrequest;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Caption {

    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("captions")
    @Expose
    private String captions;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("ocr")
    @Expose
    private String ocr;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getOcr() {
        return ocr;
    }

    public void setOcr(String ocr) {
        this.ocr = ocr;
    }

}
