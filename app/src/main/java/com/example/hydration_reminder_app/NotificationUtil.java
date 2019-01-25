package com.example.hydration_reminder_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationUtil {

    private static final int LAUNCH_MAIN_ACTIVITY_PENDING_INTENT_ID = 1111;
    private static final int INCREMENT_WATER_COUNT_PENDING_INTENT_ID = 1112;
    private static final int DISMISS_NOTIFICATION_PENDING_INTENT_ID = 1113;
    private static final String WATER_NOTIFICATION_CHANNEL_ID = "1114";
    private static final int WATER_NOTIFICATION_ID = 1115;
    private static final String WATER_NOTIFICATION_CHANNEL_NAME = "water-reminder-notification-channel";

    public static void createReminderNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                                            context.getSystemService(Context.NOTIFICATION_SERVICE);

        // create notification channel if android version
        // equal to or greater than OREO
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(
                    WATER_NOTIFICATION_CHANNEL_ID,
                    WATER_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationManager.createNotificationChannel(notificationChannel);
        }

        // create the notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                context,
                WATER_NOTIFICATION_CHANNEL_ID
        );

        notificationBuilder.setContentTitle("Reminder: Hydrate Yourself")
                           .setContentText("Its time to drink some water")
                           .setSmallIcon(R.drawable.ic_local_drink)
                           .setStyle(
                                     new NotificationCompat.BigTextStyle()
                                         .bigText("Its time to drink some water, so that you don't dehydrate")
                           )
                          .setDefaults(Notification.DEFAULT_SOUND)
                          .setContentIntent(NotificationUtil.launchMainActivityPendingIntent(context))
                          .setAutoCancel(true)
                          .addAction(
                                  R.drawable.ic_local_drink,
                                  "I did it",
                                  incWaterCountPendingIntent(context)
                          )
                          .addAction(
                                  R.drawable.ic_local_drink,
                                  "No thanks",
                                  dismissNotificationPendingIntent(context)
                          );

        // if android version older then OREO but equal or greater than NOUGAT,
        // set the priority on the Notification.Builder instead of NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                  && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        notificationManager.notify(WATER_NOTIFICATION_ID, notificationBuilder.build());
    }

    // returns a pending intent to launch main activity when notification is clicked
    private static PendingIntent launchMainActivityPendingIntent(Context context) {
        Intent launchMainActivity = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                LAUNCH_MAIN_ACTIVITY_PENDING_INTENT_ID,
                launchMainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    // returns a pending intent to increment water count using WaterService
    // when an action on notification is clicked
    private static PendingIntent incWaterCountPendingIntent(Context context) {
        Intent incrementWaterCount = new Intent(context, WaterService.class);
        incrementWaterCount.setAction(WaterService.ACTION_INCREMENT_WATER_COUNT);

        return PendingIntent.getService(
                context,
                INCREMENT_WATER_COUNT_PENDING_INTENT_ID,
                incrementWaterCount,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    // returns a pending intent to dismiss notification using WaterService
    // when an action on notification is clicked
    private static PendingIntent dismissNotificationPendingIntent(Context context) {
        Intent dismissNotification = new Intent(context, WaterService.class);
        dismissNotification.setAction(WaterService.ACTION_DISMISS_NOTIFICATION);

        return PendingIntent.getService(
                context,
                DISMISS_NOTIFICATION_PENDING_INTENT_ID,
                dismissNotification,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager manager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
}
