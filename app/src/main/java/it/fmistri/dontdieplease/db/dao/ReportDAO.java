package it.fmistri.dontdieplease.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.Date;

import it.fmistri.dontdieplease.db.DieDatabase;
import it.fmistri.dontdieplease.db.Entry;
import it.fmistri.dontdieplease.db.Report;
import it.fmistri.dontdieplease.db.ReportWithEntries;
import it.fmistri.dontdieplease.db.AverageEntry;

@Dao
public abstract class ReportDAO {
    @Query("SELECT AVG(`value`) as `avg_value`, `category_name` FROM `Report` " +
            "INNER JOIN `Entry` ON `report_id`=`r_id` " +
            "INNER JOIN `Category` ON `category_name`=`name` " +
            "WHERE `date` BETWEEN :startDate AND :endDate GROUP BY `date`, `category_name`")
    public abstract LiveData<AverageEntry[]> getAverageDate(Date startDate, Date endDate);

    @Transaction
    @Query("SELECT * FROM `Report`")
    public abstract LiveData<ReportWithEntries[]> getReportsWithEntries();

    @Transaction
    @Query("SELECT * FROM `Report` WHERE `r_id`=:id")
    public abstract LiveData<ReportWithEntries> getReportWithEntries(Integer id);

    @Insert
    public abstract long addReport(Report report);

    @Transaction
    public void addReportWithEntries(Report report, Entry... entries) {
        // Insert report and get the id
        long report_id = addReport(report);

        // Set the resulted id to the entries and add them
        DieDatabase db = DieDatabase.getInstance(null);
        for (Entry entry : entries) {
            entry.report_id = report_id;
            db.entryDAO().addEntries(entry);
        }
    }
}
