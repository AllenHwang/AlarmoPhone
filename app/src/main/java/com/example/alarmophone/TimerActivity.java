package com.example.alarmophone;


import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class TimerActivity extends AppCompatActivity {
    CountDownTimer cdt;
    int totalTimeLeft = 0;
    boolean pomoState = false;
    boolean pomoBreak = false;
    boolean testSkip = false;
    CircularArray<String> pomoTimes = new CircularArray<String>();

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
        final Button add = (Button) findViewById(R.id.add);
        add.setEnabled(false);
        final Button chain = (Button) findViewById(R.id.chain);
        chain.setEnabled(false);
        chain.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            boolean p = bundle.getBoolean("pomodoro");
            pomodoro.setChecked(p);
            if (pomodoro.isChecked()) {
                // The toggle is enabled
                pomoState = true;
                add.setText("Add Chain");
                chain.setVisibility(View.VISIBLE);
                hours.setText("00");
                minutes.setText("00");
                seconds.setText("00");
                //reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                    /*hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("10");
                    reverseTimer(0,0,10,hours,minutes,seconds,stop,reset,start);*/
                if(cdt != null)
                    cdt.cancel();

                //reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                start.setEnabled(true);
                stop.setEnabled(true);
                reset.setEnabled(true);
                //skip.setEnabled(true);
            } else {
                // The toggle is disabled
                if(cdt != null)
                    cdt.cancel();

                totalTimeLeft = 0;
                hours.setText("00");
                minutes.setText("00");
                seconds.setText("00");

                pomoState = false;
                add.setText("Add Timer");
                chain.setVisibility(View.GONE);
                start.setEnabled(false);
                stop.setEnabled(false);
                reset.setEnabled(false);
                //skip.setEnabled(false);
            }
        }
        String importTimer = bundle.getString("timer");
        if(importTimer != null) {
            //format 12:34:56
            importTimer = importTimer.trim();

            int h = Integer.parseInt(importTimer.substring(0,2));
            int m = Integer.parseInt(importTimer.substring(3,5));
            int s = Integer.parseInt(importTimer.substring(6));
            hours.setText(String.format("%02d", h));
            minutes.setText(String.format("%02d", m));
            seconds.setText(String.format("%02d", s));
            start.setEnabled(true);
            add.setEnabled(false);

            if(cdt != null)
                cdt.cancel();
        }

        String iChain = bundle.getString("chain");
       // String[] importChain = iChain.split("\n");
        if(iChain != null){
            String[] importChain = iChain.split("\n");
            for(int i = 0; i < importChain.length; i++) {
                pomoTimes.addLast(importChain[i]);
            }

            String firstChain = pomoTimes.popFirst();
            pomoTimes.addLast(firstChain);
            firstChain = firstChain.trim();

            int h = Integer.parseInt(firstChain.substring(0,2));
            int m = Integer.parseInt(firstChain.substring(3,5));
            int s = Integer.parseInt(firstChain.substring(6));
            hours.setText(String.format("%02d", h));
            minutes.setText(String.format("%02d", m));
            seconds.setText(String.format("%02d", s));


            start.setEnabled(true);
            add.setEnabled(false);

            if(cdt != null)
                cdt.cancel();
        }


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
                if(totalTimeLeft < 1000) {
                    add.setEnabled(true);
                    chain.setEnabled(true);
                }
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
                if(totalTimeLeft < 1000) {
                    add.setEnabled(true);
                    chain.setEnabled(true);
                }
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
                if(totalTimeLeft < 1000) {
                    add.setEnabled(true);
                    chain.setEnabled(true);
                }
            }
        });

        chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String h = hours.getText().toString();
                if(h.length() == 1) {
                    h = "0" + h;
                }
                String m = minutes.getText().toString();
                if(m.length() == 1) {
                    m = "0" + m;
                }
                String s = seconds.getText().toString();
                if(s.length() == 1) {
                    s = "0" + s;
                }

                String timer = h + ":" + m + ":" + s;
                if(!h.equals("00") || !m.equals("00") || !s.equals("00")) {
                    pomoTimes.addLast(timer);
                }
                hours.setText("00");
                minutes.setText("00");
                seconds.setText("00");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pomoState)
                    timersActivity(view, hours, minutes, seconds, pomodoro);
                else {
                    String h = hours.getText().toString();
                    if(h.length() == 1) {
                        h = "0" + h;
                    }
                    String m = minutes.getText().toString();
                    if(m.length() == 1) {
                        m = "0" + m;
                    }
                    String s = seconds.getText().toString();
                    if(s.length() == 1) {
                        s = "0" + s;
                    }

                    String timer = h + ":" + m + ":" + s;
                    if(!h.equals("00") || !m.equals("00") || !s.equals("00")) {
                        pomoTimes.addLast(timer);
                    }
                    timersActivity2(view, hours, minutes, seconds, pomodoro);
                }
                if(cdt != null)
                    cdt.cancel();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int h = Integer.parseInt(hours.getText().toString());
                int m = Integer.parseInt(minutes.getText().toString());
                int s = Integer.parseInt(seconds.getText().toString());
                reverseTimer(h,m,s,hours,minutes,seconds,stop,reset,start,add,chain);
                start.setEnabled(false);
                stop.setEnabled(true);
                reset.setEnabled(true);
                add.setEnabled(false);
                chain.setEnabled(false);
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cdt != null)
                    cdt.cancel();
                start.setEnabled(true);
                stop.setEnabled(false);
                add.setEnabled(true);
                chain.setEnabled(true);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cdt != null)
                    cdt.cancel();
                totalTimeLeft = 0;
                if(!pomoState) {
                    hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("00");
                    start.setEnabled(false);
                    stop.setEnabled(false);
                    reset.setEnabled(false);
                    add.setEnabled(false);
                    chain.setEnabled(false);
                }
                else {
                   /* pomoBreak = !pomoBreak;
                    totalTimeLeft = 0;
                    if(!pomoBreak) {
                        hours.setText("00");
                        minutes.setText("25");
                        seconds.setText("00");
                        reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                        //hours.setText("00");
                        //minutes.setText("00");
                       // seconds.setText("10");
                       // reverseTimer(0,0,10,hours,minutes,seconds,stop,reset,start);
                        cdt.cancel();
                    }
                    else {
                        hours.setText("00");
                        minutes.setText("05");
                        seconds.setText("00");
                        reverseTimer(0,5,0,hours,minutes,seconds,stop,reset,start);
                        //hours.setText("00");
                       // minutes.setText("00");
                       // seconds.setText("02");
                      //  reverseTimer(0,0,2,hours,minutes,seconds,stop,reset,start);
                        cdt.cancel();
                    }*/
                    totalTimeLeft = 0;
                    if(!pomoTimes.isEmpty()) {
                        String time = pomoTimes.popFirst();
                        pomoTimes.addLast(time);

                        int h = Integer.parseInt(time.substring(0,2));
                        //int h = Integer.parseInt(time);
                        int m = Integer.parseInt(time.substring(3, 5));
                        int s = Integer.parseInt(time.substring(6));
                        hours.setText(String.format("%02d", h));
                        minutes.setText(String.format("%02d", m));
                        seconds.setText(String.format("%02d", s));
                    }
                    else {
                        hours.setText("00");
                        minutes.setText("00");
                        seconds.setText("00");
                    }

                    //if(cdt != null)
                   //     cdt.cancel();
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
                    add.setText("Add Chain");
                    chain.setVisibility(View.VISIBLE);
                    hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("00");
                    //reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                    /*hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("10");
                    reverseTimer(0,0,10,hours,minutes,seconds,stop,reset,start);*/
                    if(cdt != null)
                        cdt.cancel();

                    //reverseTimer(0,25,0,hours,minutes,seconds,stop,reset,start);
                    start.setEnabled(true);
                    stop.setEnabled(true);
                    reset.setEnabled(true);
                    //skip.setEnabled(true);
                } else {
                    // The toggle is disabled
                    if(cdt != null)
                        cdt.cancel();

                    totalTimeLeft = 0;
                    hours.setText("00");
                    minutes.setText("00");
                    seconds.setText("00");

                    pomoState = false;
                    add.setText("Add Timer");
                    chain.setVisibility(View.GONE);
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

    public int reverseTimer(final int hours, final int minutes, final int seconds, final EditText th, final EditText tm, final EditText ts, final Button stop, final Button reset, final Button start, final Button add, final Button chain) {
        if(totalTimeLeft < 1000)
            totalTimeLeft = ((hours * 3600) + (minutes * 60) + seconds) * 1000 + 777;

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
                    start.setEnabled(false);
                    stop.setEnabled(false);
                    reset.setEnabled(false);
                    add.setEnabled(false);
                    chain.setEnabled(false);
                }
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
               /* new CountDownTimer(4000, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        r.stop();
                    }
                }.start();*/
                if(pomoState) {
                    /*pomoBreak = !pomoBreak;
                    //start.setEnabled(false);

                    if(!pomoBreak) {
                        th.setText("00");
                        tm.setText("25");
                        ts.setText("00");
                        reverseTimer(0,25,0,th,tm,ts,stop,reset,start);
                        //th.setText("00");
                        //tm.setText("00");
                        //ts.setText("10");
                        //reverseTimer(0,0,10,th,tm,ts,stop,reset,start);

                    }
                    else {
                        th.setText("00");
                        tm.setText("05");
                        ts.setText("00");
                        reverseTimer(0,5,0,th,tm,ts,stop,reset,start);
                        //th.setText("00");
                       // tm.setText("00");
                      //  ts.setText("00");
                      //  reverseTimer(0,0,2,th,tm,ts,stop,reset,start);

                    }*/
                    if(!pomoTimes.isEmpty()) {
                        String time = pomoTimes.popFirst();
                        pomoTimes.addLast(time);
                        time = time.trim();

                        int h = Integer.parseInt(time.substring(0,2));
                        //int h = Integer.parseInt(time);
                        int m = Integer.parseInt(time.substring(3, 5));
                        int s = Integer.parseInt(time.substring(6));
                        th.setText(String.format("%02d", h));
                        tm.setText(String.format("%02d", m));
                        ts.setText(String.format("%02d", s));
                        reverseTimer(h,m,s,th,tm,ts,stop,reset,start,add,chain);
                    }
                    else {
                        th.setText("00");
                        tm.setText("00");
                        ts.setText("00");
                        reverseTimer(0,0,0,th,tm,ts,stop,reset,start,add,chain);
                    }
                }
            }

        }.start();

        return 1;
    }

    public void timersActivity(View view, final EditText th, final EditText tm, final EditText ts, ToggleButton tb){
        Intent intent = new Intent(this, TimersActivity.class);
        Bundle bundle = new Bundle();
        Bundle cBundle = getIntent().getExtras();
        int size = cBundle.getInt("size");
        size++;
        bundle.putInt("size", size);
        bundle.putBoolean("pomodoro",tb.isChecked());

        String hours = th.getText().toString();
        if(hours.length() == 1) {
            hours = "0" + hours;
            }
        String minutes = tm.getText().toString();
        if(minutes.length() == 1) {
            minutes = "0" + minutes;
            }
        String seconds =  ts.getText().toString();
        if(seconds.length() == 1) {
            seconds = "0" + seconds;
        }
        String timer = hours + ":" + minutes + ":" + seconds;

        bundle.putString("timer", timer);

        if(cdt != null)
            cdt.cancel();

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void timersActivity2(View view, final EditText th, final EditText tm, final EditText ts, ToggleButton tb){
        Intent intent = new Intent(this, TimersActivity.class);
        Bundle bundle = new Bundle();
        Bundle cBundle = getIntent().getExtras();
        int size = cBundle.getInt("size");
        size++;
        bundle.putInt("size", size);
        bundle.putBoolean("pomodoro",tb.isChecked());

        String[] pomos = new String[pomoTimes.size()];
        for(int i = 0; i < pomoTimes.size(); i++){
            pomos[i] = pomoTimes.get(i);
        }

        bundle.putStringArray("chain", pomos);

        if(cdt != null)
            cdt.cancel();

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(cdt != null) {
            cdt.cancel();
            Intent intent = new Intent(this, TimersActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (cdt != null) {
            cdt.cancel();
            Intent intent = new Intent(this, TimersActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onSupportNavigateUp();
        }
        return true;
    }
}
