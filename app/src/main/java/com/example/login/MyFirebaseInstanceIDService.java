package com.example.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;


import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 04/02/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onNewToken(@NonNull String s)
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken){}
}