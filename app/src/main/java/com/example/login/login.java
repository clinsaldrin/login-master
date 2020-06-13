package com.example.login;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    View view;
    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private TextView Regup;
    private FirebaseAuth firebaseauth;
    private TextView Forgot;
     int counter = 5;
     private ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darktheme);
        }
        else
            setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Name = (EditText) findViewById(R.id.usname);
        Password = (EditText) findViewById(R.id.uspassword);
        Info = (TextView) findViewById(R.id.tvforgot);
        Login = (Button) findViewById(R.id.btnlogin);
        Regup = (TextView) findViewById(R.id.tvreg);
        Forgot = (TextView) findViewById(R.id.tvforgotpassword);
        progressdialog = new ProgressDialog(this);

        this.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN|
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);



        firebaseauth = FirebaseAuth.getInstance();


        FirebaseUser user = firebaseauth.getCurrentUser();

        if (user != null){
            finish();
            startActivity(new Intent (login.this, test.class));

        }
        Regup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (login.this, register.class));

            }
        });
        Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, forgot.class));
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name= Name.getText().toString();
                final String user_password= Password.getText().toString();

                if (user_name.trim().length() == 0 || user_password.trim().length() == 0) {

                    Toast.makeText(login.this, "Fill all", Toast.LENGTH_SHORT).show();

                }else{

                    progressdialog.setMessage("Please Wait");
                    progressdialog.show();

                    firebaseauth.signInWithEmailAndPassword(user_name, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressdialog.dismiss();
                                FirebaseUser firebaseuser = firebaseauth.getInstance().getCurrentUser();
                                Boolean emailflag = firebaseuser.isEmailVerified();

                                if (emailflag) {
                                    startActivity(new Intent(login.this, test.class));
                                    finish();
                                } else {
                                    progressdialog.dismiss();
                                    Toast.makeText(login.this, "Verify your email", Toast.LENGTH_SHORT).show();
                                    firebaseauth.signOut();
                                }

                            } else {
                                progressdialog.dismiss();
                                counter--;
                                Info.setText("No of attempts remaining: " + String.valueOf(counter));
                                Toast.makeText(login.this, "E-Mail or Password incorrect", Toast.LENGTH_SHORT).show();
                                if (counter == 0) {
                                    Login.setEnabled(false);
                                    Info.setText("login Disabled");
                                }
                            }

                        }
                    });
                }


            }
        });


    }

}
