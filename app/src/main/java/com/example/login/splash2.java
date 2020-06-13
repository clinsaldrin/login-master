package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class splash2 extends AppCompatActivity {
    private static int splashtime = 2000;
    View view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash2);

        this.getWindow().getDecorView()
        .setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        //view = this.getWindow().getDecorView();
        //view.setBackgroundResource(R.color.DarkGrey);



         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeintent = new Intent(splash2.this, login.class);
                startActivity(homeintent);
                finish();
            }
        },splashtime);
    }
}
