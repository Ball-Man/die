package it.fmistri.dontdieplease.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import it.fmistri.dontdieplease.NotificationPublisher;
import it.fmistri.dontdieplease.R;

/**
 * Contains util methods to create notifications and associated intents for the app.
 */
public class NotificationBuilder {
    /**
     * Build and return a notification suited for this app.
     * @param context The of the app (used to get resources).
     * @return A new notification instance.
     */
    @NonNull
    public static Notification buildNotification(Context context) {
        return new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_content))
                .build();
    }

    /**
     * Build a pending intent from a given notification. The requestCode is kept constant so that
     * the same PendingIntent is rebuilt each time (useful for canceling).
     * @param notification The notification instance.
     * @param context The context (Used to build the {@link Intent}.
     * @return A new pending intent.
     */
    public static PendingIntent buildPendingIntent(Notification notification, Context context) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        intent.putExtra(NotificationPublisher.NOTIFICATION_ID, 0);

        return PendingIntent.getBroadcast(context, 0,
                intent, 0);
    }
}
