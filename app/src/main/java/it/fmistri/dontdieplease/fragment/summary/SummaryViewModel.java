package it.fmistri.dontdieplease.fragment.summary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.fmistri.dontdieplease.db.AverageEntry;
import it.fmistri.dontdieplease.db.ReportWithEntries;

/**
 * ViewModel used as adaptor for {@link SummaryFragment}. This is a base class, and shall be derived
 * coupled with a specific {@link SummaryFragment} implementation in mind.
 */
public abstract class SummaryViewModel extends ViewModel {
    /**
     * Retrieve an observable list of {@link AverageEntry}s that will be used by the
     * {@link SummaryFragment} to update the UI.
     * @return An observable list of {@link AverageEntry}s.
     */
    public abstract LiveData<AverageEntry[]> getAverages();

    /**
     * Retrieve an observable list of {@link ReportWithEntries}s that will be used by the
     * {@link SummaryFragment} to update the UI with the list of all the reports in this
     * selection. Note that ideally the returned reports should be the same ones used for the
     * summary(e.g. an average).
     * @return An observable list of {@link ReportWithEntries}s.
     */
    public abstract  LiveData<ReportWithEntries[]> getReports();
}
