package com.example.luhelpmate.Data;

public class BatchData {
    private String batch, section, key;

    public BatchData() {
    }

    public BatchData(String batch, String section, String key) {
        this.batch = batch;
        this.section = section;
        this.key = key;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
