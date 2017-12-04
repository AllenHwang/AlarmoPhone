package com.example.alarmophone;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TimersActivity extends AppCompatActivity {
    static ArrayList<String> timers = new ArrayList<String>();
    static ArrayList<String> chains = new ArrayList<String>();
    static int timersSize = 0;
    static int chainsSize = 0;
    final static String timerFile = "timersFile";
    final static String chainFile = "chainsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers);


        final Button add = (Button) findViewById(R.id.button5);
        final Button clear = (Button) findViewById(R.id.button6);
        final ToggleButton pomodoro = (ToggleButton) findViewById(R.id.pomodoro);
        final ListView timersView = (ListView) findViewById(R.id.listView2);
        timersView.setVisibility(View.VISIBLE);
        final ListView chainsView = (ListView) findViewById(R.id.listView3);
        chainsView.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();

        String[] chain = null;
        if(bundle != null) {
            chain = bundle.getStringArray("chain");
            boolean p = bundle.getBoolean("pomodoro");
            pomodoro.setChecked(p);
            if (pomodoro.isChecked()) {
                timersView.setVisibility(View.GONE);
                chainsView.setVisibility(View.VISIBLE);
                add.setText("Add Chain");
            }
            else {
                timersView.setVisibility(View.VISIBLE);
                chainsView.setVisibility(View.GONE);
                add.setText("Add Timer");
            }
        }
        if(bundle != null) {
            int size = bundle.getInt("size");
            String timer = bundle.getString("timer");
            if(timer != null) {
                timers.add(timer);
                timersSize++;

                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(timerFile, Context.MODE_APPEND);
                    String fs = timer + ",";
                    fos.write(fs.getBytes());
                    fos.close();
                    //String[] test = {"test"};
                    // ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, timersArray);
                    //  timersView.setAdapter(adapter);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(timersSize >= 0) {

            FileInputStream fis = null;
            try {
                fis = openFileInput(timerFile);

                int r = 0;
                String temp="";
                while( (r = fis.read()) != -1){
                    temp = temp + Character.toString((char)r);
                }
                fis.close();
                String[] timersArray = temp.split(",");
                if(timersSize == 0) {
                    for (String s : timersArray)
                        timers.add(s);
                }
                timersSize = timersArray.length;

                String[] timersArray2 = new String[timers.size()];
                for(int i = 0; i < timers.size(); i++) {
                    String s = "Timer " + (i+1) + " - " + "\t" + timers.get(i);
                    timersArray2[i]= s;
                }

                //String[] test = {"test"};
                if(timersArray2 != null) {
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, timersArray2);
                    timersView.setAdapter(adapter);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(chain != null) {
            String chainS ="";
            for(int i = 0; i < chain.length; i++) {
                if(i != 0)
                    chainS += "\t\t\t\t\t\t\t\t";
                chainS += "\t" + chain[i] + "\n";
            }
           // chainS += ",";
            chains.add(chainS);
            chainsSize++;

            FileOutputStream fos = null;
            try {
                fos = openFileOutput(chainFile, Context.MODE_APPEND);
                String fs = chainS +",";
                fos.write(fs.getBytes());
                fos.close();
                //String[] test = {"test"};
                // ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, timersArray);
                //  timersView.setAdapter(adapter);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(chainsSize >= 0) {
            FileInputStream fis = null;
            try {
                fis = openFileInput(chainFile);

                int r = 0;
                String temp="";
                while( (r = fis.read()) != -1){
                    temp = temp + Character.toString((char)r);
                }
                fis.close();
                String[] chainsArray = temp.split(",");
                if(chainsSize == 0) {
                    for (String s : chainsArray)
                        chains.add(s);
                }
                chainsSize = chainsArray.length;

                String[] chainsArray2 = new String[chains.size()];
                for(int i = 0; i < chains.size(); i++) {
                    String s = "Chain " + (i+1) + " - " + chains.get(i);
                   chainsArray2[i]= s;
                }

                //String[] test = {"test"};
                if(chainsArray2 != null) {
                    ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chainsArray2);
                    chainsView.setAdapter(adapter);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        pomodoro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timersView.setVisibility(View.GONE);
                    chainsView.setVisibility(View.VISIBLE);
                    add.setText("Add Chain");
                }
                else {
                    timersView.setVisibility(View.VISIBLE);
                    chainsView.setVisibility(View.GONE);
                    add.setText("Add Timer");
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerActivity(view, pomodoro);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timers.clear();
                chains.clear();
                timersSize = 0;
                chainsSize = 0;
                timersView.setAdapter(null);
                chainsView.setAdapter(null);
                deleteFile(timerFile);
                deleteFile(chainFile);
            }
        });

        timersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timerActivity2(view,i, pomodoro);
            }
        });

        chainsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timerActivity3(view,i, pomodoro);
            }
        });

    }
    public void timerActivity(View view, ToggleButton tb){
        Intent intent = new Intent(this, TimerActivity.class);
        if(timersSize > 0) {
            Bundle bundle = new Bundle();
           // bundle.putString("timer", timers.get(0));
            bundle.putInt("size", ++timersSize);
            bundle.putBoolean("pomodoro", tb.isChecked());
            intent.putExtras(bundle);
        }
        else {
            Bundle bundle = new Bundle();
          //  bundle.putString("timer", null);
            bundle.putInt("size", ++timersSize);
            bundle.putBoolean("pomodoro", tb.isChecked());
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void timerActivity2(View view, int position, ToggleButton tb){
        Intent intent = new Intent(this, TimerActivity.class);
        if(timersSize > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("timer", timers.get(position));
            bundle.putBoolean("pomodoro", tb.isChecked());
            //bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString("timer", null);
            bundle.putBoolean("pomodoro", tb.isChecked());
            //bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void timerActivity3(View view, int position, ToggleButton tb){
        Intent intent = new Intent(this, TimerActivity.class);
        if(timersSize > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("chain", chains.get(position));
            bundle.putBoolean("pomodoro", tb.isChecked());
            //bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString("chain", null);
            bundle.putBoolean("pomodoro", tb.isChecked());
            //bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
