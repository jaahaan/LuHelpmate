package com.example.luhelpmate.Data;

public class User {

    private String Image, name, initial, designation, email, department, admin, uid;


    public User() {
    }

    public User(String image, String name, String initial, String designation, String email, String department, String admin, String uid) {
        this.Image = image;
        this.name = name;
        this.initial = initial;
        this.designation = designation;
        this.email = email;
        this.department = department;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
