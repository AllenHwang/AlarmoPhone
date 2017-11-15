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

public class AlarmActivity extends Activity {


    public static Ringtone ringtone;
    private static AlarmActivity inst;
    private TimePicker alarmTimePicker;
    private TextView alarmTextView;
    private AlarmReceiver alarmReceiver;

    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmTimePicker = findViewById(R.id.alarmTimePicker);
        alarmTextView =  findViewById(R.id.alarmText);
        ToggleButton alarmToggle = findViewById(R.id.alarmToggle);
        alarmReceiver = new AlarmReceiver();



    }

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.d("MyActivity", "Alarm On");

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Context context = this.getApplicationContext();
            if(alarmReceiver != null){
                alarmReceiver.SetAlarm(context, calendar.getTimeInMillis());
                Toast.makeText(AlarmActivity.this, "Alarm On", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AlarmActivity.this, "Alarm Off", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}