package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {

    private NavigationView navigationview;
    private FirebaseAuth firebaseauth;
    private ListView drawerlist;
    private Switch darklight;
    private TextView Profilename, Profileemail, ProfileDOB, Profilesex, Profilenumber, Edit, Changepassword, ProfileDocname, ProfileDocnum;
    private FirebaseDatabase firebasedatabase;
    private TextView headername;
    private FirebaseStorage firebaseStorage;
    private ImageView Propic;
    private ImageView HeaderPropic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
    {
        setTheme(R.style.darktheme);
    }
    else
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        navigationview = findViewById(R.id.navigation);
        Profilename = findViewById(R.id.tvuname);
        Profileemail = findViewById(R.id.tvemail);
        ProfileDOB = findViewById(R.id.tvdate);
        Profilesex = findViewById(R.id.tvgender);
        Profilenumber = findViewById(R.id.tvnumber);
        Edit = findViewById(R.id.tvedit);
        Changepassword = findViewById(R.id.tvpassword);
        View headerview = navigationview.getHeaderView(0);
        headername = headerview.findViewById(R.id.tvheadername);
        HeaderPropic = headerview.findViewById(R.id.defaultpropic);
        ProfileDocname = findViewById(R.id.tvdocname);
        ProfileDocnum = findViewById(R.id.tvdocnum);
        Propic = findViewById(R.id.impropic);


        firebaseauth = FirebaseAuth.getInstance();
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseauth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Propic);
                Picasso.get().load(uri).into(HeaderPropic);
            }
        });

        DatabaseReference databasereference = firebasedatabase.getReference(firebaseauth.getUid());
        databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                Profilename.setText("Name: " + userProfile.getUsername());
                Profileemail.setText("E-Mail: " + userProfile.getUseremail());
                ProfileDOB.setText("Date of Birth: " + userProfile.getUserdob());
                Profilesex.setText("Gender: " + userProfile.getUsergender());
                Profilenumber.setText("Phone: " + userProfile.getUsernumber());
                ProfileDocname.setText("Doctor Name: " + userProfile.getUserdocname());
                ProfileDocnum.setText("Doctor's UPRN: " + userProfile.getUserdocnum());
                headername.setText(userProfile.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, editprofile.class));
            }
        });

        Changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profile.this, changepassword.class));

            }
        });


        Menu menu = navigationview.getMenu();
        MenuItem menuItem = menu.findItem(R.id.switch_item);
        View action = MenuItemCompat.getActionView(menuItem);
        darklight = action.findViewById(R.id.app_bar_switch);

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(profile.this, login.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.logout:
                        firebaseauth.signOut();
                        finish();
                        startActivity(new Intent(profile.this, login.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.health:
                        startActivity(new Intent(profile.this, health.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;

                }

                return false;
            }
        });

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
        {
            darklight.setChecked(true);
        }

        darklight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(new Intent(getApplicationContext(), profile.class));
                    finish();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(getApplicationContext(), profile.class));
                    finish();
                }
            }
        });
    }
}
