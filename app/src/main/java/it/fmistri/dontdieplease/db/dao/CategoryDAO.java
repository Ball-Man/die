package it.fmistri.dontdieplease.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import it.fmistri.dontdieplease.db.Category;

@Dao
public interface CategoryDAO {
    @Insert
    public abstract Long[] addCategories(Category... categories);

    @Query("SELECT * FROM Category ORDER BY `importance` DESC")
    public abstract LiveData<Category[]> getCategories();

    @Query("DELETE FROM Category")
    public abstract void clear();
}
