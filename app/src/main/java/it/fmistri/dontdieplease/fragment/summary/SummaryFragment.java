package it.fmistri.dontdieplease.fragment.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
 */
public abstract class SummaryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container);
    }
}
