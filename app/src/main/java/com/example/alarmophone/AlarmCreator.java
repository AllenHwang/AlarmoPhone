package com.example.alarmophone;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmCreator extends Activity {


    public static Ringtone ringtone;
    private static AlarmCreator inst;




    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_create);

        alarmTextView =  findViewById(R.id.alarmText);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmReceiver = new AlarmReceiver();



    }

    public void onToggleClicked(View view) {

    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}