package it.fmistri.dontdieplease.fragment.notifications;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.NotificationsSettings;

/**
 * ViewModel for {@link NotificationsFragment}. Reads from the database
 */
public class NotificationsViewModel extends ViewModel {
    private LiveData<NotificationsSettings[]> notificationSettings;

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
     * Update the notification settings.
     * @param settings The instance of notifications settings to update.
     */
    public void updateNotificationsSettings(final NotificationsSettings settings) {
        // Update DB
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DieDatabase.getInstance(null).notificationsSettingsDAO()
                        .updateNotificationsSettings(settings);
            }
        });

        // TODO: Manage timer and intents for a broadcast receiver
    }
}
