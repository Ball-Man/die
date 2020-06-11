package it.fmistri.dontdieplease.fragment.statistics;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.GregorianCalendar;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.StatisticEntry;

/**
 * Made to work with {@link StatisticsFragment}.
 */
public class StatisticsViewModel extends ViewModel {
    private LiveData<StatisticEntry[]> entries;
    private String category;

    public LiveData<StatisticEntry[]> getLastWeeksEntries(String category) {
        if (category == null || entries == null || !category.equals(this.category)) {
            // Calculate last weeks timestamps
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            long today = calendar.getTimeInMillis();

            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            long lastWeek = calendar.getTimeInMillis();

            entries = DieDatabase.getInstance(null).entryDAO().getEntries(category, lastWeek,
                    today);
            this.category = category;
        }

        return entries;
    }
}
