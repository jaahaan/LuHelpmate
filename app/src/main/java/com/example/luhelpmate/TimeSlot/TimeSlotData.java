package com.example.luhelpmate.TimeSlot;

public class TimeSlotData {
    private String Time, key;

    TimeSlotData(){}

    public TimeSlotData(String time, String key) {
        Time = time;
        this.key = key;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
