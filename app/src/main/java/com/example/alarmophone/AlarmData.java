package com.example.alarmophone;

import android.media.Ringtone;

import java.util.HashMap;
import java.util.Map;

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
    public int cascadeAmount;
    public int intervalMinute;

    public Map<Integer, Ringtone> ringtoneMap;

    public AlarmData(String hr, String min, String amPm, String name, int amount, int intervalMinute){
        minute = min;
        hour = hr;
        this.amPm = amPm;
        this.name = name;
        this.cascadeAmount = amount;
        this.intervalMinute = intervalMinute;

        id = AlarmData.idCounter;
        AlarmData.idCounter++;
        ringtoneMap = new HashMap<Integer, Ringtone>();
    }

    @Override
    public int hashCode(){
       return ( (hour.hashCode() * minute.hashCode() * amPm.hashCode() * name.hashCode() * cascadeAmount + (cascadeAmount - id) * intervalMinute));
    }

}
