package com.example.alarmophone;


import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {
    CountDownTimer cdt;
    int totalTimeLeft = 0;
    boolean pomoState = false;
    boolean pomoBreak = false;
    boolean testSkip = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final EditText hours = (EditText) findViewById(R.id.hours);
        final EditText minutes = (EditText) findViewById(R.id.minutes);
        final EditText seconds = (EditText) findViewById(R.id.seconds);

        final Button start = (Button) findViewById(R.id.start);
        start.setEnabled(false);
        final Button stop = (Button) findViewById(R.id.stop);
        stop.setEnabled(false);
        final Button reset = (Button) findViewById(R.id.reset);
        reset.setEnabled(false);
        final ToggleButton pomodoro = (ToggleButton) findViewById(R.id.pomodoro);
        //final Button skip = (Button) findViewById(R.id.skip);
        //skip.setEnabled(false);


        hours.addTextChangedListener(new TextWatcher() {
            boolean ignore;
            boolean clear;
            // us;

            public void afterTextChanged(Editable s) {

                if(ignore)
                    return;
                if(clear) {
                    ignore = true;
                    hours.setText("00");
                    ignore = false;
                    clear = false;
                    hours.requestFocus();
                    return;
                }

                int val = 0;
                int length = s.length();
                if(length > 0)
                    val = Integer.parseInt(s.toString());
                /*if(length == 1){
                    String newNum = "0"+Integer.toString(val);
                    ignore = true;
                    hours.setText(newNum);
                    ignore = false;
                }*/
                if(length == 2 && val > 23) {
                    ignore = true;
                    hours.setText("23");
                    ignore = false;
                }
                if(val > 0 && totalTimeLeft < 1000 && !pomoState)
                    start.setEnabled(true);

                if(length == 2)
                    minutes.requestFocus();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.charAt(0) == '0' && s.length() == 1 && !ignore)
                    clear = true;
            }
        });
        minutes.addTextChangedListener(new TextWatcher() {
            boolean ignore;
            boolean clear;

            public void afterTextChanged(Editable s) {

                if(ignore)
                    return;
                if(clear) {
                    ignore = true;
                    minutes.setText("00");
                    ignore = false;
                    clear = false;
                    minutes.requestFocus();
                    return;
                }

                int val = 0;
                int length = s.length();
                if(length > 0)
                    val = Integer.parseInt(s.toString());
                /*if(length == 1){
                    String newNum = "0"+Integer.toString(val);
                    ignore = true;
                    minutes.setText(newNum);
                    ignore = false;
                }*/
                if(length == 2 && val > 59) {
                    ignore = true;
                    minutes.setText("59");
                    ignore = false;
                }
                if(val > 0 && totalTimeLeft < 1000 && !pomoState)
                    start.setEnabled(true);
                if(length == 2)
                    seconds.requestFocus();
               /* if(length == 3 && val > 9) {
                    String temp = s.toString();
                    ignore = true;
                    s.replace(1,2,"0",0,1);
                    s.replace(0,1,temp.toString(), 1,2 );
                    ignore = false;
                    s.delete(2, 3);
                }*/
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.charAt(0) == '0' && s.length() == 1 && !ignore)
                    clear = true;
            }
        });
        seconds.addTextChangedListener(new TextWatcher() {
            boolean ignore;
            boolean clear;

            public void afterTextChanged(Editable s) {
                if(ignore)
                    return;
                if(clear) {
                    ignore = true;
                    seconds.setText("00");
                    ignore = false;
                    clear = false;
                    seconds.requestFocus();
                    return;
                }

                int val = 0;
                int length = s.length();
                if(length > 0)
                    val = Integer.parseInt(s.toString());
                /*if(length == 1){
                    String newNum = "0"+Integer.toString(val);
                    ignore = true;
                    seconds.setText(newNum);
                    ignore = false;
                }*/
                if(length == 2 && val > 59) {
                    ignore = true;
                    seconds.setText("59");
                    ignore = false;
                }
                if(val > 0 && totalTimeLeft < 1000 && !pomoState)
                    start.setEnabled(true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.charAt(0) == '0' && s.length() == 1 && !ignore)
                    clear = true;
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int h = Integer.parseInt(hours.getText().toString());
                int m = Integer.parseInt(minutes.getText().toString());
                int s = Integer.parseInt(seconds.getText().toString());
                reverseTimer(h,m,s,hours,minutes,seconds,stop,reset,start);
                start.setEnabled(false);
                stop.setEnabled(true);
                reset.setEnabled(true);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                start.setEnabled(true);
                stop.setEnabled(false);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdt.cancel();
                totalTimeLeft = 0;
                if(!pomoState) {
                    hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("00");
                    start.setEnabled(false);
                    stop.setEnabled(false);
                    reset.setEnabled(false);
                }
                else {
                    pomoBreak = !pomoBreak;
                    totalTimeLeft = 0;
                    if(!pomoBreak) {
                        hours.setText("00");
                        minutes.setText("25");
                        seconds.setText("00");
                        reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                        /*hours.setText("00");
                        minutes.setText("00");
                        seconds.setText("10");
                        reverseTimer(0,0,10,hours,minutes,seconds,stop,reset,start);*/
                        cdt.cancel();
                    }
                    else {
                        hours.setText("00");
                        minutes.setText("05");
                        seconds.setText("00");
                        reverseTimer(0,5,0,hours,minutes,seconds,stop,reset,start);
                        /*hours.setText("00");
                        minutes.setText("00");
                        seconds.setText("02");
                        reverseTimer(0,0,2,hours,minutes,seconds,stop,reset,start);*/
                        cdt.cancel();
                    }
                    start.setEnabled(true);
                    stop.setEnabled(false);
                }
            }
        });
        pomodoro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    pomoState = true;
                    hours.setText("00");
                    minutes.setText("25");
                    seconds.setText("00");
                    reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                    /*hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("10");
                    reverseTimer(0,0,10,hours,minutes,seconds,stop,reset,start);*/
                    cdt.cancel();

                    //reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                    start.setEnabled(true);
                    stop.setEnabled(true);
                    reset.setEnabled(true);
                    //skip.setEnabled(true);
                } else {
                    // The toggle is disabled
                    totalTimeLeft = 0;
                    hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("00");

                    pomoState = false;
                    start.setEnabled(false);
                    stop.setEnabled(false);
                    reset.setEnabled(false);
                    //skip.setEnabled(false);
                }
            }
        });
       /* skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testSkip = true;
            }
        }); */
    }

    public int reverseTimer(final int hours, final int minutes, final int seconds, final EditText th, final EditText tm, final EditText ts, final Button stop, final Button reset, final Button start) {
        if(totalTimeLeft < 1000)
            totalTimeLeft = ((hours * 3600) + (minutes * 60) + seconds) * 1000 + 1000;

        cdt = new CountDownTimer(totalTimeLeft, 100) {

            public void onTick(long millisUntilFinished) {
                if(totalTimeLeft % 100 < 100) {
                    int secondsLeft = (int) millisUntilFinished;
                    totalTimeLeft = secondsLeft;
                    int h = (secondsLeft / 3600 / 1000);
                    secondsLeft = secondsLeft - (h * 3600 * 1000);
                    int m = (secondsLeft / 60 / 1000);
                    secondsLeft = secondsLeft - (m * 60 * 1000);
                    int s = (secondsLeft / 1000);
                    th.setText(String.format("%02d", h));
                    tm.setText(String.format("%02d", m));
                    ts.setText(String.format("%02d", s));
                }
            }

            public void onFinish() {
                if(!pomoState) {
                    stop.setEnabled(false);
                    reset.setEnabled(false);
                }
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                if(pomoState) {
                    pomoBreak = !pomoBreak;
                    //start.setEnabled(false);

                    if(!pomoBreak) {
                        th.setText("00");
                        tm.setText("25");
                        ts.setText("00");
                        reverseTimer(0,25,0,th,tm,ts,stop,reset,start);
                        /*th.setText("00");
                        tm.setText("00");
                        ts.setText("10");
                        reverseTimer(0,0,10,th,tm,ts,stop,reset,start);*/

                    }
                    else {
                        th.setText("00");
                        tm.setText("05");
                        ts.setText("00");
                        reverseTimer(0,5,0,th,tm,ts,stop,reset,start);
                        /*th.setText("00");
                        tm.setText("00");
                        ts.setText("00");
                        reverseTimer(0,0,2,th,tm,ts,stop,reset,start);*/

                    }
                }
            }

        }.start();

        return 1;
    }
}