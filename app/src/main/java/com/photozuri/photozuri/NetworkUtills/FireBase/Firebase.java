package com.photozuri.photozuri.NetworkUtills.FireBase;

import android.util.Log;

import com.photozuri.photozuri.Data.PrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Eric on 3/27/2018.
 */


public class Firebase extends FirebaseInstanceIdService {

    private String TAG="taggs";
    public Firebase() {
        super();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        PrefManager prefManager=new PrefManager(getApplicationContext());
        prefManager.setKeyUserFirebasetoken(refreshedToken);
    }
}
