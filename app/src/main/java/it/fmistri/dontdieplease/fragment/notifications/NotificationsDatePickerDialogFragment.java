package it.fmistri.dontdieplease.fragment.notifications;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * Wrapper around {@link DatePickerDialog} with a bit of custom code for project
 * specific management.
 */
public class NotificationsDatePickerDialogFragment extends DialogFragment {
    // Bundle keys
    private static String YEAR_ARG = "year";
    private static String MONTH_ARG = "month";
    private static String DAY_ARG = "day";

    private DatePickerDialog.OnDateSetListener listener;
    private int year, month, day;

    /**
     * Create a new instance of {@link DatePickerDialog} setting the given year, month and day as
     * starting point.
     * @param year The gregorian year.
     * @param month The year's month(0 based).
     * @param day The month's day.
     * @return A new dialog instance.
     */
    public static NotificationsDatePickerDialogFragment newInstance(int year, int month,
                                                                    int day) {
        // Populate bundle
        Bundle bundle = new Bundle();
        bundle.putInt(YEAR_ARG, year);
        bundle.putInt(MONTH_ARG, month);
        bundle.putInt(DAY_ARG, day);

        // Set bundle as arguments
        NotificationsDatePickerDialogFragment fragment =
                new NotificationsDatePickerDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve arguments as default values for the picker
        year = getArguments().getInt(YEAR_ARG);
        month = getArguments().getInt(MONTH_ARG);
        day = getArguments().getInt(DAY_ARG);

        return new DatePickerDialog(requireActivity(),
                listener, year, month, day);
    }

    /**
     * Set the listener that will be notified when a date is chosen by the user.
     * @param listener The listener.
     */
    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }
}
