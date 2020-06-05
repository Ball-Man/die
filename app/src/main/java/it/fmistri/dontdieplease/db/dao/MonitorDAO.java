package it.fmistri.dontdieplease.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;

import it.fmistri.dontdieplease.db.Monitor;

@Dao
public interface MonitorDAO {
    @Insert
    public void addMonitors(Monitor... monitors);

    @Query("SELECT * FROM `Monitor`")
    public LiveData<Monitor[]> getMonitors();

    @Delete
    public void removeMonitor(Monitor monitor);

    @Query("SELECT `Monitor`.* " +
            "FROM `Monitor` INNER JOIN " +
            "`Category` ON `Monitor`.`category_name`=`name` INNER JOIN " +
            "`Entry` ON `Entry`.`category_name`=`name` INNER JOIN " +
            "`Report` ON `r_id`=`report_id` " +
            "WHERE `end_date` < :valid_date AND `date` BETWEEN `start_date` AND `end_date` " +
            "AND `Monitor`.`category_name` IN (:check_categories) " +
            "GROUP BY `Monitor`.`m_id`, `Monitor`.`start_date`, `Monitor`.`end_date`, " +
            "`Monitor`.`threshold`, `Monitor`.`category_name` " +
            "HAVING AVG(`Entry`.value) > `threshold`")
    public LiveData<Monitor[]> getTriggeredMonitors(Date valid_date, String[] check_categories);
}
