package it.fmistri.dontdieplease.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import it.fmistri.dontdieplease.db.NotificationsSettings;

@Dao
public interface NotificationsDAO {
    @Query("SELECT * FROM `NotificationsSettings`")
    public LiveData<NotificationsSettings[]> getNotificationsSettings();

    @Update
    public void updateNotificationsSettings(NotificationsSettings settings);
}
