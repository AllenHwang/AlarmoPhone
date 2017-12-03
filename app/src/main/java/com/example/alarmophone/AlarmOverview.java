package com.example.alarmophone;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmOverview extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<AlarmData> arrayAdapter;
    private ArrayList<AlarmData> alarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_overview);
        listView = (ListView) findViewById(R.id.listViewAlarms);
        arrayAdapter = new AlarmArrayAdapter();
        listView.setAdapter(arrayAdapter);
    }
    /**Called when the Add Alarm button is pressed */
    public void newAlarm(View view){
        Intent intent = new Intent(this, AlarmCreator.class);
        startActivity(intent);
    }

    if (((ToggleButton) view).isChecked()) {
        Log.d("MyActivity", "Alarm On");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        Context context = this.getApplicationContext();
        if(alarmReceiver != null){
            alarmReceiver.SetAlarm(context, calendar.getTimeInMillis());
            Toast.makeText(AlarmCreator.this, "Alarm On", Toast.LENGTH_SHORT).show();
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
        }
    } else {
        Log.d("MyActivity", "Alarm Off");
        Context context = this.getApplicationContext();
        if(alarmReceiver != null){
            alarmReceiver.CancelAlarm(context);
            ringtone.stop();
            Toast.makeText(AlarmCreator.this, "Alarm Off", Toast.LENGTH_SHORT).show();
        }
    }
}

