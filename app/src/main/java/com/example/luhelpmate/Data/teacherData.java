package com.example.luhelpmate.Data;

public class teacherData {

    String name, initial, designation, email, admin, key;

    public teacherData() {
    }

    public teacherData(String name, String initial, String designation, String email, String admin, String key) {
        this.name = name;
        this.initial = initial;
        this.designation = designation;
        this.email = email;
        this.admin = admin;
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
