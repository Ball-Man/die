package it.fmistri.dontdieplease.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.fmistri.dontdieplease.db.Entry;

@Dao
public interface EntryDAO {
    @Query("SELECT * FROM Entry")
    public List<Entry> getEntries();

    @Query("SELECT `Entry`.* FROM `Entry` INNER JOIN `Report` " +
            "ON `r_id`=`report_id` WHERE `date` BETWEEN :startTime AND :endTime " +
            "AND `category_name`=:category")
    public LiveData<Entry[]> getEntries(String category, long startTime, long endTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addEntries(Entry... entries);
}
