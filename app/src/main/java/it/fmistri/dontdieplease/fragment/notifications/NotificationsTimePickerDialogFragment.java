package it.fmistri.dontdieplease.fragment.notifications;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class NotificationsTimePickerDialogFragment extends DialogFragment {

    // Bundle keys
    private static String HOUR_ARG = "hour";
    private static String MINUTE_ARG = "minute";

    /**
     * Create a new instance of {@link TimePickerDialog} setting the given hour and minute as
     * starting point.
     * @param hour The hour of the day(24h format).
     * @param minute The minute.
     * @return A new dialog instance.
     */
    public static NotificationsTimePickerDialogFragment newInstance(long hour, long minute) {
        // Populate bundle
        Bundle bundle = new Bundle();
        bundle.putLong(HOUR_ARG, hour);
        bundle.putLong(MINUTE_ARG, minute);

        // Set bundle as arguments
        NotificationsTimePickerDialogFragment fragment =
                new NotificationsTimePickerDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve arguments as default values for the picker

        long hour = getArguments().getLong(HOUR_ARG);
        long minute = getArguments().getLong(MINUTE_ARG);

        // Create a new instance of TimePickerDialog and return it(time listener is the parent
        // fragment).
        return new TimePickerDialog(getActivity(), (NotificationsFragment) getParentFragment(),
                (int) hour, (int) minute, DateFormat.is24HourFormat(getActivity()));
    }
}
