package com.cilpl.clusters.FCm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


import com.cilpl.clusters.Activites.HomeMainActivity;
import com.cilpl.clusters.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessageService extends FirebaseMessagingService {
    String title,body,key;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("pushnotification", String.valueOf(remoteMessage.getData()));



        if (remoteMessage.getData() != null) {
            // Since the notification is received directly
            // from FCM, the title and the body can be
            // fetched directly as below.

            Log.e("pushnotification", String.valueOf(remoteMessage.getData()));
            JSONObject object = new JSONObject(remoteMessage.getData());
            try {
                key = object.getString("key");
                body = object.getString("body");
                title = object.getString("title");
            }catch (Exception e){
                e.printStackTrace();
            }

            showNotification(title,body);
        }




        if (remoteMessage.getData().size() > 0) {
            //handle the data message here


            try {


                JSONObject object = new JSONObject(remoteMessage.getData());
                Log.e("firebaseobject", String.valueOf(object));
//

                if (object.has("key")) {


                    key = object.getString("key");

                }



                if (object.has("body")) {
                    body = object.getString("body");
                }

                if (object.has("title")) {
                    title = object.getString("title");
                }


                handleDataMessage(key, body, title);




            } catch (Exception e) {
                e.printStackTrace();
            }




        }
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.e("messs", title + "\n" + body);
            String clickaction = remoteMessage.getNotification().getClickAction();
            showNotification(title,body);
        }
    }

    private void handleDataMessage(String key, String body, String title) {
        Log.e("firebasswett",key+","+body+","+title);
        showNotification(title,body);
    }

    private void showNotification(String title, String body) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.cynergy_logo);

        Intent intent = new Intent(this, HomeMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id_1")
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(title)
                .setLargeIcon(icon)
                .setColor(Color.RED)
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.cynergy_logo);

//        try {
//            String picture_url = data.get("picture_url");
//            if (picture_url != null && !"".equals(picture_url)) {
//                URL url = new URL(picture_url);
//                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                notificationBuilder.setStyle(
//                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
//                );
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id_1", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("channel description");
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void showNotification1(String title,
                                   String message) {


        {
            // Pass the intent to switch to the MainActivity
            Intent intent
                    = new Intent(this, HomeMainActivity.class);
            // Assign channel ID
            String channel_id = "channel_id_1";
            // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
            // the activities present in the activity stack,
            // on the top of the Activity that is to be launched
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Pass the intent to PendingIntent to start the
            // next Activity
            PendingIntent pendingIntent
                    = PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_IMMUTABLE);

            // Create a Builder object using NotificationCompat
            // class. This will allow control over all the flags
            NotificationCompat.Builder builder
                    = new NotificationCompat
                    .Builder(getApplicationContext(),
                    channel_id)
                    .setSmallIcon(R.drawable.cynergy_logo)
                    .setAutoCancel(true)
                    .setVibrate(new long[] { 1000, 1000, 1000,
                            1000, 1000 })
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent);

            // A customized design for the notification can be
            // set only for Android versions 4.1 and above. Thus
            // condition for the same is checked here.
            if (Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.JELLY_BEAN) {
                //  builder = builder.setContent(getCustomDesign(title, message));
            } // If Android Version is lower than Jelly Beans,
            // customized layout cannot be used and thus the
            // layout is set as follows
            else {
                builder = builder.setContentTitle(title)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.cynergy_logo);
            }
            // Create an object of NotificationManager class to
            // notify the
            // user of events that happen in the background.
            NotificationManager notificationManager
                    = (NotificationManager)getSystemService(
                    Context.NOTIFICATION_SERVICE);
            // Check if the Android Version is greater than Oreo
            if (Build.VERSION.SDK_INT
                    >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel
                        = new NotificationChannel(
                        channel_id, "channel_id_1",
                        NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(
                        notificationChannel);
            }

            notificationManager.notify(0, builder.build());
        }
    }




}
