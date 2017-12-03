package com.example.alarmophone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main Menu");
        setContentView(R.layout.activity_main_menu);
    }
    /**Called when the user presses the Alarms button */
    public void alarmsActivity(View view){
        Intent intent = new Intent(this, AlarmOverview.class);
        startActivity(intent);

    }
    public void timerActivity(View view){
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }
}
