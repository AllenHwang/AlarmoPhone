package com.example.alarmophone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class TimersActivity extends AppCompatActivity {
    static ArrayList<String> timers = new ArrayList<String>();
    static int timersSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timers);

        final Button add = (Button) findViewById(R.id.button5);
        final ListView timersView = (ListView) findViewById(R.id.listView2);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int size = bundle.getInt("size");
            String timer = bundle.getString("timer");
            timers.add(timer);
            timersSize++;
        }

        if(timersSize > 0) {
            String[] timersArray = new String[timers.size()];
            for(int i = 0; i < timers.size(); i++) {
                String s = "Timer " + (i+1) + " - " + timers.get(i);
                timersArray[i]= s;
            }
            //String[] test = {"test"};
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, timersArray);
            timersView.setAdapter(adapter);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerActivity(view);
            }
        });

        timersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timerActivity2(view,i);
            }
        });

    }
    public void timerActivity(View view){
        Intent intent = new Intent(this, TimerActivity.class);
        if(timersSize > 0) {
            Bundle bundle = new Bundle();
           // bundle.putString("timer", timers.get(0));
            bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        else {
            Bundle bundle = new Bundle();
          //  bundle.putString("timer", null);
            bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void timerActivity2(View view, int position){
        Intent intent = new Intent(this, TimerActivity.class);
        if(timersSize > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("timer", timers.get(position));
            //bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString("timer", null);
            //bundle.putInt("size", ++timersSize);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
