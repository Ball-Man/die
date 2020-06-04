package it.fmistri.dontdieplease;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.Report;
import it.fmistri.dontdieplease.util.DateUtil;
import it.fmistri.dontdieplease.util.NotificationBuilder;

/**
 * Receive intents and publish a notification based on its content.
 */
public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION = "notification_instance";
    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION_TYPE = "notification_type";

    /**
     * Notification types used by the publisher to determine what to do(reminder notifications
     * will be skipped if at least one report has already been inserted).
     */
    public enum NotificationType {
        REMINDER,
        WARNING
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("NOT PUBLISHER", "Received intent");

        final Context ctx = context;
        final Intent int_ = intent;

        // Retrieve reports
        GregorianCalendar calendar = new GregorianCalendar();
        final Pair<Date, Date> dateRange = DateUtil.getDayRange(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Report[] reports = DieDatabase.getInstance(ctx)
                        .reportDAO().getReports(dateRange.first, dateRange.second);

                if (reports.length == 0) {
                    // Get notification manager
                    NotificationManagerCompat notificationManager = NotificationManagerCompat
                            .from(context);

                    // Publish notification
                    Notification notification = int_.getParcelableExtra(NOTIFICATION);
                    int id = int_.getIntExtra(NOTIFICATION_ID, 0);
                    notificationManager.notify(id, notification);
                }
            }
        });
    }
}
