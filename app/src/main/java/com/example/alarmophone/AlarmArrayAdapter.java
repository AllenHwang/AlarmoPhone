package com.example.alarmophone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by j-jizzle on 3/12/17.
 */

public class AlarmArrayAdapter extends ArrayAdapter<AlarmData> {

    AlarmArrayAdapter(Context context, ArrayList<AlarmData> alarms){
        super(context, 0, alarms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final AlarmData alarmData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_alarm_data, parent, false);
        }
        // Lookup view for data population
        TextView alarmName = (TextView) convertView.findViewById(R.id.AlarmDataName);
        TextView alarmTime = (TextView) convertView.findViewById(R.id.AlarmDataTime);

        // Populate the data into the template view using the data object
        alarmName.setText(alarmData.name);
        alarmTime.setText(alarmData.hour + ":" + alarmData.minute + " " + alarmData.amPm +".");

        final Button deleteAlarm = (Button) convertView.findViewById(R.id.AlarmDataDelete);
        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(alarmData);
            }
        });

        Switch toggleAlarm = (Switch) convertView.findViewById(R.id.AlarmDataToggle);
        toggleAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
