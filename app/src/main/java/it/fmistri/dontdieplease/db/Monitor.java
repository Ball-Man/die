package it.fmistri.dontdieplease.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity=Category.class, parentColumns = "name", childColumns = "category_name")
})
public class Monitor {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    public long m_id;

    @ColumnInfo
    public double threshold;

    @ColumnInfo
    public long start_date;

    @ColumnInfo
    public long end_date;

    @ColumnInfo
    @NonNull
    public String category_name = "heart";
}
