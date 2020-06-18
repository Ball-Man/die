package it.fmistri.dontdieplease.fragment.notifications;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.Monitor;
import it.fmistri.dontdieplease.fragment.dialog.CategoryAdapter;
import it.fmistri.dontdieplease.functional.ArrayFunctional;
import it.fmistri.dontdieplease.util.NumberUtil;

/**
 * Controller class used as listener for monitor views events.
 * This class exists in order to avoid using a single {@link androidx.fragment.app.Fragment} for
 * each element in the {@link Monitor} list. A {@link androidx.fragment.app.ListFragment} might
 * be a better solution applied in the future.
 */
public class MonitorItem implements View.OnClickListener, Observer<Category[]> {
    private Context context;

    // Useful views
    private Monitor monitor;
    private MaterialButton startTimeButton;
    private MaterialButton endTimeButton;
    private EditText thresholdEdit;
    private Spinner categorySpinner;

    // ViewModel
    private NotificationsViewModel viewModel;

    public MonitorItem(Fragment fragment, final Monitor monitor, View view,
                       final NotificationsViewModel viewModel) {
        this.context = fragment.requireContext();
        this.viewModel = viewModel;
        this.monitor = monitor;

        // Useful views
        MaterialButton deleteButton = (MaterialButton) view.findViewById(R.id.delete_button);
        MaterialButton saveButton = (MaterialButton) view.findViewById(R.id.save_button);
        this.startTimeButton = (MaterialButton) view
                .findViewById(R.id.start_time_button);
        this.endTimeButton = (MaterialButton) view
                .findViewById(R.id.end_time_button);
        this.categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        this.thresholdEdit = (EditText) view.findViewById(R.id.threshold_edit);


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

        // Observe categories
        viewModel.getCategories().observe(fragment.getViewLifecycleOwner(), this);
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
        if (!validateData())
            return;

        try {
            monitor.threshold = NumberUtil.parseDouble(context, thresholdEdit.getText().toString());
        }
        catch (Exception ignored) { }   // This should never trigger, since there is a validation
                                        // beforehand.

        if (categorySpinner.getSelectedItem() != null)
            monitor.category_name = ((Category) categorySpinner.getSelectedItem()).name;
        viewModel.addMonitor(monitor);
    }

    /**
     * Validate data inserted by the user.
     * @return true if the data is fine, false is something's wrong.
     */
    private boolean validateData() {
        boolean ret = true;
        try {
            NumberUtil.parseDouble(context, thresholdEdit.getText().toString());
        }
        catch (NullPointerException | NumberFormatException | ParseException e) {
            ret = false;
            thresholdEdit.setError(context.getString(R.string.invalid_double_value));
        }

        return ret;
    }

    /**
     * Update spinner with the given categories.
     * @param categories The updated categories.
     */
    @Override
    public void onChanged(Category[] categories) {
        // Create and set adapter
        CategoryAdapter categoryAdapter = new CategoryAdapter(context, categories);
        categorySpinner.setAdapter(categoryAdapter);

        // Check if the given categories contain the one wanted by this monitor
        Pair<Category, Integer> result = ArrayFunctional.find(new Function<Category, Boolean>() {
            @Override
            public Boolean apply(Category input) {
                return input.name.equals(monitor.category_name);
            }
        }, categories);

        // ... if the category is found, set the spinner on it
        if (result != null)
            categorySpinner.setSelection(result.second);
    }
}
