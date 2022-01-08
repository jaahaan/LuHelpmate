package com.example.luhelpmate.Data;

public class CrData {

    String batch, section, name, id, phone, email, image, key;

    public CrData() {

    }

    public CrData(String batch, String section, String name, String id, String phone, String email, String image, String key) {
        this.batch = batch;
        this.section = section;
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.key = key;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
