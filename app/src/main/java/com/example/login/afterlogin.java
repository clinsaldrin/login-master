package com.example.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class afterlogin extends AppCompatActivity {

    private Button Logout;
    private TextView Welcome;
    private FirebaseAuth firebaseauth;
    private Button Settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode()== AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darktheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterlogin);

        Logout = (Button) findViewById(R.id.btnlogout);
        Welcome = (TextView)findViewById(R.id.tvwelcome);
        Settings = (Button) findViewById(R.id.btnsettings);

        firebaseauth = FirebaseAuth.getInstance();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseauth.signOut();
                finish();
                Intent intent = new Intent(afterlogin.this, login.class);
                startActivity(intent);

            }


        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(afterlogin.this, test.class));
                finish();
            }
        });


    }
}
