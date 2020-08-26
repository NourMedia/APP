package com.raismedia.taxiclient.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.raismedia.taxiclient.settings.AppConst;

//import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    /**
     * The Application's current Instance ID token is no longer valid
     * and thus a new one must be requested.
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + token);
        if(token.length()>0)
            AppConst.fcm_id=token;

        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance()
                .subscribeToTopic(FRIENDLY_ENGAGE_TOPIC);
    }

}