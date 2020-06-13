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

public class health extends AppCompatActivity {

    private NavigationView navigationview;
    private FirebaseAuth firebaseauth;
    private ListView drawerlist;
    private Switch darklight;
    private FirebaseDatabase firebasedatabase;
    private TextView headername;
    private FirebaseStorage firebaseStorage;
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
        setContentView(R.layout.health);

        navigationview = findViewById(R.id.navigation);
        View headerview = navigationview.getHeaderView(0);
        headername = headerview.findViewById(R.id.tvheadername);
        HeaderPropic = headerview.findViewById(R.id.defaultpropic);

        Menu menu = navigationview.getMenu();
        MenuItem menuItem = menu.findItem(R.id.switch_item);
        View action = MenuItemCompat.getActionView(menuItem);
        darklight = action.findViewById(R.id.app_bar_switch);

        firebaseauth = FirebaseAuth.getInstance();
        firebasedatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(firebaseauth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(HeaderPropic);
            }
        });
        DatabaseReference databasereference = firebasedatabase.getReference(firebaseauth.getUid());
        databasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                headername.setText(userProfile.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(health.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });


        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(health.this, test.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.logout:
                        firebaseauth.signOut();
                        finish();
                        startActivity(new Intent(health.this, login.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.health:
                        startActivity(new Intent(getApplicationContext(), health.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(health.this, profile.class));
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
                    startActivity(new Intent(getApplicationContext(), health.class));
                    finish();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(getApplicationContext(), health.class));
                    finish();
                }
            }
        });
    }
}
