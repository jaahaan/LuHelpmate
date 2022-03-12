package com.example.luhelpmate.Advisor;

public class AdvisorData {
    private String batch, section, name1, name2, key;

    AdvisorData(){}

    public AdvisorData(String batch, String section, String name1, String name2, String key) {
        this.batch = batch;
        this.section = section;
        this.name1 = name1;
        this.name2 = name2;
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

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
