package com.example.alarmophone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AlarmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
    }
    /**Called when the Add Alarm button is pressed */
    public void newAlarm(View view){
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);


    }
}