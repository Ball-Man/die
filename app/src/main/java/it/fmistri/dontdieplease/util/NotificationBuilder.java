package it.fmistri.dontdieplease.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import it.fmistri.dontdieplease.BootListener;
import it.fmistri.dontdieplease.InsertReportActivity;
import it.fmistri.dontdieplease.NotificationPublisher;
import it.fmistri.dontdieplease.PostponeActivity;
import it.fmistri.dontdieplease.R;

/**
 * Contains util methods to create notifications and associated intents for the app.
 */
public class NotificationBuilder {
    // Reference for unique ids for specific pending intents.
    private static int REMINDER = 0;
    private static int REMINDER_CONTENT = 1;
    private static int REMINDER_POSTPONE = 2;

    private static String NOTIFICATION_CHANNEL = "ddp_not_channel";

    public static void createChannel(Context context) {
        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL,
                    context.getPackageName(),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getResources().getString(R.string.channel_description));
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * Build and return a reminder notification suited for this app(will trigger insertion on
     * content tap, and has an option to postpone).
     * @param context The of the app (used to get resources).
     * @return A new notification instance.
     */
    @NonNull
    public static Notification buildReminderNotification(Context context) {
        // Content tap intent
        Intent notificationIntent = new Intent(context, InsertReportActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                REMINDER_CONTENT,
                notificationIntent, 0);

        // Postpone intent
        Intent postponeIntent = new Intent(context, PostponeActivity.class);
        postponeIntent.putExtra(NotificationPublisher.REMINDER_NOTIFICATION_ID_KEY,
                NotificationPublisher.REMINDER_NOTIFICATION_ID);
        postponeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent actionIntent = PendingIntent.getActivity(context, REMINDER_POSTPONE,
                postponeIntent, 0);

        NotificationCompat.Action action = new NotificationCompat.Action(
                R.drawable.ic_favorite_black_24dp,
                context.getResources().getString(R.string.postpone), actionIntent);

        // Assemble final notification
        return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.ic_favorite_black_24dp)
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(context.getResources().getString(R.string.notification_content))
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .addAction(action)
                .setAutoCancel(true)
                .build();
    }

    /**
     * Build a pending intent from a given notification. The requestCode is kept constant so that
     * the same PendingIntent is rebuilt each time (useful for canceling).
     * @param notification The notification instance.
     * @param context The context (Used to build the {@link Intent}.
     * @param reqCode Request code for the notification's {@link PendingIntent}.
     * @return A new pending intent.
     */
    public static PendingIntent buildReminderPendingIntent(Notification notification,
                                                           Context context,
                                                           int reqCode) {
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(NotificationPublisher.NOTIFICATION_KEY, notification);
        intent.putExtra(NotificationPublisher.REMINDER_NOTIFICATION_ID_KEY,
                NotificationPublisher.REMINDER_NOTIFICATION_ID);

        return PendingIntent.getBroadcast(context, reqCode,
                intent, 0);
    }

    /**
     * Build a pending intent from a given notification. The requestCode is kept constant so that
     * the same PendingIntent is rebuilt each time (useful for canceling). The used request code
     * is constant while using this method. If you need to manually change the request code use
     * {@link NotificationBuilder#buildReminderPendingIntent(Notification, Context, int)}
     * @param notification The notification instance.
     * @param context The context (Used to build the {@link Intent}.
     * @return A new pending intent.
     */
    public static PendingIntent buildReminderPendingIntent(Notification notification,
                                                           Context context) {
        return NotificationBuilder.buildReminderPendingIntent(notification, context, REMINDER);
    }

    /**
     * Enable {@link BootListener} at startup (will re-set alarms for notifications).
     * @param context The current context.
     */
    public static void enableBootListener(Context context) {
        ComponentName receiver = new ComponentName(context, BootListener.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * Disable {@link BootListener} at startup
     * (see {@link NotificationBuilder#enableBootListener(Context)}).
     * @param context The current context.
     */
    public static  void disableBootListener(Context context) {
        ComponentName receiver = new ComponentName(context, BootListener.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }
}
