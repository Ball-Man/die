package it.fmistri.dontdieplease.fragment.notifications;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.NotificationsSettings;

/**
 * Fragment for the notifications settings layout.
 */
public class NotificationsFragment extends Fragment implements Observer<NotificationsSettings[]>,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        TimePickerDialog.OnTimeSetListener{
    private NotificationsViewModel viewModel;

    // Views
    private Switch notificationsSwitch;
    private MaterialButton chooseTimeButton;
    private TextView timeTextView;

    // Temporary data
    private NotificationsSettings settings;
    private boolean forced_update = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save useful views
        notificationsSwitch = view.findViewById(R.id.toggle_notifications);
        chooseTimeButton = view.findViewById(R.id.time_button);
        timeTextView = view.findViewById(R.id.selected_time);

        notificationsSwitch.setOnCheckedChangeListener(this);
        chooseTimeButton.setOnClickListener(this);

        // Retrieve a ViewModel instance
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        // Observe changes in settings
        viewModel.getNotificationsSettings().observe(getViewLifecycleOwner(), this);
    }

    /**
     * Retrieve the updated {@link NotificationsSettings} instance and temporary save it for
     * later management. Note that even if it is an array, its length should always be 1.
     * @param notificationsSettings The updated (array of) settings.
     */
    @Override
    public void onChanged(NotificationsSettings[] notificationsSettings) {
        // Set to true to signal other components that these state changes aren't input from the
        // user, but from the ViewModel.
        forced_update = true;

        if (notificationsSettings.length > 0)
            settings = notificationsSettings[0];
        else
            settings = new NotificationsSettings();

        // Update UI
        notificationsSwitch.setChecked(settings.enabled);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, (int) settings.hour);
        calendar.set(Calendar.MINUTE, (int) settings.minute);
        Date time = calendar.getTime();

        // Get current locale
        Locale loc = requireContext().getResources().getConfiguration().locale;

        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT, loc);
        timeTextView.setText(format.format(time));

        // Reset force updated for further use
        forced_update = false;
    }

    /**
     * On switch toggled, enable/disable notifications.
     * @param buttonView The toggled switch.
     * @param isChecked Whether it's been toggled or un-toggled
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        settings.enabled = isChecked;

        if (!forced_update)
            viewModel.updateNotificationsSettings(settings, requireContext());
    }

    /**
     * On button clicked, popup a {@link android.widget.TimePicker} that lets the user choose
     * at which time of the day the reminder should be sent.
     * @param v The clicked view(Button).
     */
    @Override
    public void onClick(View v) {
        NotificationsTimePickerDialogFragment fragment = NotificationsTimePickerDialogFragment
                .newInstance(settings.hour, settings.minute);

        fragment.show(getChildFragmentManager(), "time_picker");
    }

    /**
     * When the user selects a new notification time this event will be called. Update the settings
     * through the ViewModel.
     * @param view The view that triggered the event.
     * @param hourOfDay The selected hour of the day.
     * @param minute The selected minute.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        settings.hour = hourOfDay;
        settings.minute = minute;

        viewModel.updateNotificationsSettings(settings, requireContext());
    }
}
