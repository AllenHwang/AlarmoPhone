package com.example.alarmophone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.alarmophone.LoginActivity;
import com.example.alarmophone.R;

public class SignInActivity extends AppCompatActivity {

    //VIEWS AND WIDGET FIELDS
    Button createUser, moveToLogin;
    EditText userEmailEdit, userPassWordEdit;


    //FIREBASE AUTHENTICATION FIELDS
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //ASSIGN ID
        createUser = (Button) findViewById(R.id.createUserBtn);
        moveToLogin = (Button) findViewById(R.id.moveToLogin);
        userEmailEdit = (EditText) findViewById(R.id.emaiEditTextCreate);
        userPassWordEdit = (EditText) findViewById(R.id.passwordEditTextCreate);

        //ASSIGN INSTANCES
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {

                    startActivity(new Intent(SignInActivity.this, MainMenuActivity.class));

                } else
                {

                }

            }
        };


        //ON CLICK LISTENERS

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmailString, userPassString;

                userEmailString = userEmailEdit.getText().toString().trim();
                userPassString = userPassWordEdit.getText().toString().trim();

                if(!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPassString))
                {

                    mAuth.createUserWithEmailAndPassword(userEmailString, userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                Toast.makeText(SignInActivity.this, "User Account Created", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignInActivity.this, MainMenuActivity.class));

                            }
                            else
                            {

                                Toast.makeText(SignInActivity.this, "Failed to create User Account", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }


            }
        });

        //MOVE TO LOGIN
        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignInActivity.this, LoginActivity.class));


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
