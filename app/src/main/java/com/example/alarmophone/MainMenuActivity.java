package com.example.alarmophone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity {


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main Menu");
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main_menu);
    }
    /**Called when the user presses the Alarms button */
    public void alarmsActivity(View view){
        Intent intent = new Intent(this, AlarmsActivity.class);
        startActivity(intent);

    }
    public void timersActivity(View view){
        Intent intent = new Intent(this, TimersActivity.class);
        startActivity(intent);
    }
    public void logoutActivity(View view){
        mAuth.signOut();
        finish();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
