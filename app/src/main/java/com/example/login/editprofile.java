package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

public class editprofile extends AppCompatActivity {

    private FirebaseDatabase firebasedatabase;
    private FirebaseAuth firebaseauth;
    private EditText Newname;
    private TextView Newdate, Email, Gender, Number;
    private Button Change;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private String name, date, gender, email, number, datefun,  doctorname, doctornum;
    private static final String TAG = "editprofile";
    private EditText NewDoctorname;
    private EditText NewDoctornum;
    private ImageView Updatepropic;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 123;
    private StorageReference storageReference;
    Uri imagepath;
    Bitmap bitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && data.getData() != null ){
            imagepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                Updatepropic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.darktheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);

        Newname = findViewById(R.id.etuname2);
        Newdate =findViewById(R.id.tvdate2);
        Email = findViewById(R.id.tvemail2);
        Gender = findViewById(R.id.tvgender2);
        Number = findViewById(R.id.tvnumber2);
        Change = findViewById(R.id.btnchange);
        NewDoctorname = (EditText) findViewById(R.id.etdocname);
        NewDoctornum = (EditText) findViewById(R.id.etdocnum);
        Updatepropic = findViewById(R.id.defaultpropic3);
        firebaseauth = FirebaseAuth.getInstance();
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseauth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Updatepropic);
            }
        });

        Updatepropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select"), PICK_IMAGE);

            }
        });




        final DatabaseReference databasereference = firebasedatabase.getReference(firebaseauth.getUid());
        databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                Newname.setText(userProfile.getUsername());
                Newdate.setText(userProfile.getUserdob());
                Email.setText(userProfile.getUseremail());
                Gender.setText(userProfile.getUsergender());
                Number.setText(userProfile.getUsernumber());
                NewDoctorname.setText(userProfile.getUserdocname());
                NewDoctornum.setText(userProfile.getUsernumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(editprofile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        Newdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(editprofile.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth ,onDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy" + dayOfMonth + "/" + month + "/" + year);

                datefun = dayOfMonth + "/" + month + "/" + year;
                Newdate.setText(datefun);
            }
        };


        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = Newname.getText().toString();
                email = Email.getText().toString();
                date = Newdate.getText().toString();
                gender = Gender.getText().toString();
                number = Number.getText().toString();
                doctorname = NewDoctorname.getText().toString();
                doctornum = NewDoctornum.getText().toString();

                UserProfile userprofile = new UserProfile(name, email, date, gender, number, doctorname, doctornum);
                databasereference.setValue(userprofile);
                StorageReference imagereference = storageReference.child(firebaseauth.getUid()).child("Images").child("Profile Pic");
                UploadTask uploadTask = imagereference.putFile(imagepath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editprofile.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(editprofile.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                });

                finish();
            }
        });
    }
}
