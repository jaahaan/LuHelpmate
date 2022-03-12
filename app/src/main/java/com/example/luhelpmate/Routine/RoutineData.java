package com.example.luhelpmate.Routine;

public class RoutineData {
    private String initial, day, timeSlot, department, batch, section, code, room, key;

    RoutineData(){}

    public RoutineData(String initial, String day, String timeSlot, String department, String batch, String section, String code, String room, String key) {
        this.initial = initial;
        this.day = day;
        this.timeSlot = timeSlot;
        this.department = department;
        this.batch = batch;
        this.section = section;
        this.code = code;
        this.room = room;
        this.key = key;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
