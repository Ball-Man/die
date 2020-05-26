package it.fmistri.dontdieplease.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
@TypeConverters(DateConverter.class)
public class Report {
    @PrimaryKey(autoGenerate = true)
    public Long r_id;

    @ColumnInfo
    public String note;

    @ColumnInfo
    public Date date;
}
