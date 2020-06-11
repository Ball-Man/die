package it.fmistri.dontdieplease;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.Monitor;
import it.fmistri.dontdieplease.db.Report;
import it.fmistri.dontdieplease.util.DateUtil;
import it.fmistri.dontdieplease.util.NotificationBuilder;

/**
 * Receive intents and publish a notification based on its content.
 */
public class NotificationPublisher extends BroadcastReceiver {
    public static String NOTIFICATION_KEY = "notification_instance";
    public static String REMINDER_NOTIFICATION_ID_KEY = "notification_id";
    public static int REMINDER_NOTIFICATION_ID = 0;
    public static String NOTIFICATION_TYPE = "notification_type";

    public static int MIN_WARNING_IMPORTANCE = 3;

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

        notifyReminder(context, intent);
        notifyWarning(context);
    }

    /**
     * Shoot a reminder notification if no report was inserted today.
     * @param ctx The context (used for the database and notification manager).
     * @param intent The received intent(should contain a notification
     */
    private void notifyReminder(final Context ctx, final Intent intent) {
        final Notification notification = intent.getParcelableExtra(NOTIFICATION_KEY);
        if (notification == null)
            return;

        GregorianCalendar calendar = new GregorianCalendar();
        final Pair<Date, Date> dateRange = DateUtil.getDayRange(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Retrieve reports
                Report[] reports = DieDatabase.getInstance(ctx)
                        .reportDAO().getReports(dateRange.first, dateRange.second);

                if (reports.length == 0) {
                    // Get notification manager
                    NotificationManagerCompat notificationManager = NotificationManagerCompat
                            .from(ctx);

                    // Publish notification
                    int id = intent.getIntExtra(REMINDER_NOTIFICATION_ID_KEY, 0);
                    notificationManager.notify(id, notification);
                }
            }
        });
    }

    /**
     * Generate and shoot a warning notification for each {@link Monitor} that exceeded the
     * threshold.
     * @param context The context (used for the database and notification manager).
     */
    public void notifyWarning(final Context context) {
        // Get notification manager
        final NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(context);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Retrieve exceeded monitors
                Monitor[] monitors = DieDatabase.getInstance(context).monitorDAO()
                        .getTriggeredMonitorsSync(new GregorianCalendar().getTime(),
                                MIN_WARNING_IMPORTANCE);

                Log.d("NOT PUBLISHER", "Exceeding monitors: " + monitors.length);

                // Shoot a notification for each exceeded monitor
                for (int i = 0; i < monitors.length; i++) {
                    Log.d("NOT PUBLISHER", String.format("%d, %s, %f", monitors[i].m_id,
                            monitors[i].category_name, monitors[i].threshold));

                    notificationManager.notify((int) monitors[i].m_id,
                            NotificationBuilder.buildWarningNotification(context, monitors[i])
                    );
                }
            }
        });
    }
}
