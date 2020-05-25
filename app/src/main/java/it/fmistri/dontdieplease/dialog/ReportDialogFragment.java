package it.fmistri.dontdieplease.dialog;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.ReportWithEntries;

public class ReportDialogFragment extends DialogFragment {
    private static String EDIT_ARG_KEY = "edit";
    private static String REPORT_ARG_KEY = "report_index";

    private ReportDialogViewModel viewModel;

    private Spinner[] spinners;

    // Parameters
    private boolean edit;
    private int report_index;

    /**
     * Instantiate a new fragment.
     * A ReportDialogFragment can be in 'insertion' or 'editing' mode. When in insertion mode, a
     * new report will be created from the user input. When in editing mode, the fragment will be
     * populated with previously existing data and the user will be able to update it(the actual
     * report data will be accessed from a given index that will be looked up in a cached array
     * of ReportWithEntries).
     * @param edit Whether this fragment instance is for the insertion of a new report or is
     *             for editing a previously added report.
     * @param report_index The report index used for lookup in a ReportWithEntries array(this value
     *                     isn't used if edit = false).
     * @return A new fragment instance.
     */
    public static ReportDialogFragment newInstance(boolean edit, int report_index) {
        // Create a bundle of arguments
        Bundle args = new Bundle();
        args.putBoolean(EDIT_ARG_KEY, edit);
        args.putInt(REPORT_ARG_KEY, report_index);

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
        report_index = getArguments().getInt(REPORT_ARG_KEY, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Set dialog title
        getDialog().setTitle(R.string.dialog_report_title);

        return inflater.inflate(R.layout.dialog_report, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save useful views
        spinners = new Spinner[] {
                getView().findViewById(R.id.first_line).findViewById(R.id.category_spinner),
                getView().findViewById(R.id.second_line).findViewById(R.id.category_spinner)
        };

        // Observe changes from in the database from a ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(ReportDialogViewModel.class);
        viewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<Category[]>() {
            @Override
            public void onChanged(Category[] categories) {
                changedCategories(categories);
            }
        });

        // Set callback for the 'done' button
        ((TextView) view.findViewById(R.id.done)).setOnClickListener(new View.OnClickListener() {
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

    /**
     * Callback for the Category LiveData.
     * When categories are updated(should not happen dynamically anyway), update the
     * spinners.
     * @param categories The updated array of categories.
     */
    private void changedCategories(Category[] categories) {
        // Add the categories to the spinners
        for (int i = 0; i < spinners.length; i++) {
            spinners[i].setAdapter(new CategoryAdapter(getContext(), R.layout.spinner_item,
                    categories));

            // Set default category(if possible, different for each spinner)
            if (categories.length > i)
                spinners[i].setSelection(i);
        }
    }

    /**
     * Submit the collected data(report) and dismiss.
     * Callback for the done button click.
     */
    private void done() {
        // TODO: submit collected data(report)
        dismiss();
    }
}
