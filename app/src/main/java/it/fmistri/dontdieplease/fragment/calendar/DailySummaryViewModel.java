package it.fmistri.dontdieplease.fragment.calendar;

import android.os.Bundle;
import android.util.Pair;

import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.AverageEntry;
import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.ReportWithEntries;
import it.fmistri.dontdieplease.fragment.summary.SummaryViewModel;
import it.fmistri.dontdieplease.util.DateUtil;

/**
 * Implementation of {@link SummaryViewModel} for daily reports(get reports on a daily basis, mainly
 * from a {@link android.widget.CalendarView}).
 */
public class DailySummaryViewModel extends SummaryViewModel {
    static String YEAR = "year";
    static String MONTH = "month";
    static String DAY = "DAY";

    private LiveData<AverageEntry[]> averageEntries;
    private LiveData<ReportWithEntries[]> reports;

    // Stored arguments
    private int year, month, day;

    @Override
    public LiveData<AverageEntry[]> getAverages() {
        if (averageEntries == null) {
            // Submit the selected date to the DAO and retrieve the observable collection
            Pair<Date, Date> dates = DateUtil.getDayRange(year, month, day);
            averageEntries = DieDatabase.getInstance(null).reportDAO().getAverageDate(
                dates.first, dates.second
            );
        }
        return averageEntries;
    }

    @Override
    public LiveData<ReportWithEntries[]> getReports() {
        if (reports == null) {
            // Submit the selected date to the DAO and retrieve the observable collection
            Pair<Date, Date> dates = DateUtil.getDayRange(year, month, day);
            reports = DieDatabase.getInstance(null).reportDAO().getReportsWithEntries(
                    dates.first, dates.second
            );
        }
        return reports;
    }

    @Override
    public void setArgs(Bundle args) {
        // Clear data on argument change
        averageEntries = null;
        reports = null;

        year = args.getInt(YEAR);
        month = args.getInt(MONTH);
        day = args.getInt(DAY);
    }
}
