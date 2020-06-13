package com.example.login;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;

public class register extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private EditText Email;
    private EditText Number;
    private Button Register;
    private TextView Signin;
    private FirebaseAuth firebaseauth;
    private ProgressDialog progressdialog;
    private TextView DOB;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private static final String TAG = "register";
    private Spinner sex;
    String name, email, date, password, gender, number, doctorname, doctornum;
    private ImageView defaultpic;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 123;
    private StorageReference storageReference;
    Uri imagepath;
    Bitmap bitmap;
    private EditText Doctorname;
    private EditText Doctornum;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null ){
            imagepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                defaultpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Name = (EditText) findViewById(R.id.etname);
        Password = (EditText) findViewById(R.id.etpassword);
        Email = (EditText) findViewById(R.id.etemail);
        Register = (Button) findViewById(R.id.btnreg);
        Number = (EditText) findViewById(R.id.etnumber);
        Signin = (TextView) findViewById(R.id.tvregin);
        Doctorname = (EditText) findViewById(R.id.etdoctorname);
        Doctornum = (EditText) findViewById(R.id.etdoctornum);

        firebaseauth = FirebaseAuth.getInstance();
        firebaseStorage =FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference();
        progressdialog = new ProgressDialog(this);
        DOB = (TextView) findViewById(R.id.tvdob);
        sex = (Spinner)findViewById(R.id.spin1);
        defaultpic = findViewById(R.id.defaultpropic4);

        defaultpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select"), PICK_IMAGE);

            }
        });





        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                register.this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.sex));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(register.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth ,onDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy" + dayOfMonth + "/" + month + "/" + year);

                date = dayOfMonth + "/" + month + "/" + year;
                DOB.setText(date);
            }
        };

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean result = false;

                name = Name.getText().toString();
                password = Password.getText().toString();
                email = Email.getText().toString();
                date = DOB.getText().toString();
                gender = sex.getSelectedItem().toString();
                number = Number.getText().toString();
                doctorname = Doctorname.getText().toString();
                doctornum = Doctornum.getText().toString();


                if (((String) name).isEmpty() || ((String) password).isEmpty() || ((String) email).isEmpty() || ((String) number).isEmpty() || imagepath == null  || ((String) doctorname).isEmpty()  || ((String) doctornum).isEmpty()  )  {

                    progressdialog.dismiss();

                    Toast.makeText(register.this, "fill all", Toast.LENGTH_SHORT).show();

                }
                else {
                    result = true;
                }
                if (result == true) {

                    String user_email = Email.getText().toString().trim();
                    String user_password = Password.getText().toString().trim();
                    progressdialog.setMessage("Sending E-Mail verification");
                    progressdialog.show();


                    firebaseauth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                FirebaseUser firebaseuser = firebaseauth.getInstance().getCurrentUser();
                                if (firebaseuser != null){
                                    firebaseuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){

                                                FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
                                                DatabaseReference databasereference = firebasedatabase.getReference(firebaseauth.getUid());
                                                StorageReference imagereference = storageReference.child(firebaseauth.getUid()).child("Images").child("Profile Pic");
                                                UploadTask uploadTask = imagereference.putFile(imagepath);
                                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(register.this, "Failed", Toast.LENGTH_SHORT).show();

                                                    }
                                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        Toast.makeText(register.this, "Success", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                                UserProfile userprofile = new UserProfile(name, email, date, gender, number, doctorname, doctornum);
                                                databasereference.setValue(userprofile);
                                                progressdialog.dismiss();
                                                Toast.makeText(register.this,"Succefully registered, please verify", Toast.LENGTH_SHORT).show();
                                                firebaseauth.signOut();
                                                startActivity(new Intent(register.this, login.class));
                                                finish();
                                            }
                                            else {
                                                progressdialog.dismiss();
                                                Toast.makeText(register.this,"Email hasn't been sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                            else{
                                progressdialog.dismiss();
                                Toast.makeText(register.this, "Failed", Toast.LENGTH_SHORT).show();

                            }



                        }
                    });

                }
            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, login.class));
                finish();

            }
        });

    }

}
