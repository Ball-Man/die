package it.fmistri.dontdieplease;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.NotificationsSettings;
import it.fmistri.dontdieplease.util.NotificationBuilder;

/**
 * Receives boot intents to activate notification alarms.
 */
public class BootListener extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
            return;

        // Build notification pending intent
        final AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent alarmIntent = NotificationBuilder.buildReminderPendingIntent(
                NotificationBuilder.buildReminderNotification(context),
                context);

        // Query notifications settings and set alarm
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                // Get settings
                NotificationsSettings[] settingsCollection = DieDatabase.getInstance(context)
                        .notificationsSettingsDAO().getNotificationsSettingsSync();
                NotificationsSettings settings;

                if (settingsCollection.length == 0)
                    return;

                settings = settingsCollection[0];

                // If notifications are enabled, register a daily alarm
                if (settings.enabled) {
                    // Get time
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.set(Calendar.HOUR_OF_DAY, (int) settings.hour);
                    calendar.set(Calendar.MINUTE, (int) settings.minute);

                    // Register alarm
                    alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, alarmIntent);
                }
            }
        });
    }
}
