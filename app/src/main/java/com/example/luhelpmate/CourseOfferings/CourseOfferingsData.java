package com.example.luhelpmate.CourseOfferings;

public class CourseOfferingsData {
    private String code, title, credit, prerequisite, semester, batch, initial, key;

    CourseOfferingsData() {
    }

    public CourseOfferingsData(String code, String title, String credit, String prerequisite, String semester, String batch, String initial, String key) {
        this.code = code;
        this.title = title;
        this.credit = credit;
        this.prerequisite = prerequisite;
        this.semester = semester;
        this.batch = batch;
        this.initial = initial;
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
