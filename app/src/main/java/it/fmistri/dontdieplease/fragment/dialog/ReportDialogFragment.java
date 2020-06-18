package it.fmistri.dontdieplease.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import it.fmistri.dontdieplease.BuildConfig;
import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.Entry;
import it.fmistri.dontdieplease.db.Report;
import it.fmistri.dontdieplease.db.ReportWithEntries;
import it.fmistri.dontdieplease.fragment.summary.CategorizedAdapter;
import it.fmistri.dontdieplease.functional.ArrayFunctional;
import it.fmistri.dontdieplease.util.NumberUtil;

public class ReportDialogFragment extends DialogFragment {

    /**
     * Listener interface for dialog completion. When the dialog is dismissed
     * {@link OnDismissListener#onDismiss(DialogFragment)} is called, populated with the
     * dismissed fragment instance.
     */
    public interface OnDismissListener {
        public void onDismiss(DialogFragment fragment);
    }

    private static String EDIT_ARG_KEY = "edit";
    private static String REPORT_ARG_KEY = "report_id";

    private ReportDialogViewModel viewModel;

    // UI
    private Spinner[] categorySpinners;
    private EditText[] categoryEditTexts;
    private EditText notesEditText;

    // Parameters
    private boolean edit;
    private long reportId;

    private ReportWithEntries report;

    // Used to notify the father context
    private OnDismissListener listener;

    public ReportDialogFragment() {
        // Persist on configuration changes
        setRetainInstance(true);
    }

    /**
     * Instantiate a new fragment.
     * A ReportDialogFragment can be in 'insertion' or 'editing' mode. When in insertion mode, a
     * new report will be created from the user input. When in editing mode, the fragment will be
     * populated with previously existing data and the user will be able to update it(the actual
     * report data will be accessed from a given id that will be looked up in the db(thanks to
     * the ViewModel).
     * @param edit Whether this fragment instance is for the insertion of a new report or is
     *             for editing a previously added report.
     * @param reportId The report index used for lookup in a ReportWithEntries array(this value
     *                     isn't used if edit = false).
     * @return A new fragment instance.
     */
    public static ReportDialogFragment newInstance(boolean edit, long reportId) {
        // Create a bundle of arguments
        Bundle args = new Bundle();
        args.putBoolean(EDIT_ARG_KEY, edit);
        args.putLong(REPORT_ARG_KEY, reportId);

        // Create the fragment and set the arguments
        ReportDialogFragment fragment = new ReportDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve arguments
        edit = getArguments().getBoolean(EDIT_ARG_KEY, false);
        reportId = getArguments().getLong(REPORT_ARG_KEY, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Set dialog title
        if (edit)
            getDialog().setTitle(R.string.dialog_report_title_edit);
        else
            getDialog().setTitle(R.string.dialog_report_title);

        return inflater.inflate(R.layout.dialog_report, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save useful views
        // Entries for each category(2 of them for now, but may be dynamic in the future)
        View[] entries = new View[]{
                getView().findViewById(R.id.first_line),
                getView().findViewById(R.id.second_line)
        };

        // Save category spinners
        categorySpinners = ArrayFunctional.map(new Function<View, Spinner>() {
            @Override
            public Spinner apply(View input) {
                return input.findViewById(R.id.category_spinner);
            }
        }, entries).toArray(new Spinner[0]);

        // Save category value editors
        categoryEditTexts = ArrayFunctional.map(new Function<View, EditText>() {
            @Override
            public EditText apply(View input) {
                return input.findViewById(R.id.category_value);
            }
        }, entries).toArray(new EditText[0]);

        notesEditText = getView().findViewById(R.id.notes);

        // Observe changes from in the database from a ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ReportDialogViewModel.class);
        viewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<Category[]>() {
            @Override
            public void onChanged(Category[] categories) {
                changedCategories(categories);
            }
        });

        // If in 'edit mode', observe the report in order to update the UI.
        // If in 'insert mode', create an empty report that will be populated when the user
        // confirms.
        report = new ReportWithEntries();

        if (edit) {
            // Edit mode
            viewModel.getReport(reportId).observe(getViewLifecycleOwner(),
                    new Observer<ReportWithEntries>() {
                        @Override
                        public void onChanged(ReportWithEntries report) {
                            changedReport(report);
                        }
                    });

            // Manage the delete button
            MaterialButton deleteButton = view.findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete();
                }
            });
        }

        // Set callback for the 'done' button
        ((MaterialButton) view.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set dialog size
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = (int) (size.x * 0.85);

        window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // Notify the listener
        if (listener != null)
            listener.onDismiss(this);
    }

    /**
     * Callback for the Category LiveData.
     * When categories are updated(should not happen dynamically anyway), update the
     * spinners.
     * @param categories The updated array of categories.
     */
    private void changedCategories(Category[] categories) {
        // Add the categories to the spinners
        for (int i = 0; i < categorySpinners.length; i++) {
            categorySpinners[i].setAdapter(new CategoryAdapter(requireContext(),
                    categories));

            // If on edit, changing categories is forbidden (so far)
            if (edit)
                categorySpinners[i].setEnabled(false);

            // Set default category(if possible, different for each spinner) only if not in edit
            // mode(in which case the values are set according to the edited report values).
            if (categories.length > i && !edit)
                categorySpinners[i].setSelection(i);
        }
    }

    /**
     * Callback for the ReportWithEntries LiveData.
     * When the selected report changes, the UI is updated(used in edit mode).
     * WARNING: Complexity O(C * N) where:
     *      C is the total number of categories in the spinners
     *      N is the number of entries in the report
     * @param report Updated report.
     */
    private void changedReport(ReportWithEntries report) {
        if (!edit || report == null)
            return;

        this.report = report;

        // Populate spinners and texts
        // For each spinner
        HashSet<String> used = new HashSet<>();
        for (int i = 0; i < categorySpinners.length; i++) {
            if (report.entries.size() < i)
                break;
            Entry entry = report.entries.get(i);

            /// ** Populate spinners ** ///
            CategoryAdapter adapter = (CategoryAdapter) categorySpinners[i].getAdapter();
            final String categoryName = entry.category_name;

            Pair<Category, Integer> matchingEntry = ArrayFunctional.find(
                    new Function<Category, Boolean>() {
                        @Override
                        public Boolean apply(Category input) {
                            return input.name.equals(categoryName);
                        }
                    }, adapter.getCategories());

            // If a category has a matching entry
            if (matchingEntry != null && !used.contains(categoryName)) {

                // Set the category
                Log.d("EDIT", "Updated spinner: " + i + ", with category: " +
                        categoryName);
                categorySpinners[i].setSelection(matchingEntry.second);
                used.add(categoryName);
            }

            /// ** Populate texts ** ///
            Locale loc = requireContext().getResources().getConfiguration().locale;
            categoryEditTexts[i].setText(String.format(loc, "%f", entry.value));

        }

        // Populate notes
        notesEditText.setText(report.report.note);
    }


    /**
     * Submit the collected data({@link ReportDialogFragment#report}) using
     * This method uses {@link ReportDialogFragment#updateInnerReport()} and dismiss the dialog.
     * Callback for the done button click.
     */
    private void done() {
        // Validate data
        if (!validateData())
            return;

        // Update the contained report and give the data to the ViewModel
        updateInnerReport();

        viewModel.addReport(report);

        dismiss();
    }

    /**
     * Update the {@link ReportDialogFragment#report} attribute based on how the user populated
     * the UI.
     */
    private void updateInnerReport() {
        // assert categorySpinners.length == categoryEditTexts.length;

        // Set entries
        Entry[] entries = new Entry[categorySpinners.length];
        for (int i = 0; i < categorySpinners.length; i++) {
            // Dump data from UI
            Entry entry = new Entry();
            entry.category_name = ((Category) categorySpinners[i].getSelectedItem()).name;

            try {
                entry.value = NumberUtil.parseDouble(requireContext(),
                        categoryEditTexts[i].getText().toString());
            }
            catch (Exception ignored) { }   // This should never trigger, since the data is
                                            // validate beforehand.

            // Set entry in the temporary array
            entries[i] = entry;
        }
        report.entries = Arrays.asList(entries);

        report.report = report.report == null ? new Report() : report.report;

        // Set notes
        report.report.note = notesEditText.getText().toString();

        // Set date to 'now'(only if adding a new report)
        if (!edit)
            report.report.date = new Date(System.currentTimeMillis());
    }

    /**
     * Validate user inserted data(in particular, EditTexts. If any error is found, it will be
     * signaled to the user through the UI.
     * @return true if all the inserted data is valid. False otherwise.
     */
    private boolean validateData() {
        boolean ret = true;

        // Used for spinners validation
        HashSet<String> usedCategories = new HashSet<>();

        for (int i = 0; i < categorySpinners.length; i++) {
            Spinner spinner = categorySpinners[i];
            String selectedCategoryName = ((Category) spinner.getSelectedItem()).name;
            EditText editText = categoryEditTexts[i];

            // Two spinners may not have the same selected category
            if (usedCategories.contains(selectedCategoryName)) {
                ((TextView) spinner.getSelectedView()).setError(
                        getResources().getString(R.string.invalid_category));
                ret = false;
            }
            usedCategories.add(selectedCategoryName);

            // All the input values should be parsable as doubles
            try {
                NumberUtil.parseDouble(requireContext(), editText.getText().toString());
            }
            catch (NumberFormatException | NullPointerException | ParseException e) {
                editText.setError(getResources().getString(R.string.invalid_double_value));
                ret = false;
            }
        }

        return ret;
    }

    /**
     * Set the listener that will be notified at the occurrence of
     * {@link ReportDialogFragment#onDismiss(DialogInterface)}.
     * @param listener The listener instance.
     */
    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

    /**
     * Delete the selected report(can't be undone).
     */
    public void delete() {
        viewModel.deleteReport(reportId);
        dismiss();
    }
}
