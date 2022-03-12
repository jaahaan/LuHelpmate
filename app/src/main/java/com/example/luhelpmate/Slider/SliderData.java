package com.example.luhelpmate.Slider;

public class SliderData {

    String image, key;

    SliderData(){}

    public SliderData(String image, String key) {
        this.image = image;
        this.key = key;
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
}
