package it.fmistri.dontdieplease.db;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotificationsSettings {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo
    public boolean enabled;

    @ColumnInfo
    public long hour;

    @ColumnInfo
    public long minute;
}
