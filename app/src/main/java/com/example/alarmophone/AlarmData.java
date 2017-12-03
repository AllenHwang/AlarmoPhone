package com.example.alarmophone;

/**
 * Created by j-jizzle on 3/12/17.
 */

public class AlarmData {
    public static int idCounter = 0;
    public int id;
    public String minute;
    public String hour;
    public String amPm;
    public String name;

    public AlarmData(String min, String hr, String amPm, String name){
        minute = min;
        hour = hr;
        this.amPm = amPm;
        this.name = name;
        id = AlarmData.idCounter;
        AlarmData.idCounter++;
    }


}
