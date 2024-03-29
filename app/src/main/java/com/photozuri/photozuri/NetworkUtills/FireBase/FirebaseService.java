package com.photozuri.photozuri.NetworkUtills.FireBase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.photozuri.photozuri.Payments.Payments;
import com.photozuri.photozuri.R;

import org.json.JSONObject;

public class FirebaseService extends FirebaseMessagingService {
    private String TAG="taggs";
    public FirebaseService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            handleNow(remoteMessage.getData().toString());

        }else{
            Log.d(TAG, "Message data payload: is lessssss");

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(String result) {
        try {
            JSONObject json = new JSONObject(result);
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            int code = data.getInt("code");
            //TODO read more attributes here
            sendNotification(title, message);

            Intent pushNotification = new Intent(Payments.NOTIFICATION);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("title", title);
            pushNotification.putExtra("code", code);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        } catch (Exception e) {
            sendNotification("bbbb", "mbh");
            Intent pushNotification = new Intent(Payments.NOTIFICATION);
            pushNotification.putExtra("message", "");
            pushNotification.putExtra("title", "");
            pushNotification.putExtra("code", 0);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, Payments.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_pic)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri);
        //.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }
}
