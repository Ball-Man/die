package it.fmistri.dontdieplease.fragment.notifications;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Wrapper around {@link TimePickerDialog} with a bit of custom code for project specific
 * management.
 */
public class NotificationsTimePickerDialogFragment extends DialogFragment {
    /**
     * Listener interface for dialog completion. When the dialog is dismissed
     * {@link OnDismissListener#onDismiss(DialogFragment)} is called, populated with the
     * dismissed fragment instance.
     */
    public interface OnDismissListener {
        public void onDismiss(DialogFragment fragment);
    }

    private OnDismissListener listener;

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
        // fragment or activity).
        TimePickerDialog.OnTimeSetListener listener;
        if (getParentFragment() == null)
            listener = (TimePickerDialog.OnTimeSetListener) requireActivity();
        else
            listener = (TimePickerDialog.OnTimeSetListener) getParentFragment();

        TimePickerDialog picker = new TimePickerDialog(getActivity(),
                listener,
                (int) hour, (int) minute, DateFormat.is24HourFormat(getActivity()));

        return picker;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (listener != null)
            listener.onDismiss(this);
    }

    /**
     * Set the listener that will be called on dismissal.
     * @param listener The designed listener.
     */
    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }
}

