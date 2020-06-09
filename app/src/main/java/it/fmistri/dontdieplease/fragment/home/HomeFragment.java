package it.fmistri.dontdieplease.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.fragment.dialog.CategoryAdapter;
import it.fmistri.dontdieplease.fragment.dialog.ReportDialogFragment;

/**
 * Fragment implementation for the home tab of the application.
 */
public class HomeFragment extends Fragment implements Observer<Category[]> {
    private HomeViewModel viewModel;

    // Useful views
    private Spinner categorySpinner;
    private EditText minValueEdit;
    private EditText maxValueEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Useful views
        categorySpinner = view.findViewById(R.id.category_spinner);
        minValueEdit = view.findViewById(R.id.from_edit);
        maxValueEdit = view.findViewById(R.id.to_edit);

        // Retrieve ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        // Observe changes
        viewModel.getCategories().observe(getViewLifecycleOwner(), this);

        ((FloatingActionButton) view.findViewById(R.id.add_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClick();
            }
        });

        ((MaterialButton) view.findViewById(R.id.search_button))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchClick();
            }
        });
    }

    /**
     * Callback for the add button click.
     */
    private void addClick() {
        Log.d("HOME", "button pressed");
        ReportDialogFragment.newInstance(false, 0)
                .show(getChildFragmentManager(), "report_dialog");
    }

    /**
     * Callback for the search button click.
     */
    private void searchClick() {
        // TODO: Validate input
        Fragment fragment = FilterSummaryFragment.newInstance(
                ((Category) categorySpinner.getSelectedItem()).name,
                Double.parseDouble(minValueEdit.getText().toString()),
                Double.parseDouble(maxValueEdit.getText().toString()));

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_home_search, fragment)
                .commit();
    }

    /**
     * On categories updated, update filter category spinner.
     * @param categories The updated categories.
     */
    @Override
    public void onChanged(Category[] categories) {
        categorySpinner.setAdapter(new CategoryAdapter(requireContext(), categories));
    }
}
