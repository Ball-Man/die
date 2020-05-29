package it.fmistri.dontdieplease.db;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

@TypeConverters(DateConverter.class)
public class AverageEntry implements Categorized{
    @ColumnInfo
    public double avg_value;

    @ColumnInfo
    public String category_name;

    @Override
    public double getValue() {
        return avg_value;
    }

    @Override
    public String getCategoryName() {
        return category_name;
    }
}
