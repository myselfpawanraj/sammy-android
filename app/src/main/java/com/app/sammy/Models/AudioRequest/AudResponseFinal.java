
package com.app.sammy.Models.AudioRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AudResponseFinal {

    @SerializedName("file")
    @Expose
    private File file;
    @SerializedName("words")
    @Expose
    private List< Word > words = null;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

}
