package it.fmistri.dontdieplease.fragment.notifications;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.Monitor;
import it.fmistri.dontdieplease.db.NotificationsSettings;
import it.fmistri.dontdieplease.view.AdaptableLinearLayout;

/**
 * Fragment for the notifications settings layout.
 */
public class NotificationsFragment extends Fragment implements Observer<NotificationsSettings[]>,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        TimePickerDialog.OnTimeSetListener {
    private NotificationsViewModel viewModel;

    // Views
    private Switch notificationsSwitch;
    private MaterialButton chooseTimeButton;
    private TextView timeTextView;
    private AdaptableLinearLayout monitorLinearLayout;
    private FloatingActionButton addMonitorFAB;

    // Temporary data
    private NotificationsSettings settings;
    private boolean forced_update = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save useful views
        notificationsSwitch = view.findViewById(R.id.toggle_notifications);
        chooseTimeButton = view.findViewById(R.id.time_button);
        timeTextView = view.findViewById(R.id.selected_time);
        monitorLinearLayout = view.findViewById(R.id.monitor_list);

        notificationsSwitch.setOnCheckedChangeListener(this);
        chooseTimeButton.setOnClickListener(this);

        addMonitorFAB = view.findViewById(R.id.add_monitor);

        // Listen and add monitors on FAB touched
        addMonitorFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTempMonitor();
            }
        });

        // Retrieve a ViewModel instance
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationsViewModel.class);

        // Observe changes in settings
        viewModel.getNotificationsSettings().observe(getViewLifecycleOwner(), this);

        // Observe changes in monitors
        viewModel.getMonitors().observe(getViewLifecycleOwner(),
                new Observer<Monitor[]>() {
            @Override
            public void onChanged(Monitor[] monitors) {
                changedMonitors(monitors);
            }
        });
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

    /**
     * Repopulate the monitor list({@link NotificationsFragment#monitorLinearLayout}).
     * @param monitors The list of updated monitors.
     */
    private void changedMonitors(Monitor[] monitors) {
        MonitorAdapter adapter = new MonitorAdapter(requireContext(), monitors, new Category[0]);
        monitorLinearLayout.populateWithAdapter(adapter);

        // Populate monitor views events
        // TODO: Find a scalable solution (Fragments/ListFragment?).
        for (int i = 0; i < monitors.length; i++) {
            new MonitorItem(requireContext(), monitors[i], monitorLinearLayout.getChildAt(i),
                    viewModel);
        }
    }

    /**
     * Add a default monitor definition to the model. This can be modified by the user or deleted.
     */
    private void addTempMonitor() {
        Monitor monitor = new Monitor();

        // Construct with default values
        GregorianCalendar calendar = new GregorianCalendar();
        long today = calendar.getTimeInMillis();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        monitor.start_date = today;
        monitor.end_date = calendar.getTimeInMillis();
        monitor.threshold = 0;

        viewModel.addMonitor(monitor);
    }
}
