package it.fmistri.dontdieplease;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Receive intents and publish a notification based on its content.
 */
public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION = "notification_instance";
    public static String NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NOT PUBLISHER", "Received intent");

        // Check if a report has already been added today(in which case, ignore the intent)


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }
}
