package it.fmistri.dontdieplease.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.AverageEntry;

/**
 * Fragment used to show report summaries given a set of fragments.
 * This is a base class, and should be derived in order to get the desired behaviour. A summary is
 * made up of two main parts: an average of all the values, the list of all the reports.
 * This information shall be calculated by a proper (implementation of) {@link SummaryViewModel},
 * which is used to retrieve the necessary information(the average values, packed into a collection
 * of {@link AverageEntry}s and the list of reports, embedded into a
 * collection of {@link it.fmistri.dontdieplease.db.ReportWithEntries}.
 * Note that in order to pass arbitrary data to a {@link SummaryViewModel} {@link Bundle}s are
 * used(see {@link SummaryViewModel#setArgs(Bundle)}.
 * Also note that the ViewModel instance should be set in
 * {@link SummaryFragment#onCreate(Bundle)} using .
 */
public abstract class SummaryFragment extends Fragment {
    /**
     * Actual instance of the ViewModel. This is populated by
     * {@link SummaryFragment#onCreate(Bundle)} (View, Bundle)} by using the class provided in
     * {@link SummaryFragment#getViewModelClass()}.
     */
    private SummaryViewModel viewModel;

    /**
     * View used to show the average values for the summary entries.
     */
    private ListView averageListView;

    /**
     * View used to show all the reports of this summary.
     */
    private ListView summaryListView;

    /**
     * @return the ViewModel type associated to this fragment. This will be used inside
     * {@link SummaryFragment#onCreate(Bundle)} to automatically manage the ViewModel.
     * This will throw an exception at runtime; users should override this to return a concrete
     * implementation of {@link SummaryViewModel}.
     */
    protected Class getViewModelClass() {
        return SummaryViewModel.class;
    }

    /**
     * @return The {@link SummaryViewModel} associated to the current instance.
     */
    protected SummaryViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Fragment onCreate event(call to super).
     * Initialize the ViewModel
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve ViewModel(this will throw an exception at runtime. Users should override
        // onCreate and use their
        viewModel = (SummaryViewModel) new ViewModelProvider(requireActivity())
                .get(getViewModelClass());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    /**
     * Fragment onViewCreated event(call to super).
     * Start observing changes from the ViewModel.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save useful views
        averageListView = view.findViewById(R.id.list_average);
        summaryListView = view.findViewById(R.id.list_summary);

        // Observe changes from the model
        SummaryViewModel viewModel = getViewModel();
        viewModel.getAverages().observe(getViewLifecycleOwner(), new Observer<AverageEntry[]>() {
            @Override
            public void onChanged(AverageEntry[] averageEntries) {
                averageListView.setAdapter(new CategorizedAdapter(getContext(), averageEntries));
            }
        });

//        viewModel.getReports().observe(getViewLifecycleOwner(),
//                new Observer<ReportWithEntries[]>() {
//            @Override
//            public void onChanged(ReportWithEntries[] reportWithEntries) {
//                // TODO: Set report adapter
//            }
//        });
    }
}
