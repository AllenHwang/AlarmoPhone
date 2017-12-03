package com.example.alarmophone;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.alarmophone.NotificationPublisher.NOTIFICATION;
import static com.example.alarmophone.NotificationPublisher.NOTIFICATION_ID;

public class AlarmOverview extends AppCompatActivity {

    public static AlarmArrayAdapter arrayAdapter;
    public static ArrayList<AlarmData> alarms = new ArrayList<AlarmData>();
    private ListView listView;
    private Button addAlarm;
    private AlarmManager alarmManager;
    private Context context;
    private AlarmReceiver alarmReceiver;
    private String CHANNEL_ID = "Alarm_Channel";

    public static Map<Integer, Ringtone> ringtoneMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_overview);
        context = this.getApplicationContext();

        arrayAdapter = new AlarmArrayAdapter(context, alarms, this);
        listView = (ListView) findViewById(R.id.listViewAlarms);
        listView.setAdapter(arrayAdapter);
        addAlarm = (Button) findViewById(R.id.buttonAddAlarm);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmReceiver = new AlarmReceiver();
        ringtoneMap = new HashMap<Integer, Ringtone>();
    }

    /**
     * Called when the Add Alarm button is pressed
     */
    public void newAlarm(View view) {
        Intent intent = new Intent(this, AlarmCreator.class);
        startActivity(intent);
    }

    public void createAlarm(AlarmData data) {
        Log.d("MyActivity", "Alarm On");
        int hour = Integer.parseInt(data.hour);
        String amPm = data.amPm;
        if (hour == 12) {
            if (amPm.equals("am")) {
                hour = 0;
            }
        } else if (amPm.equals("pm")) {
            hour += 12;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, Integer.parseInt(data.minute));
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("AlarmoPhone")
                .setContentText("Alarm: " + data.name)
                .setSmallIcon(R.drawable.ic_launcher_background);
        Notification notification = builder.build();
        intent.putExtra(NOTIFICATION_ID, 1);
        intent.putExtra(NOTIFICATION, notification);
        intent.putExtra("ALARM_ID", data.id);

        PendingIntent pi = PendingIntent.getBroadcast(context, data.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }
        else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtoneMap.put(data.id, RingtoneManager.getRingtone(context, alarmUri));

        Toast.makeText(context, "Alarm " + data.name + "On", Toast.LENGTH_SHORT).show();


    }

    public void clearAlarm(AlarmData data) {
        Log.d("MyActivity", "Alarm Off");
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, data.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
        Ringtone ring = ringtoneMap.get(data.id);
        if(ring.isPlaying()){
            ring.stop();
        }
        Toast.makeText(context, "Alarm " + data.name + "Off", Toast.LENGTH_SHORT).show();
    }
}

