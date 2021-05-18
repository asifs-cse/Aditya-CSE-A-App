package com.jntuk.engineers.Notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jntuk.engineers.R;
import com.jntuk.engineers.View.HomeActivity;

public class FirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String message) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(title, message);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        try {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "acoe")
                    .setSmallIcon(R.drawable.notification)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(999, builder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void sentNormalNotification(RemoteMessage remoteMessage) {
        
    }

    private void sendOAndAboveNotification(RemoteMessage remoteMessage) {

    }


}
