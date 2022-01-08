package com.example.luhelpmate.Data;

public class User {

    private String profilepic;
    private String name;
    private String email;
    private String password;
    private String id;
    private String bio;


    public User() {
    }

    public User(String profilepic, String name, String email, String password, String id, String bio) {
        this.profilepic = profilepic;
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;
        this.bio = bio;
    }

    //signup constrictor
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }



    }
