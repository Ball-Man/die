package it.fmistri.dontdieplease.fragment.notifications;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Monitor;

/**
 * Controller class used as listener for monitor views events.
 * This class exists in order to avoid using a single {@link androidx.fragment.app.Fragment} for
 * each element in the {@link Monitor} list. A {@link androidx.fragment.app.ListFragment} might
 * be a better solution applied in the future.
 */
public class MonitorItem implements View.OnClickListener {
    private Context context;

    // Useful views
    private Monitor monitor;
    private MaterialButton startTimeButton;
    private MaterialButton endTimeButton;
    private EditText thresholdEdit;

    // ViewModel
    private NotificationsViewModel viewModel;

    public MonitorItem(Context context, final Monitor monitor, View view,
                       final NotificationsViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
        this.monitor = monitor;

        // Useful views
        final MaterialButton deleteButton = (MaterialButton) view.findViewById(R.id.delete_button);
        this.startTimeButton = (MaterialButton) view
                .findViewById(R.id.start_time_button);
        this.endTimeButton = (MaterialButton) view
                .findViewById(R.id.end_time_button);
        this.thresholdEdit = (EditText) view.findViewById(R.id.threshold_edit);
        final MaterialButton saveButton = (MaterialButton) view.findViewById(R.id.save_button);

        // Change monitor date range
        startTimeButton.setOnClickListener(this);
        endTimeButton.setOnClickListener(this);

        // Delete item
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteMonitor(monitor);
            }
        });

        // Save item
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    /**
     * Manage date set buttons.
     * @param v The clicked view.
     */
    @Override
    public void onClick(View v) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(monitor.start_date);
        NotificationsDatePickerDialogFragment dialog =
                NotificationsDatePickerDialogFragment.newInstance(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

        // Use this listener if the start date button is pressed
        DatePickerDialog.OnDateSetListener startDateListener =
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int month, int dayOfMonth) {
                monitor.start_date = new GregorianCalendar(year, month, dayOfMonth)
                        .getTimeInMillis();
                viewModel.addMonitor(monitor);
            }
        };

        // Use this listener if the end date button is pressed
        DatePickerDialog.OnDateSetListener endDateListener =
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int month, int dayOfMonth) {
                monitor.end_date = new GregorianCalendar(year, month, dayOfMonth)
                        .getTimeInMillis();
                viewModel.addMonitor(monitor);
            }
        };

        // Select the appropriate listener
        DatePickerDialog.OnDateSetListener selectedListener = null;
        if (v == startTimeButton)
            selectedListener = startDateListener;
        else if (v == endTimeButton)
            selectedListener = endDateListener;

        Log.d("M ITEM", "Selected listener: " + selectedListener);

        dialog.setOnDateSetListener(selectedListener);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "date_picker");
    }

    /**
     * Send the current edited values to the ViewModel.
     */
    private void saveItem() {
        // TODO: Validate data
        monitor.threshold = Double.parseDouble(thresholdEdit.getText().toString());
        viewModel.addMonitor(monitor);
    }

}
