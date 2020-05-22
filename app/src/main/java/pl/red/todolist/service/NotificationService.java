package pl.red.todolist.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

import pl.red.todolist.R;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class NotificationService {
    private static final String CHANNEL = "notifications";

    private final NotificationCompat.Builder notificationBuilder;
    private final NotificationManager manager;

    public NotificationService(NotificationCompat.Builder builder, NotificationManager manager) {
        notificationBuilder = builder;
        this.manager = manager;
        manager.createNotificationChannel(new NotificationChannel(CHANNEL, CHANNEL, IMPORTANCE_HIGH));
        notificationBuilder.setSmallIcon(R.drawable.notification);
        notificationBuilder.setContentTitle("To Do:");
        notificationBuilder.setChannelId(CHANNEL);
    }

    public void notify(String message, long count) {
        notificationBuilder.setContentText(count + " Task(s) " + message);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(count + "\n" + message);
        notificationBuilder.setStyle(bigText);
        manager.notify(0, notificationBuilder.build());
    }
}
