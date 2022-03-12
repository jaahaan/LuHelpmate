package com.example.luhelpmate.CourseList;

public class CourseData {
    String code, title, credit, prerequisite, key;

    public CourseData() {

    }

    public CourseData(String code, String title, String credit, String prerequisite, String key) {
        this.code = code;
        this.title = title;
        this.credit = credit;
        this.prerequisite = prerequisite;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
