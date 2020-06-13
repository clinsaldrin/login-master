package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot extends AppCompatActivity {

    private EditText PEmail;
    private Button Reset;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot);

        PEmail = (EditText)findViewById(R.id.etpemail);
        Reset = (Button)findViewById(R.id.btnrepassword);
        firebaseauth = FirebaseAuth.getInstance();

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = PEmail.getText().toString().trim();

                if (useremail == ""){
                    Toast.makeText(forgot.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseauth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(forgot.this, "Reset Email send", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgot.this, login.class));
                            }
                            else{
                                Toast.makeText(forgot.this, "Error please try again later", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });


    }
}
