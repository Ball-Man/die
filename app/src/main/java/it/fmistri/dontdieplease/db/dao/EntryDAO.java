package it.fmistri.dontdieplease.db.dao;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addEntries(Entry... entries);
}
