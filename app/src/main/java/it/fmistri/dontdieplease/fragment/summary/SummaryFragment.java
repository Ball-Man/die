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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import it.fmistri.dontdieplease.R;
import it.fmistri.dontdieplease.db.AverageEntry;
import it.fmistri.dontdieplease.db.ReportWithEntries;

/**
 * Fragment used to show report summaries given a set of fragments.
 * This is a base class, and should be derived in order to get the desired behaviour. A summary is
 * made up of two main parts: an average of all the values, the list of all the reports.
 * This information shall be calculated by a proper (implementation of) {@link SummaryViewModel},
 * which is used to retrieve the necessary information(the average values, packed into a collection
 * of {@link AverageEntry}s and the list of reports, embedded into a
 * collection of {@link it.fmistri.dontdieplease.db.ReportWithEntries}.
 */
public abstract class SummaryFragment extends Fragment {
    /**
     * The ViewModel associated to this specific class of SummaryFragment. When deriving
     * this Fragment class, override this with your implementation of {@link SummaryViewModel}.
     */
    private static Class viewModelClass = SummaryViewModel.class;

    /**
     * Actual instance of the ViewModel. This is populated by
     * {@link SummaryFragment#onViewCreated(View, Bundle)} by using the class provided in
     * {@link SummaryFragment#viewModelClass}.
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
     * @return The {@link SummaryViewModel} associated to the current instance.
     */
    private SummaryViewModel getViewModel() {
        return viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Save useful views
        averageListView = view.findViewById(R.id.list_average);
        summaryListView = view.findViewById(R.id.list_summary);

        // Retrieve ViewModel
        viewModel = (SummaryViewModel) new ViewModelProvider(requireActivity()).get(viewModelClass);

        // Observe changes from the model
        viewModel.getAverages().observe(getViewLifecycleOwner(), new Observer<AverageEntry[]>() {
            @Override
            public void onChanged(AverageEntry[] averageEntries) {
                averageListView.setAdapter(new AverageReportAdapter(getContext(), averageEntries));
            }
        });

        viewModel.getReports().observe(getViewLifecycleOwner(),
                new Observer<ReportWithEntries[]>() {
            @Override
            public void onChanged(ReportWithEntries[] reportWithEntries) {
                // TODO: Set report adapter
            }
        });
    }
}
