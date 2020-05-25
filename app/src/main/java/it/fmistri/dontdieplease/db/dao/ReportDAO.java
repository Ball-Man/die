package it.fmistri.dontdieplease.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;

import it.fmistri.dontdieplease.db.Report;
import it.fmistri.dontdieplease.db.ReportWithEntries;
import it.fmistri.dontdieplease.db.SummaryReport;

@Dao
public interface ReportDAO {
    @Query("SELECT AVG(`value`) as `avg_value`, `date`, `category_name` FROM `Report` INNER JOIN `Entry` ON `report_id`=`r_id` INNER JOIN `Category` ON `category_name`=`name` WHERE `date`=:date GROUP BY `date`, `category_name`")
    public LiveData<SummaryReport[]> getSummary(Date date);

    @Query("SELECT * FROM `Report`")
    public  LiveData<ReportWithEntries[]> getReportsWithEntries();

    @Query("SELECT * FROM `Report` WHERE `r_id`=:id")
    public LiveData<ReportWithEntries> getReportWithEntries(Integer id);

    @Insert
    public void addReport(Report report);
}
