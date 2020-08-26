package com.raismedia.taxiclient.firebase;

/**
 * Created by RaisMedia 03/2019
 * Tel: +213 673 28 39 95
 * Email: abdo2009198@gmail.com
 **/

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.raismedia.taxiclient.R;
import com.raismedia.taxiclient.activity.MainActivity;
import com.raismedia.taxiclient.fragment.driver.FragmentDashboard;
import com.raismedia.taxiclient.fragment.customer.FragmentRideCanceled;
import com.raismedia.taxiclient.fragment.driver.FragmentRideCanceledDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideCompleted;
import com.raismedia.taxiclient.fragment.driver.FragmentRideCompletedDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideConfirmed;
import com.raismedia.taxiclient.fragment.driver.FragmentRideConfirmedDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideNew;
import com.raismedia.taxiclient.fragment.driver.FragmentRideNewDriver;
import com.raismedia.taxiclient.fragment.customer.FragmentRideOnRide;
import com.raismedia.taxiclient.fragment.driver.FragmentRideOnRideDriver;
import com.raismedia.taxiclient.model.M;

import org.json.JSONException;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String tag = remoteMessage.getNotification().getTag();
        String[] sound_ = {};
        String lat_conducteur = "", lng_conducteur = "", lat_client = "", lng_client = "";
        if(tag.equals("rideconfirmed")) {
            String sound = remoteMessage.getNotification().getSound().replace(" ", ".");
            sound_ = sound.split("_");
            lat_conducteur = sound_[0];
            lng_conducteur = sound_[1];
            lat_client = sound_[2];
            lng_client = sound_[3];
        }

//        Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();


//        if (!Helper.isAppRunning(getBaseContext(), getResources().getString(R.string.package_name))) {
//            // App is not running
//            if(M.isPushNotify(getBaseContext()) && !M.getID(getBaseContext()).equals(""))
//                sendNotification(title, message);
//        } else {
//            // App is running
////            Intent localMessage = new Intent(CurrentActivityReceiver.CURRENT_ACTIVITY_ACTION);
////            LocalBroadcastManager.getInstance(getBaseContext()).
////                    sendBroadcast(localMessage);
//        }
        sendNotification(title, message, tag, lat_conducteur, lng_conducteur, lat_client, lng_client);
    }

    @SuppressLint("WrongConstant")
    public void sendNotification(String title, String messageBody, String tag, final String lat_conducteur, final String lng_conducteur, final String lat_client, final String lng_client) {
        String tabMessage[] = {};
        Intent intent = new Intent(this, MainActivity.class);
        if(tag.length() != 0) {
            if (tag.equals("ridenewrider"))
                intent.putExtra("fragment_name", "ridenewrider");
            else if (tag.equals("ridenew"))
                intent.putExtra("fragment_name", "ridenew");
            else if(tag.equals("rideconfirmed_book"))
                intent.putExtra("fragment_name", "rideconfirmed_book");
            /*else if(tag.equals("rideconfirmed")) {
                intent.putExtra("fragment_name", "rideconfirmed");
                intent.putExtra("lat_conducteur", lat_conducteur);
                intent.putExtra("lng_conducteur", lng_conducteur);
                intent.putExtra("lat_client", lat_client);
                intent.putExtra("lng_client", lng_client);
            }*/else if(tag.equals("rideonride"))
                intent.putExtra("fragment_name", "rideonride");
            else if(tag.equals("riderejected"))
                intent.putExtra("fragment_name", "riderejected");
            else if(tag.equals("ridecompleted"))
                intent.putExtra("fragment_name", "ridecompleted");
            else if(tag.equals("ridecanceledrider"))
                intent.putExtra("fragment_name", "ridecanceledrider");
            else {
                intent.putExtra("fragment_name", "");
                /*intent = new Intent(this, TchatActivity.class);
                tabMessage = tag.split("_");
                String id_envoyeur = tabMessage[0];
                String id_receveur = tabMessage[1];
                String id_ride = tabMessage[2];
                String nom_receveur = tabMessage[3];
                intent.putExtra("id_envoyeur",id_envoyeur);
                intent.putExtra("id_receveur",Integer.parseInt(id_receveur));
                intent.putExtra("id_ride",Integer.parseInt(id_ride));
                intent.putExtra("nom_receveur",nom_receveur);*/
            }
        }else{
            intent.putExtra("fragment_name", "");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channel_id=this.getResources().getString(R.string.app_name);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        @SuppressLint("WrongConstant") NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id)
                .setContentTitle(title)
                .setSubText("")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(channel_id)
                .setContentIntent(pendingIntent);

        //Vibration
        long[] v = {500,1000};
        notificationBuilder.setVibrate(v);
        //LED
        notificationBuilder.setLights(Color.RED, 3000, 3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_car_top_view)
                    .setBadgeIconType(R.drawable.ic_car_top_view);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_car_top_view)
                    .setBadgeIconType(R.drawable.ic_car_top_view);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channel_id, channel_id, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        Log.i("MyFirebaseMsgService",messageBody);

        if(!M.getID(getBaseContext()).equals("")) {
            if(M.getUserCategorie(getBaseContext()).equals("user_app")) {
                if (MainActivity.getCurrentFragment().getTag().equals("new") && FragmentRideNew.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideNew.getRide().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("confirmed") && FragmentRideConfirmed.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideConfirmed.getRideConfirmed().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("on_ride") && FragmentRideOnRide.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideOnRide.getRideOnRide().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("completed") && FragmentRideCompleted.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideCompleted.getRideCompleted().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("canceled") && FragmentRideCanceled.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideCanceled.getRideCanceled().execute();
                }else if(tag.equals("rideconfirmed")) {
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            try {
                                MainActivity.dialogTimeOut(lat_conducteur,lng_conducteur,lat_client,lng_client);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }else if(M.getUserCategorie(getBaseContext()).equals("conducteur")){
                if (MainActivity.getCurrentFragment().getTag().equals("dashboard") && FragmentDashboard.connectionDetector.isConnectingToInternet()) {
                    new FragmentDashboard.getDashboard().execute();
                }else if (MainActivity.getCurrentFragment().getTag().equals("new") && FragmentRideNewDriver.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideNewDriver.getRide().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("confirmed") && FragmentRideConfirmedDriver.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideConfirmedDriver.getRideConfirmed().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("on_ride") && FragmentRideOnRideDriver.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideOnRideDriver.getRideOnRide().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("completed") && FragmentRideCompletedDriver.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideCompletedDriver.getRideCompleted().execute();
                } else if (MainActivity.getCurrentFragment().getTag().equals("canceled") && FragmentRideCanceledDriver.connectionDetector.isConnectingToInternet()) {
                    new FragmentRideCanceledDriver.getRideCanceled().execute();
                }
            }
        }
    }
}