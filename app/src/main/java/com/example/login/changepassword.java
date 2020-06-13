package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class changepassword extends AppCompatActivity {

    private Button Change;
    private EditText Newpassword;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        Change = findViewById(R.id.btnnewpassword);
        Newpassword = findViewById(R.id.etnewpassword);


        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                String userpasswordnew = Newpassword.getText().toString();

                firebaseUser.updatePassword(userpasswordnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(changepassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(changepassword.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}
