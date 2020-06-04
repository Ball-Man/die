package it.fmistri.dontdieplease;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.fragment.notifications.NotificationsTimePickerDialogFragment;
import it.fmistri.dontdieplease.util.NotificationBuilder;

/**
 * The sole purpose of this activity is instantiating a
 * {@link NotificationsTimePickerDialogFragment}, giving the user the ability to postpone a
 * notification.
 */
public class PostponeActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener,
        NotificationsTimePickerDialogFragment.OnDismissListener {
    private int notificationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get notification ID from intent(used to cancel it)
        notificationId = getIntent().getIntExtra(NotificationPublisher.REMINDER_NOTIFICATION_ID_KEY,
                0);

        // Init notification channel
        NotificationBuilder.createChannel(this);

        // Start the dialog
        GregorianCalendar calendar = new GregorianCalendar();
        NotificationsTimePickerDialogFragment dialog =  NotificationsTimePickerDialogFragment
                .newInstance(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        dialog.show(getSupportFragmentManager(), "time_dialog");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Cancel previous notification
        NotificationManagerCompat.from(this).cancel(notificationId);

        // Create a one shot notification for the postponed time
        PendingIntent intent = NotificationBuilder.buildReminderPendingIntent(
                NotificationBuilder.buildReminderNotification(this), this,
                (int) System.currentTimeMillis()
        );

        // Set date and time (today at the given hour and minute)
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Register alarm
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent);

        finish();
    }

    @Override
    public void onDismiss(DialogFragment fragment) {
        //finish();
    }
}
