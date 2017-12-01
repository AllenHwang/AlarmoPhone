package com.example.alarmophone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TimersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers);
    }
    public void timerActivity(View view){
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }
}
