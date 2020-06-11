package it.fmistri.dontdieplease.fragment.statistics;

import android.text.format.DateUtils;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.Report;
import it.fmistri.dontdieplease.db.StatisticEntry;
import it.fmistri.dontdieplease.util.DateUtil;

/**
 * Made to work with {@link StatisticsFragment}.
 */
public class StatisticsViewModel extends ViewModel {
    private LiveData<StatisticEntry[]> entries;
    private String category;
    private LiveData<Report[]> reports;

    /**
     * Retrieve all the entries from last week that match the given category.
     * @param category The selected category.
     * @return An observable of the entries, enriched with their dates.
     */
    public LiveData<StatisticEntry[]> getLastWeeksEntries(String category) {
        if (category == null || entries == null || !category.equals(this.category)) {
            // Calculate last weeks timestamps
            GregorianCalendar calendar = new GregorianCalendar();
            Pair<Date, Date> range = DateUtil.getWeekRange(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            entries = DieDatabase.getInstance(null).entryDAO().getEntries(category,
                    range.first.getTime(),
                    range.second.getTime());
            this.category = category;
        }

        return entries;
    }

    public LiveData<Report[]> getLastWeeksReportCount() {
        //if ()
        return null;
    }
}
