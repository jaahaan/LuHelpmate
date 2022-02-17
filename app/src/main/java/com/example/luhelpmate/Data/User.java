package com.example.luhelpmate.Data;

public class User {

    private String Image, name, initial, designation, email, admin, uid;


    public User() {
    }

    public User(String image, String name, String initial, String designation, String email, String admin, String uid) {
        Image = image;
        this.name = name;
        this.initial = initial;
        this.designation = designation;
        this.email = email;
        this.admin = admin;
        this.uid = uid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
