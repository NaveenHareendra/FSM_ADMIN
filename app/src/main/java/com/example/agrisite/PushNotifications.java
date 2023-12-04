/*package com.example.agrisite;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;*/

/*public class PushNotifications extends FirebaseMessagingService {
    @SuppressLint({"NewApi", "MissingPermission"})
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remotemessage) {

        //Get the Notification Title & Body

        String title = remotemessage.getNotification().getTitle();
        String text = remotemessage.getNotification().getBody();

        String CHANNEL_ID = "MESSAGE";
        CharSequence name;

         NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Message Notification", NotificationManager.IMPORTANCE_HIGH);


        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Context context;
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1,notification.build());

        super.onMessageReceived(remotemessage);
    }
}*/
