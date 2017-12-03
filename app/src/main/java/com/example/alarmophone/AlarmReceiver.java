package com.example.alarmophone;

/**
 * Created by Phoenix Horizon on 10/31/2017.
 */

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.alarmophone.NotificationPublisher.NOTIFICATION;
import static com.example.alarmophone.NotificationPublisher.NOTIFICATION_ID;

public class AlarmReceiver extends BroadcastReceiver {

    private String CHANNEL_ID = "Alarm_Channel";

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Wake up");
        wl.acquire();

        StringBuilder msgStr = new StringBuilder();
        Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));
        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Notification", importance);
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(mChannel);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);

        int alarmId = intent.getIntExtra("ALARM_ID", 0);
        AlarmOverview.ringtoneMap.get(alarmId).play();
    }



}