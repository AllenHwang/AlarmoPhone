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
import java.util.List;
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

    public static Map<Integer, ArrayList<Ringtone>> ringtoneMap;

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
        ringtoneMap = new HashMap<Integer, ArrayList<Ringtone>>();
    }

    /**
     * Called when the Add Alarm button is pressed
     */
    public void newAlarm(View view) {
        Intent intent = new Intent(this, AlarmCreator.class);
        startActivity(intent);
    }

    public void createAlarm(AlarmData data) {
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

        ArrayList<Ringtone> ringtones = new ArrayList<>();
        for(int i = 0; i < data.cascadeAmount; i++) {
            Calendar clone = (Calendar) calendar.clone();
            clone.add(Calendar.MINUTE, i * data.intervalMinute);
            Log.d("CascadedAlarms", "Alarm #" + new Integer(i).toString());

            intent.putExtra("ALARM_ID",data.hashCode());
            intent.putExtra("ALARM_COUNT", i);
            PendingIntent pi = PendingIntent.getBroadcast(context, (i + 1) * data.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, clone.getTimeInMillis(), pi);
            }
            else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, clone.getTimeInMillis(), pi);
            }

            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtones.add(RingtoneManager.getRingtone(context, alarmUri));
        }
        ringtoneMap.put(data.hashCode(), ringtones);

        Toast.makeText(context, "Alarm " + data.name + "On", Toast.LENGTH_SHORT).show();
    }

    public void clearAlarm(AlarmData data) {
        for (int i = 0; i < data.cascadeAmount; i++) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, (i + 1) * data.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pi);
            Log.d("Alarm Off", new Integer(i).toString());
        }
        if(ringtoneMap.containsKey(data.hashCode())) {
            for (Ringtone ring : ringtoneMap.get(data.hashCode())) {
                if (ring.isPlaying()) {
                    ring.stop();
                }
            }
        }
        Toast.makeText(context, "Alarm " + data.name + "Off", Toast.LENGTH_SHORT).show();
    }
}

