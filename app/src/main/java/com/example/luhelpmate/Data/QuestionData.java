package com.example.luhelpmate.Data;

public class QuestionData {
    private String exam, session, image, pdf, code, title, initial, key;

    public QuestionData(){
    }

    public QuestionData(String exam, String session, String image, String pdf, String code, String title, String initial, String key) {
        this.exam = exam;
        this.session = session;
        this.image = image;
        this.pdf = pdf;
        this.code = code;
        this.title = title;
        this.initial = initial;
        this.key = key;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
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
