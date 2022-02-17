package com.example.luhelpmate.Data;

public class ClubsData {
    private String name, image;

    public ClubsData(){}

    public ClubsData(String image, String  name){
        this.image=image;
        this.name=name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }
}
