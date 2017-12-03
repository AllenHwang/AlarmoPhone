package com.example.alarmophone;

import android.app.Activity;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmCreator extends Activity {


    public static Ringtone ringtone;

    private Spinner hour, minute, amPm;
    private EditText alarmName;
    private Button createAlarm;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_create);
        hour = (Spinner) findViewById(R.id.spinnerHour);
        hour.setSelection(0);
        minute = (Spinner) findViewById(R.id.spinnerMinute);
        minute.setSelection(0);
        amPm = (Spinner) findViewById(R.id.spinnerAMPM);
        amPm.setSelection(0);
        alarmName = (EditText) findViewById(R.id.alarmNameCreate);
        createAlarm = (Button) findViewById(R.id.buttonCreate);
        createAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmOverview.arrayAdapter.add(new AlarmData(getHour(), getMinute(), getAMPM(), getName()));
                AlarmOverview.arrayAdapter.notifyDataSetChanged();
                finish();
            }
        });
    }

    private String getHour(){
        return (String) hour.getSelectedItem();
    }
    private String getMinute(){
        return (String) minute.getSelectedItem();
    }
    private String getAMPM(){
        return (String) amPm.getSelectedItem();
    }
    private String getName(){
        return (String) alarmName.getText().toString();
    }


}