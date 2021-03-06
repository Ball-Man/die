package it.fmistri.dontdieplease.fragment.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.Monitor;
import it.fmistri.dontdieplease.db.NotificationsSettings;
import it.fmistri.dontdieplease.util.NotificationBuilder;

/**
 * ViewModel for {@link NotificationsFragment}. Reads from the database
 */
public class NotificationsViewModel extends ViewModel {
    private LiveData<NotificationsSettings[]> notificationSettings;
    private LiveData<Monitor[]> monitors;
    private LiveData<Category[]> categories;

    /**
     * @return An observable containing the array of notifications settings.
     */
    public LiveData<NotificationsSettings[]> getNotificationsSettings() {
        if (notificationSettings == null)
            notificationSettings = DieDatabase.getInstance(null).notificationsSettingsDAO()
                    .getNotificationsSettings();

        return notificationSettings;
    }

    /**
     * @return An observable collection of categories.
     */
    public LiveData<Category[]> getCategories() {
        if (categories == null)
            categories = DieDatabase.getInstance(null).categoryDAO().getCategories();

        return categories;
    }

    /**
     * Update the notification settings.
     * @param settings The instance of notifications settings to update.
     */
    public void updateNotificationsSettings(final NotificationsSettings settings, Context context) {
        // Update DB
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DieDatabase.getInstance(null).notificationsSettingsDAO()
                        .updateNotificationsSettings(settings);
            }
        });

        // Build notification pending intent
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = NotificationBuilder.buildReminderPendingIntent(
                NotificationBuilder.buildReminderNotification(context),
                context);

        // If notifications are enabled, register a daily alarm
        if (settings.enabled) {
            // Get time
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.HOUR_OF_DAY, (int) settings.hour);
            calendar.set(Calendar.MINUTE, (int) settings.minute);

            // Register alarm
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

            NotificationBuilder.enableBootListener(context);
        }
        else {  // If notifications are disabled, disable intent
            alarmMgr.cancel(alarmIntent);

            NotificationBuilder.disableBootListener(context);
        }
    }

    /**
     * @return An observable collection of {@link Monitor}.
     */
    public LiveData<Monitor[]> getMonitors() {
        if (monitors == null) {
            monitors = DieDatabase.getInstance(null).monitorDAO().getMonitors();
        }
        return monitors;
    }

    /**
     * Delete the given monitor.
     * @param monitor The monitor to delete.
     */
    public void deleteMonitor(final Monitor monitor) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DieDatabase.getInstance(null).monitorDAO().removeMonitor(monitor);
            }
        });
    }

    /**
     * Add a monitor(already existing monitors will be overwritten, meaning this method is also
     * good for updating).
     * @param monitor The monitor to be added.
     */
    public void addMonitor(final Monitor monitor) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DieDatabase.getInstance(null).monitorDAO().addMonitors(monitor);
            }
        });
    }
}
