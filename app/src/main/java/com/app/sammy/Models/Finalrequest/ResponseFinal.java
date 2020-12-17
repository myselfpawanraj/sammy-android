
package com.app.sammy.Models.Finalrequest;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFinal {

    @SerializedName("file")
    @Expose
    private File file;
    @SerializedName("captions")
    @Expose
    private List<Caption> captions = null;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<Caption> getCaptions() {
        return captions;
    }

    public void setCaptions(List<Caption> captions) {
        this.captions = captions;
    }

}
