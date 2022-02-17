package com.example.luhelpmate.Data;

import android.widget.AdapterView;

public class FacultyData {

    String name, designation, initial, phone, email, image, key, no;

    public FacultyData() {

    }

    public FacultyData(String name, String initial, String designation, String phone, String email, String image, String key, String no) {
        this.name = name;
        this.initial = initial;
        this.designation = designation;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.key = key;
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
