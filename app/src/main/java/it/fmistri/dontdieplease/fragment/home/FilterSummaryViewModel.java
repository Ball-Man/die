package it.fmistri.dontdieplease.fragment.home;

import android.os.Bundle;

import androidx.lifecycle.LiveData;

import it.fmistri.dontdieplease.db.AverageEntry;
import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.ReportWithEntries;
import it.fmistri.dontdieplease.fragment.summary.SummaryViewModel;

/**
 * Made to work with {@link FilterSummaryFragment}.
 */
public class FilterSummaryViewModel extends SummaryViewModel {
    private LiveData<AverageEntry[]> averages;
    private LiveData<ReportWithEntries[]> reports;

    // Arguments
    private String categoryName;
    private double minValue;
    private double maxValue;

    @Override
    public LiveData<AverageEntry[]> getAverages() {
        if (averages == null)
            averages = DieDatabase.getInstance(null).reportDAO()
                    .getAverage(categoryName, minValue, maxValue);
        return averages;
    }

    @Override
    public LiveData<ReportWithEntries[]> getReports() {
        if (reports == null)
            reports = DieDatabase.getInstance(null).reportDAO()
                    .getReportsWithEntries(categoryName, minValue, maxValue);
        return reports;
    }

    @Override
    public void setArgs(Bundle args) {
        // Clean cached data
        averages = null;
        reports = null;

        categoryName = args.getString(FilterSummaryFragment.CATEGORY_NAME_KEY);
        minValue = args.getDouble(FilterSummaryFragment.MINVALUE_KEY);
        maxValue = args.getDouble(FilterSummaryFragment.MAXVALUE_KEY);
    }
}
