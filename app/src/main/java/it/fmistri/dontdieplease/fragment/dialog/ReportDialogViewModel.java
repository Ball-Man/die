package it.fmistri.dontdieplease.fragment.dialog;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import it.fmistri.dontdieplease.db.Category;
import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.Entry;
import it.fmistri.dontdieplease.db.ReportWithEntries;

/**
 * Adapt database model to the view(ReportDialogFragment).
 */
public class ReportDialogViewModel extends ViewModel {
    private LiveData<Category[]> categories;
    private LiveData<ReportWithEntries> report;
    private long reportId;

    /**
     * @return An observable of the accepted categories.
     */
    LiveData<Category[]> getCategories() {
        if (categories == null)
            categories = DieDatabase.getInstance(null).categoryDAO().getCategories();
        return categories;
    }

    /**
     * Select a report (presumably) for editing purposes.
     * @param id The id of the selected report.
     * @return An observable of the report currently selected.
     */
    LiveData<ReportWithEntries> getReport(long id) {
        if (report == null || reportId != id) {
            report = DieDatabase.getInstance(null).reportDAO().getReportWithEntries(id);
            reportId = id;
        }
        return report;
    }

    /**
     * Add a report to the DB using a composite object(ReportWithEntries).
     * The object will be inserted subdividing its Report and its Entries that will be independently
     * added to the DB through their DAOs.
     */
    void addReport(final ReportWithEntries report) {
        // Separate the content of the composite object and make the appropriate
        // queries on the DAOs.
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DieDatabase.getInstance(null).reportDAO().addReportWithEntries(
                        report.report, report.entries.toArray(new Entry[0]));
            }
        });
    }
}
