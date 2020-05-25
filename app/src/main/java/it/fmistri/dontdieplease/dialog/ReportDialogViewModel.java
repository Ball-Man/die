package it.fmistri.dontdieplease.dialog;

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
class ReportDialogViewModel extends ViewModel {
    private LiveData<Category[]> categories;
    private LiveData<ReportWithEntries[]> reports;

    /**
     * @return An observable of the accepted categories.
     */
    LiveData<Category[]> getCategories() {
        if (categories == null)
            categories = DieDatabase.getInstance(null).categoryDAO().getCategories();
        return categories;
    }

    /**
     * @return An observable of all the reports with their entries.
     */
    LiveData<ReportWithEntries[]> getReports() {
        if (reports == null)
            reports = DieDatabase.getInstance(null).reportDAO().getReportsWithEntries();
        return reports;
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
                DieDatabase.getInstance(null).reportDAO().addReport(report.report);
                DieDatabase.getInstance(null).entryDAO().addEntries(
                        report.entries.toArray(new Entry[0]));
            }
        });
    }
}
