package it.fmistri.dontdieplease.db;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import java.util.Date;

@TypeConverters(DateConverter.class)
public class SummaryReport {
    @ColumnInfo
    public double avg_value;

    @ColumnInfo
    public Date date;

    @ColumnInfo
    public String category_name;
}
