package com.example.alarmophone;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.security.Provider;


/**
 * Created by Phoenix Horizon on 11/12/2017.
 */

public class RingtonePlayingService extends Service{
    MediaPlayer media_song;
    private Ringtone ringtone;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startID)
    {
        Uri ringtoneURI = Uri.parse(intent.getExtras().getString("ringtone-uri"));
        this.ringtone = RingtoneManager.getRingtone(this, ringtoneURI);
        ringtone.play();
        return START_NOT_STICKY;
    }

    public void onDestroy()
    {
        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
        ringtone.stop();
    }
}
